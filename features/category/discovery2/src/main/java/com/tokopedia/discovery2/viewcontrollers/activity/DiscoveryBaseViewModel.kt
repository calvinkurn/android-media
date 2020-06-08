package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel


abstract class DiscoveryBaseViewModel(){
    abstract fun initDaggerInject()
    val syncData: MutableLiveData<Boolean> = MutableLiveData()
    fun getSyncPageLiveData(): LiveData<Boolean> {
        return syncData
    }
    fun onCleared() {
    }

    open fun onAttachToViewHolder() {

    }
    fun navigate(context: Context?,  applink : String?){
        if (!applink.isNullOrEmpty() && context != null) {
            RouteManager.route(context, applink)
        }
    }

    fun onDetachToViewHolder() {

    }
}