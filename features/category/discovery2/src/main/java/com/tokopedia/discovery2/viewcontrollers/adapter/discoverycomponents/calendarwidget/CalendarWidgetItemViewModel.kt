package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.SubScribeToUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CalendarWidgetItemViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components), CoroutineScope {
    private val pushBannerSubscription: MutableLiveData<Boolean> = MutableLiveData()
    private val pushBannerStatus: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    private val showErrorToast: MutableLiveData<String> = MutableLiveData()

    fun getShowErrorToastData(): LiveData<String> = showErrorToast
    fun getPushBannerSubscriptionData(): LiveData<Boolean> = pushBannerSubscription
    fun getPushBannerStatusData(): LiveData<Pair<Boolean, String>> = pushBannerStatus

    @JvmField
    @Inject
    var checkPushStatusUseCase: CheckPushStatusUseCase? = null

    @JvmField
    @Inject
    var subScribeToUseCase: SubScribeToUseCase? = null

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

    fun getUserId(): String {
        return UserSession(application).userId
    }

    fun subscribeUserForPushNotification(campaignId: String) {
        launchCatchError(block = {
            val pushSubscriptionResponse =
                subScribeToUseCase?.subscribeToPush(campaignId.toLongOrZero())
            if (pushSubscriptionResponse?.notifierSetReminder?.isSuccess == 1 || pushSubscriptionResponse?.notifierSetReminder?.isSuccess == 2) {
                pushBannerStatus.value =
                    Pair(true, application.getString(R.string.discovery_calendar_push_subscribe))
            }
        }, onError = {
            it.printStackTrace()
            showErrorToast.value = application.getString(R.string.discovery_calendar_push_error)
        })
    }

    fun unSubscribeUserForPushNotification(campaignId: String) {
        launchCatchError(block = {
            val pushSubscriptionResponse =
                subScribeToUseCase?.unSubscribeToPush(campaignId.toLongOrZero())
            if (pushSubscriptionResponse?.notifierSetReminder?.isSuccess == 1 || pushSubscriptionResponse?.notifierSetReminder?.isSuccess == 2) {
                pushBannerStatus.value =
                    Pair(false, application.getString(R.string.discovery_calendar_push_unsubscribe))
            }
        }, onError = {
            it.printStackTrace()
            showErrorToast.value = application.getString(R.string.discovery_calendar_push_error)
        })
    }

    fun checkUserPushStatus(campaignId: String) {
        launchCatchError(block = {
            val pushSubscriptionResponse =
                checkPushStatusUseCase?.checkPushStatus(campaignId.toLongOrZero())
            pushBannerSubscription.value =
                pushSubscriptionResponse?.notifierCheckReminder?.status == 1
        }, onError = {
            it.printStackTrace()
        })
    }

    override fun loggedInCallback() {
        this.syncData.value = true
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getCtaText(defaultString: String): String {
        val ctaFromBE = components.data?.firstOrNull()?.ctaText
        return if (ctaFromBE.isNullOrEmpty()) {
            defaultString
        } else {
            ctaFromBE
        }
    }

    fun getNotifiedCtaText(defaultString: String): String {
        val ctaFromBE = components.data?.firstOrNull()?.notifiedCtaText
        return if (ctaFromBE.isNullOrEmpty()) {
            defaultString
        } else {
            ctaFromBE
        }
    }
}
