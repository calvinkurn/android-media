package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCategoryCarouselDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoDataModel
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
import com.tokopedia.product.detail.data.model.datamodel.ProductReportDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel

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
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
