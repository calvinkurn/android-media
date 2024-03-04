package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText

class MerchantVoucherCarouselItemViewModel(
    application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {
    private val _multiShopData = MutableLiveData<MerchantVoucherCarouselModel>()
    val multiShopModel: LiveData<MerchantVoucherCarouselModel> = _multiShopData


    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.data?.firstOrNull()?.let {
            _multiShopData.value = it.mapToAutomateCouponModelList()
        }
    }

    private fun DataItem.mapToAutomateCouponModelList(): MerchantVoucherCarouselModel {
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
                )
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
}
