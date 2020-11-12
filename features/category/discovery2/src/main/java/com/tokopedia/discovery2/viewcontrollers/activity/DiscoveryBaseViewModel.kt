package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import androidx.lifecycle.*
import com.tokopedia.applink.RouteManager


abstract class DiscoveryBaseViewModel : LifecycleObserver {
    open fun initDaggerInject() {}
    val syncData: MutableLiveData<Boolean> = MutableLiveData()
    fun getSyncPageLiveData(): LiveData<Boolean> {
        return syncData
    }

    fun onCleared() {
    }

    open fun onAttachToViewHolder() {

    }

    fun navigate(context: Context?, applink: String?) {
        if (!applink.isNullOrEmpty() && context != null) {
            RouteManager.route(context, applink)
        }
    }

    open fun onDetachToViewHolder() {

    }

    open fun loggedInCallback() {

    }

    open fun isPhoneVerificationSuccess(phoneVerifyStatus: Boolean) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {

    }
}