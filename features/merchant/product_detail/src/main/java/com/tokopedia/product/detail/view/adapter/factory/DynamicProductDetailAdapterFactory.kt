package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ArButtonDataModel
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.GlobalBundlingDataModel
import com.tokopedia.product.detail.data.model.datamodel.LoadingDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
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
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalPlaceholderDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductReportDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopAdditionalDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ViewToViewWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductReviewListDataModel

interface DynamicProductDetailAdapterFactory {
    fun type(data: ProductMostHelpfulReviewDataModel): Int
    fun type(data: ProductDiscussionMostHelpfulDataModel): Int
    fun type(data: ProductGeneralInfoDataModel): Int
    fun type(data: ProductRecommendationDataModel): Int
    fun type(data: PageErrorDataModel): Int
    fun type(data: VariantDataModel): Int
    fun type(data: ProductNotifyMeDataModel): Int
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
    fun type(data: ProductBundlingDataModel): Int
    fun type(data: ContentWidgetDataModel): Int
    fun type(data: FintechWidgetDataModel):Int
    fun type(data: ProductRecommendationVerticalDataModel): Int
    fun type(data: ProductRecommendationVerticalPlaceholderDataModel): Int
    fun type(data: LoadingDataModel): Int
    fun type(data: GlobalBundlingDataModel): Int
    fun type(data: ProductShopAdditionalDataModel): Int
    fun type(data: ArButtonDataModel): Int
    fun type(data: ViewToViewWidgetDataModel): Int
    fun type(data: ProductCustomInfoTitleDataModel): Int
    fun type(data: ProductReviewListDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
