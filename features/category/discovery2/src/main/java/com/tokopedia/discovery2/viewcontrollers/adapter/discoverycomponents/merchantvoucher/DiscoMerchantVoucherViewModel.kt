package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherCarouselModel
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import com.tokopedia.discovery_component.widgets.automatecoupon.TimeLimit
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiscoMerchantVoucherViewModel(
    application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components), CoroutineScope {
    @JvmField
    @Inject
    var merchantVoucherUseCase: MerchantVoucherUseCase? = null

    @JvmField
    @Inject
    var topAdsTrackingUseCase: TopAdsTrackingUseCase? = null

    private val _coupon: MutableLiveData<MerchantVoucherCarouselModel> = MutableLiveData()
    val coupon: LiveData<MerchantVoucherCarouselModel> = _coupon
    private val _loadError: MutableLiveData<Boolean> = MutableLiveData()
    val loadError: LiveData<Boolean> = _loadError

    fun fetchDataForCoupons() {
        launchCatchError(block = {
            merchantVoucherUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint)
            setVoucherList()
        }, onError = {
            _loadError.value = true
        })
    }

    private fun ComponentsItem.mapToAutomateCouponModelList(): MerchantVoucherCarouselModel {
        val component = this.data?.firstOrNull()
        val timeLimitObj = component?.timeLimit
        return MerchantVoucherCarouselModel(
            appLink = component?.shopInfo?.appLink.orEmpty(),
            buttonText = component?.buttonText.orEmpty(),
            url = component?.shopInfo?.url.orEmpty(),
            automateCouponModel = AutomateCouponModel.List(
                backgroundUrl = component?.backgroundImageUrl.orEmpty(),
                benefit = DynamicColorText(
                    value = "${component?.subtitle}"
                ),
                tnc = DynamicColorText(
                    value = component?.subtitle_1.orEmpty(),
                ),
                iconUrl = component?.shopInfo?.iconUrl.orEmpty(),
                shopName = DynamicColorText(
                    value = component?.shopInfo?.name.orEmpty()
                ),
                type = DynamicColorText(
                    value = component?.title.orEmpty(),
                    colorHex = component?.fontColor.orEmpty(),
                ),
                timeLimit = if (timeLimitObj != null) {
                    if (timeLimitObj.canShowTimer()) {
                        TimeLimit.Timer(
                            prefix = DynamicColorText(timeLimitObj.title),
                            endDate = timeLimitObj.timestamp.epochToDate()
                        )
                    } else {
                        TimeLimit.Text(
                            prefix = DynamicColorText(timeLimitObj.title),
                            endText = null
                        )
                    }
                } else {
                    null
                },
                badgeText = if (component?.badge.isNullOrEmpty()) null else component?.badge,
                isTopads = component?.isTopads ?: false,
                topAdsClickUrl = component?.topadsClickUrl
            )
        )
    }

    private fun Long.epochToDate(): Date {
        return Date(this * 1_000)
    }

    private fun setVoucherList() {
        getVoucher().run {
            if (this != null) {
                _loadError.value = false
                _coupon.value = this.mapToAutomateCouponModelList()
                syncData.value = true
            } else {
                _loadError.value = true
            }
        }
    }

    private fun getVoucher(): ComponentsItem? {
        components.getComponentsItem()?.let { product ->
            return product.firstOrNull()
        }
        return null
    }

    fun getShopID(): String {
        return components.data?.firstOrNull()?.shopIds?.firstOrNull()?.toString() ?: ""
    }

    fun getProductId(): String {
        return components.data?.firstOrNull()?.productId ?: ""
    }

    fun trackTopAdsClick() {
        val model = _coupon.value?.automateCouponModel ?: return
        if (model.isTopads) {
            val topAdsClickUrl = model.topAdsClickUrl
            if (!topAdsClickUrl.isNullOrEmpty()) {
                topAdsTrackingUseCase?.hitClick(
                    this@DiscoMerchantVoucherViewModel::class.qualifiedName,
                    topAdsClickUrl,
                    "",
                    "",
                    ""
                )
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()
}
