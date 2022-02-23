package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MyCouponItemViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val componentData = MutableLiveData<MyCoupon>()

    fun getComponentData(): LiveData<MyCoupon> {
        componentData.value = components.myCouponList?.coupons?.firstOrNull()
        return componentData
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun setClick(context: Context) {
        navigate(context, getCouponAppLink() ?: "")
    }

    fun getCouponAppLink(): String? {
        if (components.data.isNullOrEmpty()) return ""

        return components.data?.get(0)?.applinks
    }

}