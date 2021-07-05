package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.BannerAction
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoveryext.checkForNullAndSize
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.SubScribeToUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val PROMO_CODE = "Promo Code"

class MultiBannerViewModel(val application: Application, var components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val bannerData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val pushBannerStatus: MutableLiveData<Int> = MutableLiveData()
    private val pushBannerSubscription: MutableLiveData<Int> = MutableLiveData()
    private val showLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val applinkCheck: MutableLiveData<String> = MutableLiveData()
    private val refreshPage: MutableLiveData<Boolean> = MutableLiveData()

    @Inject
    lateinit var checkPushStatusUseCase: CheckPushStatusUseCase

    @Inject
    lateinit var subScribeToUseCase: SubScribeToUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        bannerData.value = components
        pushBannerStatus.value = Utils.BANNER_SUBSCRIPTION_DEFAULT_STATUS
        pushBannerSubscription.value = Utils.BANNER_SUBSCRIPTION_DEFAULT_STATUS

    }


    fun getComponentData(): LiveData<ComponentsItem> = bannerData
    fun getPushBannerStatusData(): LiveData<Int> = pushBannerStatus
    fun getShowLoginData(): LiveData<Boolean> = showLogin
    fun getPushBannerSubscriptionData(): LiveData<Int> = pushBannerSubscription
    fun getBannerUrlHeight() = Utils.extractDimension(bannerData.value?.data?.firstOrNull()?.imageUrlDynamicMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(bannerData.value?.data?.firstOrNull()?.imageUrlDynamicMobile, "width")
    fun checkApplink(): LiveData<String> = applinkCheck
    fun isPageRefresh(): LiveData<Boolean> = refreshPage

    fun onBannerClicked(position: Int, context: Context) {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            when (listItem[position].action) {
                BannerAction.APPLINK.name -> navigation(position, context)
                BannerAction.CODE.name -> copyCodeToClipboard(position)
                BannerAction.PUSH_NOTIFIER.name -> subscribeUserForPushNotification(position)
                BannerAction.LOGIN.name -> loginUser(position, context)
                else -> navigation(position, context)
            }
        }
    }

    private fun loginUser(position: Int, context: Context) {
        if (isUserLoggedIn()) navigation(position, context) else refreshPage.value = true
    }

    private fun copyCodeToClipboard(position: Int) {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            val item = listItem[position]
            (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)
                    ?.setPrimaryClip(ClipData.newPlainText(PROMO_CODE, item.code))
            if (!item.applinks.isNullOrEmpty()) applinkCheck.value = item.applinks else applinkCheck.value = ""
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

    private fun subscribeUserForPushNotification(position: Int) {
        if (isUserLoggedIn()) {
            launchCatchError(block = {
                val pushSubscriptionResponse = subScribeToUseCase.subscribeToPush(getCampaignId(position))
                if (pushSubscriptionResponse.notifierSetReminder?.isSuccess == 1 || pushSubscriptionResponse.notifierSetReminder?.isSuccess == 2) {
                    pushBannerStatus.value = position
                }
            }, onError = {
                it.printStackTrace()
            })
        } else {
            showLogin.value = true
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
                val pushSubscriptionResponse = checkPushStatusUseCase.checkPushStatus(getCampaignId(position))
                if (pushSubscriptionResponse.notifierCheckReminder?.status == 1) {
                    pushBannerSubscription.value = position
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    private fun getCampaignId(position: Int): Int {
        bannerData.value?.data.checkForNullAndSize(position)?.let { listItem ->
            val parameterList: List<String>? = listItem[position].paramsMobile?.split("=")
            if (parameterList != null && parameterList.size >= 2) {
                return parameterList[1].toIntOrZero()
            }
        }
        return 0
    }

    private fun checkUserLocalPushStatus(position: Int) {
        launchCatchError(block = {
            val pushSubscriptionResponse = checkPushStatusUseCase.checkPushStatus(getCampaignId(position))
            if (pushSubscriptionResponse.notifierCheckReminder?.status == 1) {
                pushBannerSubscription.value = position
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getComponentPosition() = position
}