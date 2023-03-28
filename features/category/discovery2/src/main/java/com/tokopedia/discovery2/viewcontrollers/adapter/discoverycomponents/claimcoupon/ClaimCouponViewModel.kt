package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponRequest
import com.tokopedia.discovery2.usecase.ClaimCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ClaimCouponViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentList = MutableLiveData<ArrayList<ComponentsItem>>()

    @Inject
    lateinit var claimCouponUseCase: ClaimCouponUseCase



    fun getComponentList(): LiveData<ArrayList<ComponentsItem>> {
        return componentList
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        getClickCouponData()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()



    private fun getClickCouponData() {
        launchCatchError(block = {
            claimCouponUseCase.getClickCouponData(components.id, components.pageEndPoint,getClaimCoupleBundle(components.properties))
            componentList.postValue(components.getComponentsItem() as ArrayList<ComponentsItem>)
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun getClaimCoupleBundle(properties: Properties?): ClaimCouponRequest {
        return ClaimCouponRequest(
            catalogSlugs = properties?.catalogSlug?.split(",") ?: listOf(),
            categorySlug = properties?.categorySlug ?: ""
        )
    }

}
