package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest
import com.tokopedia.discovery2.usecase.HideSectionUseCase
import com.tokopedia.discovery2.usecase.MyCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val SOURCE = "discovery-page"
private const val SERVICE_ID = ""
private const val CATEGORY_ID = 0
private const val CATEGORY_ID_COUPON = -1
private const val PAGE = 1
private const val LIMIT = 20
private const val INCLUDE_EXTRA_INFO = 1
private const val API_VERSION = "2.0.0"
private const val IS_GET_PROMO_INFO = true
private const val CLIENT_ID = "disco"

class MyCouponViewModel(application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentList = MutableLiveData<ArrayList<ComponentsItem>>()
    private val _hideSection = SingleLiveEvent<String>()
    val hideSectionLD: LiveData<String> = _hideSection

    @JvmField
    @Inject
    var myCouponUseCase: MyCouponUseCase? = null

    @JvmField
    @Inject
    var hideSectionUseCase: HideSectionUseCase? = null

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
        val dataItem = components.data?.firstOrNull()
        dataItem?.let { couponDataItem ->
            launchCatchError(block = {
                myCouponUseCase?.getMyCouponData(components.id, components.pageEndPoint, getMyCoupleBundle(couponDataItem))
                setCouponsList()
            }, onError = {
                    components.noOfPagesLoaded = 1
                    hideIfPresentInSection()
                })
        }
    }

    private fun getCouponsList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { couponList ->
            return couponList as ArrayList<ComponentsItem>
        }
        return null
    }

    private fun hideIfPresentInSection() {
        val response = hideSectionUseCase?.checkForHideSectionHandling(components)
        if (response?.shouldHideSection == true) {
            if (response.sectionId.isNotEmpty()) {
                _hideSection.value = response.sectionId
            }
            syncData.value = true
        }
    }

    private fun setCouponsList() {
        getCouponsList()?.let {
            if (it.isNotEmpty()) {
                componentList.value = it
            } else {
                componentList.value = null
                hideIfPresentInSection()
            }
        }
    }

    private fun getMyCoupleBundle(dataItem: DataItem): MyCouponsRequest {
        return MyCouponsRequest(
            serviceID = SERVICE_ID,
            categoryID = CATEGORY_ID,
            categoryIDCoupon = CATEGORY_ID_COUPON,
            page = PAGE,
            limit = dataItem.limit ?: LIMIT,
            includeExtraInfo = INCLUDE_EXTRA_INFO,
            apiVersion = API_VERSION,
            isGetPromoInfo = IS_GET_PROMO_INFO,
            clientID = CLIENT_ID,
            catalogSlugs = dataItem.catalogSlug ?: listOf(),
            source = SOURCE
        )
    }
}
