package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*

interface DynamicProductDetailAdapterFactory {
    fun type(data: ProductMostHelpfulReviewDataModel): Int
    fun type(data: ProductDiscussionMostHelpfulDataModel): Int
    fun type(data: ProductInfoDataModel): Int
    fun type(data: ProductGeneralInfoDataModel): Int
    fun type(data: ProductMerchantVoucherDataModel): Int
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
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}