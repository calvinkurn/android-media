package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class MyCouponItemViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(){
    private val componentData = MutableLiveData<ComponentsItem>()

    init {
        componentData.value = components
    }

    fun getComponentData(): LiveData<ComponentsItem> = componentData

//    fun setClick(context: Context) {
//        navigate(context, getCouponAppLink() ?: "")
//    }

//    fun getCouponAppLink(): String? {
//        if (components.data.isNullOrEmpty()) return ""
//
//        return components.data?.get(0)?.applinks
//    }

    fun getCouponItem(): MyCoupon? {
        return components.myCouponList?.firstOrNull()
    }

}