package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import androidx.lifecycle.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.ComponentsItem


abstract class DiscoveryBaseViewModel(val components: ComponentsItem) : LifecycleObserver {

    // this is just to alias to components
    val component: ComponentsItem = components

    // hacky way to store binding adapter position in onDetachedView
    var detachedBindingAdapterPosition = 0

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

    open fun refreshProductCarouselError() {

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
