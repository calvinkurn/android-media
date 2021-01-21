package com.tokopedia.shop.home.view.model

import android.os.Parcelable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

data class ShopHomeNewProductLaunchCampaignUiModel(
        override val widgetId: String = "",
        override val layoutOrder: Int = -1,
        override val name: String = "",
        override val type: String = "",
        override val header: BaseShopHomeWidgetUiModel.Header = BaseShopHomeWidgetUiModel.Header(),
        val data: List<NewProductLaunchCampaignItem>? = null
) : BaseShopHomeWidgetUiModel, ImpressHolder() {

    companion object{
        const val TOTAL_NOTIFY_WORDING_FORMAT_FOR_REPLACED = "{{number_of_users}}"
    }

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
            val dynamicRule: DynamicRule = DynamicRule(),
            val bannerList: List<BannerItem> = listOf(),
            val productList: List<ShopHomeProductUiModel> = listOf(),
            var isRemindMe: Boolean? = null,
            var rvState: Parcelable? = null,
            var showRemindMeLoading : Boolean = false,
            var isHideRemindMeTextAfterXSeconds: Boolean = false
    ) {

        data class BannerItem(
                val imageId: Int = -1,
                val imageUrl: String = "",
                val bannerType: String = "",
                val device: String = ""
        ) : ImpressHolder()

        data class DynamicRule(
                val descriptionHeader: String = "",
                val dynamicRoleData: DynamicRoleData = DynamicRoleData()
        ) {
            data class DynamicRoleData(
                    val ruleID: String = ""
            )
        }
    }

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}