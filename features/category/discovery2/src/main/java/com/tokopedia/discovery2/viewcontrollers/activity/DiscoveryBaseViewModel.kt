package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.applink.RouteManager


abstract class DiscoveryBaseViewModel() {
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

    fun onDetachToViewHolder() {

    }

    open fun loggedInCallback() {

    }

    open fun isPhoneVerificationSuccess(phoneVerifyStatus: Boolean) {

    }
}