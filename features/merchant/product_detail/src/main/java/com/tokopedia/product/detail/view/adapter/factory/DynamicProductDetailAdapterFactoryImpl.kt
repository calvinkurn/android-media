package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.*
import com.tokopedia.variant_common.view.ProductVariantListener

class DynamicProductDetailAdapterFactoryImpl(private val listener: DynamicProductDetailListener,
                                             private val variantListener: ProductVariantListener)
    : BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory {
    override fun type(data: ProductLastSeenDataModel): Int {
        return ProductLastSeenViewHolder.LAYOUT
    }

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

    override fun type(data: ProductDiscussionDataModel): Int {
        return ProductDiscussionViewHolder.LAYOUT
    }

    override fun type(data: ProductDiscussionMostHelpfulDataModel): Int {
        return ProductDiscussionMostHelpfulViewHolder.LAYOUT
    }

    override fun type(data: ProductInfoDataModel): Int {
        return ProductInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductShopInfoDataModel): Int {
        return ProductShopInfoViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return ProductShimmeringViewHolder.LAYOUT
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

    override fun type(data: ProductMiniShopInfoDataModel): Int {
        return ProductMiniShopInfoViewHolder.LAYOUT
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

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductLastSeenViewHolder.LAYOUT -> ProductLastSeenViewHolder(view)
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(view, listener)
            ProductMerchantVoucherViewHolder.LAYOUT -> ProductMerchantVoucherViewHolder(view, listener)
            ProductShopInfoViewHolder.LAYOUT -> ProductShopInfoViewHolder(view, listener)
            ProductInfoViewHolder.LAYOUT -> ProductInfoViewHolder(view, listener)
            ProductDiscussionViewHolder.LAYOUT -> ProductDiscussionViewHolder(view, listener)
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
            ProductMiniShopInfoViewHolder.LAYOUT -> ProductMiniShopInfoViewHolder(view, listener)
            ProductTickerInfoViewHolder.LAYOUT -> ProductTickerInfoViewHolder(view, listener)
            ProductShopCredibilityViewHolder.LAYOUT -> ProductShopCredibilityViewHolder(view, listener)
            ProductCustomInfoViewHolder.LAYOUT -> ProductCustomInfoViewHolder(view, listener)
            ProductTopAdsImageViewHolder.LAYOUT -> ProductTopAdsImageViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }

}