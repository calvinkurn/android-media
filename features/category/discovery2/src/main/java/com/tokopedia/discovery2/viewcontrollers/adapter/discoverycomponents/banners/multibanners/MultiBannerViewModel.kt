package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.BannerAction
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.SubScribeToUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.Toaster
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
        initDaggerInject()
    }


    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getComponentData(): LiveData<ComponentsItem> = bannerData
    fun getPushBannerStatusData(): LiveData<Int> = pushBannerStatus
    fun getShowLoginData(): LiveData<Boolean> = showLogin
    fun getPushBannerSubscriptionData(): LiveData<Int> = pushBannerSubscription
    fun getBannerUrlHeight() = Utils.extractDimension(bannerData.value?.data?.firstOrNull()?.imageUrlDynamicMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(bannerData.value?.data?.firstOrNull()?.imageUrlDynamicMobile, "width")

    fun onBannerClicked(position: Int, view: View) {
        when (bannerData.value?.data?.get(position)?.action) {
            BannerAction.APPLINK.name -> navigation(position, view.context)
            BannerAction.CODE.name -> copyCodeToClipboard(position, view)
            BannerAction.PUSH_NOTIFIER.name -> subscribeUserForPushNotification(position)
            else -> navigation(position, view.context)
        }
    }

    private fun copyCodeToClipboard(position: Int, view: View) {
        (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)
                ?.setPrimaryClip(ClipData.newPlainText(PROMO_CODE, bannerData.value?.data?.get(position)?.code))
        if (bannerData.value?.data?.get(position)?.applinks?.isNotEmpty() == true) {
            Toaster.make(view, view.context.getString(R.string.coupon_code_successfully_copied), Toast.LENGTH_SHORT, Toaster.TYPE_NORMAL,
                    view.context.getString(R.string.coupon_code_btn_text), View.OnClickListener {
                navigate(view.context, bannerData.value?.data?.get(position)?.applinks)
            })
        } else {
            Toaster.make(view, view.context.getString(R.string.coupon_code_successfully_copied), Toast.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        }
    }

    private fun navigation(position: Int, context: Context) {
        if (!bannerData.value?.data?.get(position)?.applinks.isNullOrEmpty()) {
            navigate(context, bannerData.value?.data?.get(position)?.applinks)
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
        when (bannerData.value?.data?.get(position)?.action) {
            BannerAction.PUSH_NOTIFIER.name -> checkUserPushStatus(position)
            BannerAction.LOCAL_CALENDAR.name -> checkUserLocalPushStatus(position)
        }
    }


    private fun checkUserPushStatus(position: Int) {
        launchCatchError(block = {
            val pushSubscriptionResponse = checkPushStatusUseCase.checkPushStatus(getCampaignId(position))
            if (pushSubscriptionResponse.notifierCheckReminder?.status == 1) {
                pushBannerSubscription.value = position
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun getCampaignId(position: Int): Int {
        val parameterList: List<String>? = bannerData.value?.data?.get(position)?.paramsMobile?.split("=")
        if (parameterList != null && parameterList.size >= 2) {
            return parameterList[1].toIntOrZero()
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