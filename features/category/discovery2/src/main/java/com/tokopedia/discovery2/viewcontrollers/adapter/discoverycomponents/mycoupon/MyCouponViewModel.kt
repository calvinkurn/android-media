package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest
import com.tokopedia.discovery2.usecase.MyCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val SOURCE = "discovery"

class MyCouponViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentList = MutableLiveData<ArrayList<ComponentsItem>>()

    @Inject
    lateinit var myCouponUseCase: MyCouponUseCase



    fun getComponentList(): LiveData<ArrayList<ComponentsItem>> {
        return componentList
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        getClickCouponData()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()



    fun getClickCouponData() {
        val dataItem = components.data?.firstOrNull()
        dataItem?.let { couponDataItem ->
            if (!couponDataItem.catalogSlug.isNullOrEmpty()) {
                launchCatchError(block = {
                    val myCouponResponse = myCouponUseCase.getMyCouponData(getMyCoupleBundle(couponDataItem))
                    myCouponResponse.tokopointsCouponListStack?.let { myCouponResponse ->
                        if (!myCouponResponse.coupons.isNullOrEmpty()) {
                            components.name = "my_coupon_item"
                            components.myCouponList?.coupons = myCouponResponse.coupons
                        } else {
//                        showNotifyToast.value = Triple(true, campaignResponse.errorMessage, 0)
                        }
                    }
                }, onError = {
                    it.printStackTrace()
                })
            }
        }
    }

    private fun getMyCoupleBundle(dataItem: DataItem): MyCouponsRequest {
        val myCouponsRequest = MyCouponsRequest()
        myCouponsRequest.serviceID = ""
        myCouponsRequest.categoryID = 0
        myCouponsRequest.categoryIDCoupon = -1
        myCouponsRequest.page = 1
        myCouponsRequest.limit = 10
        myCouponsRequest.includeExtraInfo = 1
        myCouponsRequest.apiVersion = "2.0.0"
        myCouponsRequest.isGetPromoInfo = true
        myCouponsRequest.clientID = "disco"
        myCouponsRequest.catalogSlugs = dataItem.catalogSlug
        myCouponsRequest.source = SOURCE
        return myCouponsRequest
    }

}