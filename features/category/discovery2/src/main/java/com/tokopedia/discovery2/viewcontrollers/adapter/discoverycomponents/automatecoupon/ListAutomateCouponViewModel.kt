package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.automatecoupon.GetAutomateCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.notifications.common.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ListAutomateCouponViewModel(
    application: Application,
    component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(component), CoroutineScope {
    private val componentList = MutableLiveData<ArrayList<ComponentsItem>>()

    @JvmField
    @Inject
    var useCase: GetAutomateCouponUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getComponentList(): LiveData<ArrayList<ComponentsItem>> {
        return componentList
    }

    fun fetch(isDarkMode: Boolean) {
        launchCatchError(block = {
            useCase?.execute(component.id, component.pageEndPoint, isDarkMode)
            if (component.getComponentsItem()?.isNotEmpty() == true) {
                val coupons = component.getComponentsItem()?.filter {
                    it.automateCoupons?.isNotEmpty() == true
                }

                componentList.postValue(coupons as ArrayList<ComponentsItem>)
            }
        }, onError = {
                Timber.e(it)
            })
    }
}
