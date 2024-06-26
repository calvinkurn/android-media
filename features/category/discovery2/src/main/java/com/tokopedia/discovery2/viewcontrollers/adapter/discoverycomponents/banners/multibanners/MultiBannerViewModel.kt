package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.NAVIGATION
import com.tokopedia.discovery2.Constant.REDIRECTION
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.BANNER_SUBSCRIPTION_DEFAULT_STATUS
import com.tokopedia.discovery2.Utils.Companion.BANNER_SUBSCRIPTION_REMINDED_STATUS
import com.tokopedia.discovery2.Utils.Companion.BANNER_SUBSCRIPTION_UNREMINDED_STATUS
import com.tokopedia.discovery2.data.BannerAction
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoveryext.checkForNullAndSize
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.SubScribeToUseCase
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.model.PushNotificationBannerSubscription
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val PROMO_CODE = "Promo Code"
const val BANNER_ACTION_CODE = "CODE"
private const val SINGLE_PROMO_CODE = "single_promo_code"
private const val DOUBLE_PROMO_CODE = "double_promo_code"

class MultiBannerViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(components), CoroutineScope {
    private val bannerData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val showLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val applinkCheck: MutableLiveData<String> = MutableLiveData()
    private val refreshPage: MutableLiveData<Boolean> = MutableLiveData()

    private val pushNotificationBannerSubscriptionUpdatedData: MutableLiveData<PushNotificationBannerSubscription> = MutableLiveData()
    private val pushNotificationBannerSubscriptionInitData: MutableLiveData<PushNotificationBannerSubscription> = MutableLiveData()

    private val _hideShimmer = SingleLiveEvent<Boolean>()
    private val _showErrorState = SingleLiveEvent<Boolean>()
    private val _redirectionToTab = SingleLiveEvent<String?>()

    private var isDarkMode: Boolean = false
    private var pushNotificationBannerStatus: MutableMap<Int, Int> = mutableMapOf()

    @JvmField
    @Inject
    var checkPushStatusUseCase: CheckPushStatusUseCase? = null

    @JvmField
    @Inject
    var subScribeToUseCase: SubScribeToUseCase? = null

    @JvmField
    @Inject
    var bannerUseCase: BannerUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        bannerData.value = components
        pushNotificationBannerSubscriptionUpdatedData.value = PushNotificationBannerSubscription()
        pushNotificationBannerSubscriptionInitData.value = PushNotificationBannerSubscription()
    }

    fun getComponentData(): LiveData<ComponentsItem> = bannerData
    fun getPushNotificationBannerSubscriptionUpdated(): LiveData<PushNotificationBannerSubscription> = pushNotificationBannerSubscriptionUpdatedData
    fun getPushNotificationBannerSubscriptionInit(): LiveData<PushNotificationBannerSubscription> = pushNotificationBannerSubscriptionInitData
    fun getShowLoginData(): LiveData<Boolean> = showLogin
    fun getBannerUrlHeight() = Utils.extractDimension(bannerData.value?.data?.firstOrNull()?.imageUrlDynamicMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(bannerData.value?.data?.firstOrNull()?.imageUrlDynamicMobile, "width")
    fun checkApplink(): LiveData<String> = applinkCheck
    fun isPageRefresh(): LiveData<Boolean> = refreshPage
    val redirectedTab: LiveData<String?> = _redirectionToTab
    val hideShimmer: LiveData<Boolean> = _hideShimmer
    val showErrorState: LiveData<Boolean> = _showErrorState

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchBannerData()
    }

    private fun fetchBannerData() {
        if (components.properties?.dynamic == true) {
            launchCatchError(block = {
                if (bannerUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint, isDarkMode) == true) {
                    if (components.data.isNullOrEmpty()) {
                        _hideShimmer.value = true
                    }
                    bannerData.value = components
                }
            }, onError = {
                    components.noOfPagesLoaded = 1
                    if (it is UnknownHostException || it is SocketTimeoutException) {
                        components.verticalProductFailState = true
                        _showErrorState.value = true
                    } else {
                        _hideShimmer.value = true
                    }
                })
        }
    }

    fun layoutSelector(): Int {
        return when (components.name) {
            ComponentNames.SingleBanner.componentName -> R.layout.disco_shimmer_single_banner_layout
            ComponentNames.DoubleBanner.componentName -> R.layout.disco_shimmer_double_banner_layout
            ComponentNames.TripleBanner.componentName -> R.layout.disco_shimmer_triple_banner_layout
            ComponentNames.QuadrupleBanner.componentName -> R.layout.disco_shimmer_quadruple_banner_layout
            else -> R.layout.multi_banner_layout
        }
    }

    fun onBannerClicked(
        position: Int,
        context: Context,
        defaultErrorMessage: String
    ) {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            when (listItem[position].action) {
                BannerAction.APPLINK.name -> {
                    pageRedirection(position, context)
                }
                BannerAction.CODE.name -> copyCodeToClipboard(position)
                BannerAction.PUSH_NOTIFIER.name -> subscribeUnsubscribeUserForPushNotification(
                    position = position,
                    defaultErrorMessage = defaultErrorMessage
                )
                BannerAction.LOGIN.name -> loginUser(position, context)
                else -> navigation(position, context)
            }
        }
    }

    private fun pageRedirection(position: Int, context: Context) {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            when (listItem[position].moveAction?.type) {
                REDIRECTION -> {
                    if (!listItem[position].moveAction?.value.isNullOrEmpty()) {
                        navigate(context, listItem[position].moveAction?.value)
                    }
                }
                NAVIGATION -> {
                    _redirectionToTab.value = listItem[position].moveAction?.value
                }
                else -> {
                    navigation(position, context)
                }
            }
        }
    }

    private fun loginUser(position: Int, context: Context) {
        if (isUserLoggedIn()) navigation(position, context) else refreshPage.value = true
    }

    private fun copyCodeToClipboard(position: Int) {
        try {
            bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
                val item = listItem[position]
                (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)
                    ?.setPrimaryClip(ClipData.newPlainText(PROMO_CODE, item.code))
                if (!item.applinks.isNullOrEmpty()) {
                    applinkCheck.value =
                        item.applinks
                } else {
                    applinkCheck.value = ""
                }
            }
        } catch (e: Exception) {
            Utils.logException(e)
        }
    }

    private fun navigation(position: Int, context: Context) {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            val item = listItem[position]
            if (!item.applinks.isNullOrEmpty()) {
                navigate(context, item.applinks)
            }
        }
    }

    private fun subscribeUnsubscribeUserForPushNotification(position: Int, defaultErrorMessage: String) {
        if (isUserLoggedIn()) {
            val isSubscribed = pushNotificationBannerStatus[position] == BANNER_SUBSCRIPTION_REMINDED_STATUS
            launchCatchError(
                block = {
                    if (isSubscribed) {
                        unsubscribeUserForPushNotification(position)
                    } else {
                        subscribeUserForPushNotification(position)
                    }
                },
                onError = {
                    pushNotificationBannerSubscriptionUpdatedData.value = PushNotificationBannerSubscription(
                        position = position,
                        errorMessage = defaultErrorMessage,
                        isSubscribed = isSubscribed
                    )
                    it.printStackTrace()
                }
            )
        } else {
            showLogin.value = true
        }
    }

    private suspend fun subscribeUserForPushNotification(
        position: Int
    ) {
        val response = subScribeToUseCase?.subscribeToPush(getCampaignId(position))
        if (response != null && response.isSuccess()) {
            val errorMessage = response.getErrorMessage()
            pushNotificationBannerSubscriptionUpdatedData.value = PushNotificationBannerSubscription(
                position = position,
                errorMessage = errorMessage,
                isSubscribed = true
            )
            if (errorMessage.isEmpty()) {
                pushNotificationBannerStatus[position] = BANNER_SUBSCRIPTION_REMINDED_STATUS
            }
        }
    }

    private suspend fun unsubscribeUserForPushNotification(
        position: Int
    ) {
        val response = subScribeToUseCase?.unSubscribeToPush(getCampaignId(position))
        if (response != null && response.isSuccess()) {
            val errorMessage = response.getErrorMessage()
            pushNotificationBannerSubscriptionUpdatedData.value = PushNotificationBannerSubscription(
                position = position,
                errorMessage = errorMessage,
                isSubscribed = false
            )
            if (errorMessage.isEmpty()) {
                pushNotificationBannerStatus[position] = BANNER_SUBSCRIPTION_UNREMINDED_STATUS
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

    fun campaignSubscribedStatus(position: Int) {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            when (listItem[position].action) {
                BannerAction.PUSH_NOTIFIER.name -> checkUserPushStatus(position)
                BannerAction.LOCAL_CALENDAR.name -> checkUserLocalPushStatus(position)
            }
        }
    }

    private fun checkUserPushStatus(position: Int) {
        if (isUserLoggedIn()) {
            launchCatchError(block = {
                val pushSubscriptionResponse = checkPushStatusUseCase?.checkPushStatus(getCampaignId(position))
                pushNotificationBannerSubscriptionInitData.value = PushNotificationBannerSubscription(
                    position = position,
                    errorMessage = String.EMPTY,
                    isSubscribed = pushSubscriptionResponse?.notifierCheckReminder?.status == BANNER_SUBSCRIPTION_REMINDED_STATUS
                )
                pushNotificationBannerStatus[position] = pushSubscriptionResponse?.notifierCheckReminder?.status ?: BANNER_SUBSCRIPTION_DEFAULT_STATUS
            }, onError = {
                    it.printStackTrace()
                })
        }
    }

    private fun getCampaignId(position: Int): Long {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            val parameterList: List<String>? = listItem[position].paramsMobile?.split("=")
            if (parameterList != null && parameterList.size >= 2) {
                return parameterList[1].toLongOrZero()
            }
        }
        return 0
    }

    private fun checkUserLocalPushStatus(position: Int) {
        launchCatchError(block = {
            val pushSubscriptionResponse = checkPushStatusUseCase?.checkPushStatus(getCampaignId(position))
            pushNotificationBannerSubscriptionInitData.value = PushNotificationBannerSubscription(
                position = position,
                errorMessage = String.EMPTY,
                isSubscribed = pushSubscriptionResponse?.notifierCheckReminder?.status == BANNER_SUBSCRIPTION_REMINDED_STATUS
            )
            pushNotificationBannerStatus[position] = pushSubscriptionResponse?.notifierCheckReminder?.status ?: BANNER_SUBSCRIPTION_DEFAULT_STATUS
        }, onError = {
                it.printStackTrace()
            })
    }

    fun getComponentPosition() = position
    fun shouldShowShimmer(): Boolean {
        return components.properties?.dynamic == true && components.noOfPagesLoaded != 1 && !components.verticalProductFailState
    }

    fun reload() {
        components.noOfPagesLoaded = 0
        fetchBannerData()
    }

    fun setComponentPromoNameForCoupons(bannerName: String, data: List<DataItem>) {
        data.forEach {
            if (it.action == BANNER_ACTION_CODE) {
                when (bannerName) {
                    ComponentNames.SingleBanner.componentName -> it.componentPromoName = SINGLE_PROMO_CODE

                    ComponentNames.DoubleBanner.componentName -> it.componentPromoName = DOUBLE_PROMO_CODE
                }
            }
        }
    }
    fun checkForDarkMode(context: Context?) {
        if (context != null) {
            isDarkMode = context.isDarkMode()
        }
    }
}
