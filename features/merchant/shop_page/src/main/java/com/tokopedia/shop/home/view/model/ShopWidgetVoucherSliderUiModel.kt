package com.tokopedia.shop.home.view.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.common.data.model.MerchantVoucherCouponUiModel
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopWidgetVoucherSliderUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val listCategorySlug: List<String> = listOf(),
    val listVoucher: List<ExclusiveLaunchVoucher> = listOf(),
    val isError: Boolean = false,
    var rvState: Parcelable? = null
) : BaseShopHomeWidgetUiModel() {

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopCampaignTabAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}
