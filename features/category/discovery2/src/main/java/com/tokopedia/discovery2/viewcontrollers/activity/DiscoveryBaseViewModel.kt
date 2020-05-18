package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import android.view.View
import com.tokopedia.applink.RouteManager


abstract class DiscoveryBaseViewModel(){
    abstract fun initDaggerInject()
    fun onCleared() {
    }

    fun navigate(context: Context?,  applink : String?){
        if (!applink.isNullOrEmpty() && context != null) {
            RouteManager.route(context, applink)
        }
    }
}