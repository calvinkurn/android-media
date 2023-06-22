package com.tokopedia.shop.home.view.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.common.data.model.DynamicRule
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopHomeNewProductLaunchCampaignUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: BaseShopHomeWidgetUiModel.Header = BaseShopHomeWidgetUiModel.Header(),
    override val isFestivity: Boolean = false,
    val data: List<NewProductLaunchCampaignItem>? = null
) : BaseShopHomeWidgetUiModel() {

    val impressHolder = ImpressHolder()

    data class NewProductLaunchCampaignItem(
        val campaignId: String = "",
        val name: String = "",
        val description: String = "",
        val startDate: String = "",
        val endDate: String = "",
        val statusCampaign: String = "",
        val timeDescription: String = "",
        val timeCounter: String = "",
        var totalNotify: Int = 0,
        val totalNotifyWording: String = "",
        val voucherWording: String = "",
        val dynamicRule: DynamicRule = DynamicRule(),
        val bannerList: List<BannerItem> = listOf(),
        val productList: List<ShopHomeProductUiModel> = listOf(),
        var isRemindMe: Boolean? = null,
        var rvState: Parcelable? = null,
        var showRemindMeLoading : Boolean = false,
        var isHideRemindMeTextAfterXSeconds: Boolean = false
    ) {

        data class BannerItem(
            val imageId: String = "",
            val imageUrl: String = "",
            val bannerType: String = "",
            val device: String = ""
        ) : ImpressHolder()

    }

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}
