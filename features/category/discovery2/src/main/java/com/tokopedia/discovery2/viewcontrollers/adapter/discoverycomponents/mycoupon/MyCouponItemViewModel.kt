package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.user.session.UserSession

class MyCouponItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(){
    private val componentData = MutableLiveData<ComponentsItem>()

    init {
        componentData.value = components
    }

    fun getComponentData(): LiveData<ComponentsItem> = componentData

    fun setClick(context: Context) {
        navigate(context, getCouponAppLink())
    }

    private fun getCouponAppLink(): String {
        val appLink = components.myCouponList?.firstOrNull()?.redirectAppLink
        if (appLink.isNullOrEmpty()) return ""

        return appLink
    }

    fun getUserId(): String {
        return UserSession(application).userId
    }

    fun getCouponItem(): MyCoupon? {
        return components.myCouponList?.firstOrNull()
    }

}