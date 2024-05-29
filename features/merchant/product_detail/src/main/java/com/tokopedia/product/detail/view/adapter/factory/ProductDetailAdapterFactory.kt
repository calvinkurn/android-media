package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.component.shipment.ShipmentUiModel
import com.tokopedia.product.detail.data.model.datamodel.ArButtonDataModel
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetV2DataModel
import com.tokopedia.product.detail.data.model.datamodel.GlobalBundlingDataModel
import com.tokopedia.product.detail.data.model.datamodel.LoadingDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpRecommendationWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCategoryCarouselDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoTitleDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLoadingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherSummaryDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniShopWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofStockDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewUiModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductReportDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopAdditionalDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTabletLeftSectionDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTabletRightSectionDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.SDUIDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.data.model.datamodel.ViewToViewWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewDataModel
import com.tokopedia.product.detail.view.viewholder.a_plus_content.APlusImageUiModel
import com.tokopedia.product.detail.view.viewholder.bmgm.BMGMUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.OngoingCampaignUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.ProductNotifyMeUiModel
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.DynamicOneLinerUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.GWPUiModel
import com.tokopedia.product.detail.view.viewholder.promo_price.ui.ProductPriceUiModel

interface ProductDetailAdapterFactory {
    fun type(data: ProductMostHelpfulReviewUiModel): Int
    fun type(data: ProductDiscussionMostHelpfulDataModel): Int
    fun type(data: ProductGeneralInfoDataModel): Int
    fun type(data: ProductRecommendationDataModel): Int
    fun type(data: PageErrorDataModel): Int
    fun type(data: ProductNotifyMeUiModel): Int
    fun type(data: ProductMediaDataModel): Int
    fun type(data: ProductContentDataModel): Int
    fun type(data: ProductMiniSocialProofDataModel): Int
    fun type(data: ProductMiniSocialProofStockDataModel): Int
    fun type(data: ProductTickerInfoDataModel): Int
    fun type(data: ProductShopCredibilityDataModel): Int
    fun type(data: ProductCustomInfoDataModel): Int
    fun type(data: TopAdsImageDataModel): Int
    fun type(reportData: ProductReportDataModel): Int
    fun type(data: ProductDetailInfoDataModel): Int
    fun type(data: ProductLoadingDataModel): Int
    fun type(data: ProductShipmentDataModel): Int
    fun type(data: ProductMerchantVoucherSummaryDataModel): Int
    fun type(data: PdpComparisonWidgetDataModel): Int
    fun type(data: ProductSingleVariantDataModel): Int
    fun type(data: ProductMiniShopWidgetDataModel): Int
    fun type(data: ProductRecomWidgetDataModel): Int
    fun type(data: OneLinersDataModel): Int
    fun type(data: ProductCategoryCarouselDataModel): Int
    fun type(data: TopadsHeadlineUiModel): Int
    fun type(data: ContentWidgetDataModel): Int
    fun type(data: FintechWidgetDataModel): Int
    fun type(data: FintechWidgetV2DataModel): Int
    fun type(data: LoadingDataModel): Int
    fun type(data: GlobalBundlingDataModel): Int
    fun type(data: ProductShopAdditionalDataModel): Int
    fun type(data: ArButtonDataModel): Int
    fun type(data: ViewToViewWidgetDataModel): Int
    fun type(data: ProductCustomInfoTitleDataModel): Int
    fun type(data: ProductShopReviewDataModel): Int
    fun type(data: OngoingCampaignUiModel): Int
    fun type(data: DynamicOneLinerUiModel): Int
    fun type(data: APlusImageUiModel): Int
    fun type(data: BMGMUiModel): Int
    fun type(data: ShipmentUiModel): Int
    fun type(data: ProductPriceUiModel): Int
    fun type(data: ProductTabletLeftSectionDataModel): Int
    fun type(data: ProductTabletRightSectionDataModel): Int
    fun type(data: GWPUiModel): Int
    fun type(data: SDUIDataModel): Int

    // This is the new centralized recom widget model.
    // After migration, all recom widgets will only be mapped into this model
    fun type(data: PdpRecommendationWidgetDataModel): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
