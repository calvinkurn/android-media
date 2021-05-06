package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.*
import com.tokopedia.variant_common.view.ProductVariantListener

class DynamicProductDetailAdapterFactoryImpl(private val listener: DynamicProductDetailListener,
                                             private val variantListener: ProductVariantListener)
    : BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory {

    override fun type(data: ProductRecommendationDataModel): Int {
        return ProductRecommendationViewHolder.LAYOUT
    }

    override fun type(data: ProductMerchantVoucherDataModel): Int {
        return ProductMerchantVoucherViewHolder.LAYOUT
    }

    override fun type(data: ProductGeneralInfoDataModel): Int {
        return ProductGeneralInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductMostHelpfulReviewDataModel): Int {
        return ProductReviewViewHolder.LAYOUT
    }

    override fun type(data: ProductDiscussionMostHelpfulDataModel): Int {
        return ProductDiscussionMostHelpfulViewHolder.LAYOUT
    }

    override fun type(data: ProductInfoDataModel): Int {
        return ProductInfoViewHolder.LAYOUT
    }

    override fun type(data: PageErrorDataModel): Int {
        return PageErrorViewHolder.LAYOUT
    }

    override fun type(data: VariantDataModel): Int {
        return ProductVariantViewHolder.LAYOUT
    }

    override fun type(data: ProductNotifyMeDataModel): Int {
        return ProductNotifyMeViewHolder.LAYOUT
    }

    override fun type(data: ProductMediaDataModel): Int {
        return ProductMediaViewHolder.LAYOUT
    }

    override fun type(data: ProductContentDataModel): Int {
        return ProductContentViewHolder.LAYOUT
    }

    override fun type(data: ProductMiniSocialProofDataModel): Int {
        return ProductMiniSocialProofViewHolder.LAYOUT
    }

    override fun type(data: ProductTickerInfoDataModel): Int {
        return ProductTickerInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductShopCredibilityDataModel): Int {
        return ProductShopCredibilityViewHolder.LAYOUT
    }

    override fun type(data: ProductCustomInfoDataModel): Int {
        return ProductCustomInfoViewHolder.LAYOUT
    }

    override fun type(data: TopAdsImageDataModel): Int {
        return ProductTopAdsImageViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoDataModel): Int {
        return ProductDetailInfoViewHolder.LAYOUT
    }

    override fun type(reportData: ProductReportDataModel): Int {
        return ProductReportViewHolder.LAYOUT
    }

    override fun type(data: ProductLoadingDataModel): Int {
        return ProductShimmeringViewHolder.LAYOUT
    }

    override fun type(data: ProductShipmentDataModel): Int {
        return ProductShipmentViewHolder.LAYOUT
    }

    override fun type(data: ProductMerchantVoucherSummaryDataModel): Int {
        return ProductMerchantVoucherSummaryViewHolder.LAYOUT
    }

    override fun type(data: PdpComparisonWidgetDataModel): Int {
        return PdpComparisonWidgetViewHolder.LAYOUT
    }

    override fun type(data: ProductRecomWidgetDataModel): Int {
        return ProductRecomWidgetViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(view, listener)
            ProductMerchantVoucherViewHolder.LAYOUT -> ProductMerchantVoucherViewHolder(view, listener)
            ProductInfoViewHolder.LAYOUT -> ProductInfoViewHolder(view, listener)
            ProductDiscussionMostHelpfulViewHolder.LAYOUT -> ProductDiscussionMostHelpfulViewHolder(view, listener)
            ProductGeneralInfoViewHolder.LAYOUT -> ProductGeneralInfoViewHolder(view, listener)
            ProductReviewViewHolder.LAYOUT -> ProductReviewViewHolder(view, listener)
            ProductShimmeringViewHolder.LAYOUT -> ProductShimmeringViewHolder(view)
            PageErrorViewHolder.LAYOUT -> PageErrorViewHolder(view, listener)
            ProductVariantViewHolder.LAYOUT -> ProductVariantViewHolder(view, variantListener, listener)
            ProductNotifyMeViewHolder.LAYOUT -> ProductNotifyMeViewHolder(view, listener)
            ProductMediaViewHolder.LAYOUT -> ProductMediaViewHolder(view, listener)
            ProductContentViewHolder.LAYOUT -> ProductContentViewHolder(view, listener)
            ProductMiniSocialProofViewHolder.LAYOUT -> ProductMiniSocialProofViewHolder(view, listener)
            ProductTickerInfoViewHolder.LAYOUT -> ProductTickerInfoViewHolder(view, listener)
            ProductShopCredibilityViewHolder.LAYOUT -> ProductShopCredibilityViewHolder(view, listener)
            ProductCustomInfoViewHolder.LAYOUT -> ProductCustomInfoViewHolder(view, listener)
            ProductTopAdsImageViewHolder.LAYOUT -> ProductTopAdsImageViewHolder(view, listener)
            ProductDetailInfoViewHolder.LAYOUT -> ProductDetailInfoViewHolder(view, listener)
            ProductReportViewHolder.LAYOUT -> ProductReportViewHolder(view, listener)
            ProductShipmentViewHolder.LAYOUT -> ProductShipmentViewHolder(view, listener)
            ProductMerchantVoucherSummaryViewHolder.LAYOUT -> ProductMerchantVoucherSummaryViewHolder(view)
            PdpComparisonWidgetViewHolder.LAYOUT -> PdpComparisonWidgetViewHolder(view, listener)
            ProductRecomWidgetViewHolder.LAYOUT -> ProductRecomWidgetViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }

}