package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import com.tokopedia.discovery_component.widgets.automatecoupon.TimeLimit
import java.util.*
import javax.inject.Inject

class MerchantVoucherCarouselItemViewModel(
    application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components) {
    private val _multiShopData = MutableLiveData<MerchantVoucherCarouselModel>()
    val multiShopModel: LiveData<MerchantVoucherCarouselModel> = _multiShopData

    @JvmField
    @Inject
    var topAdsTrackingUseCase: TopAdsTrackingUseCase? = null

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.data?.firstOrNull()?.let {
            _multiShopData.value = it.mapToAutomateCouponModelList()
        }
    }

    private fun DataItem.mapToAutomateCouponModelList(): MerchantVoucherCarouselModel {
        val timeLimitObj = this.timeLimit
        return MerchantVoucherCarouselModel(
            buttonText = this.buttonText.orEmpty(),
            url = shopInfo?.url.orEmpty(),
            appLink = shopInfo?.appLink.orEmpty(),
            automateCouponModel = AutomateCouponModel.List(
                backgroundUrl = backgroundImageUrl.orEmpty(),
                benefit = DynamicColorText(
                    value = "$subtitle"
                ),
                tnc = DynamicColorText(
                    value = subtitle_1.orEmpty(),
                ),
                iconUrl = shopInfo?.iconUrl.orEmpty(),
                shopName = DynamicColorText(
                    value = shopInfo?.name.orEmpty()
                ),
                type = DynamicColorText(
                    value = title.orEmpty(),
                    colorHex = fontColor.orEmpty(),
                ),
                timeLimit = if (timeLimitObj != null) {
                    if (timeLimitObj.canShowTimer()) {
                        TimeLimit.Timer(
                            prefix = DynamicColorText(timeLimitObj.title),
                            endDate = Date(timeLimitObj.timestamp * 1000L)
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
                badgeText = badge.ifEmpty { null },
                isTopads = isTopads ?: false,
                topAdsClickUrl = topadsClickUrl
            )
        )
    }

    fun syncParentPosition() {
        if (components.parentComponentId.isNotEmpty()) {
            getComponent(components.parentComponentId, components.pageEndPoint)?.let {
                components.parentComponentPosition = it.position
            }
        }
    }

    fun trackTopAdsClick() {
        val model = _multiShopData.value?.automateCouponModel ?: return
        if (model.isTopads) {
            val topAdsClickUrl = model.topAdsClickUrl
            if (!topAdsClickUrl.isNullOrEmpty()) {
                topAdsTrackingUseCase?.hitClick(
                    this@MerchantVoucherCarouselItemViewModel::class.qualifiedName,
                    topAdsClickUrl,
                    "",
                    "",
                    ""
                )
            }
        }
    }
}
