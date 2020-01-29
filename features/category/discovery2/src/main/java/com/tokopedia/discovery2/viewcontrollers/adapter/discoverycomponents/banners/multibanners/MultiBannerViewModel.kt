package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.categoryrevamp.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.data.BannerAction
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.SubScribeToUseCase
import com.tokopedia.discovery2.utils.Utils
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MultiBannerViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {
    private val bannerData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val pushBannerStatus: MutableLiveData<Int> = MutableLiveData()
    private val pushBannerSubscription: MutableLiveData<Int> = MutableLiveData()
    private val showLogin: MutableLiveData<Boolean> = MutableLiveData()
    @Inject lateinit var checkPushStatusUseCase:CheckPushStatusUseCase
    @Inject lateinit var subScribeToUseCase: SubScribeToUseCase

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
    fun getComponentData() = bannerData
    fun getPushBannerStatusData() = pushBannerStatus
    fun getshowLoginData() = showLogin
    fun getPushBannerSubscriptionData() = pushBannerSubscription
    fun getBannerUrlHeight() = Utils.extractDimension(bannerData.value?.data?.get(0)?.imageUrlDynamicMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(bannerData.value?.data?.get(0)?.imageUrlDynamicMobile, "width")

    fun onBannerClicked(position: Int) {
        when (bannerData.value?.data?.get(position)?.action) {
            BannerAction.APPLINK.name -> navigation(position)
            BannerAction.CODE.name -> copyCodeToClipboard(position)
            BannerAction.PUSH_NOTIFIER.name -> subscribeUserForPushNotification(position)
        }
    }

    private fun copyCodeToClipboard(position: Int) {
        (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)
                ?.primaryClip = ClipData.newPlainText("Promo Code", bannerData.value?.data?.get(position)?.applinks)
        //TODO Use unfiy Toasted instead of Toast
        Toast.makeText(application, "Code copied", Toast.LENGTH_SHORT).show()
    }

    private fun navigation(position: Int) {
        RouteManager.route(application, bannerData.value?.data?.get(position)?.applinks)
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

     fun isUserLoggedIn():Boolean{
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
            return parameterList[1].toInt()
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
}