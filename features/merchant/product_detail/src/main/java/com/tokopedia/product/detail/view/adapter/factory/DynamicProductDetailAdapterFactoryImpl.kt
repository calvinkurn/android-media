package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.*

class DynamicProductDetailAdapterFactoryImpl(private val listener: DynamicProductDetailListener) : BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory {
    override fun type(data: ProductLastSeenDataModel): Int {
        return ProductLastSeenViewHolder.LAYOUT
    }

    override fun type(data: ProductOpenShopDataModel): Int {
        return ProductOpenShopViewHolder.LAYOUT
    }

    override fun type(data: ProductRecommendationDataModel): Int {
        return ProductRecommendationViewHolder.LAYOUT
    }

    override fun type(data: ProductTradeinDataModel): Int {
        return ProductTradeinViewHolder.LAYOUT
    }

    override fun type(data: ProductMerchantVoucherDataModel): Int {
        return ProductMerchantVoucherViewHolder.LAYOUT
    }

    override fun type(data: ProductGeneralInfoDataModel): Int {
        return ProductGeneralInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductMostHelpfulReviewDataModel): Int {
        return ProductMostHelpfulReviewViewHolder.LAYOUT
    }

    override fun type(data: ProductImageReviewDataModel): Int {
        return ProductImageReviewViewHolder.LAYOUT
    }

    override fun type(data: ProductDiscussionDataModel): Int {
        return ProductDiscussionViewHolder.LAYOUT
    }

    override fun type(data: ProductInfoDataModel): Int {
        return ProductInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductSocialProofDataModel): Int {
        return ProductSocialProofViewHolder.LAYOUT
    }

    override fun type(data: ProductShopInfoDataModel): Int {
        return ProductShopInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductSnapshotDataModel): Int {
        return ProductSnapshotViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductLastSeenViewHolder.LAYOUT -> ProductLastSeenViewHolder(view)
            ProductOpenShopViewHolder.LAYOUT -> ProductOpenShopViewHolder(view, listener)
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(view, listener)
            ProductTradeinViewHolder.LAYOUT -> ProductTradeinViewHolder(view, listener)
            ProductMerchantVoucherViewHolder.LAYOUT -> ProductMerchantVoucherViewHolder(view, listener)
            ProductImageReviewViewHolder.LAYOUT -> ProductImageReviewViewHolder(view, listener)
            ProductSnapshotViewHolder.LAYOUT -> ProductSnapshotViewHolder(view, listener)
            ProductShopInfoViewHolder.LAYOUT -> ProductShopInfoViewHolder(view, listener)
            ProductSocialProofViewHolder.LAYOUT -> ProductSocialProofViewHolder(view, listener)
            ProductInfoViewHolder.LAYOUT -> ProductInfoViewHolder(view, listener)
            ProductDiscussionViewHolder.LAYOUT -> ProductDiscussionViewHolder(view, listener)
            ProductGeneralInfoViewHolder.LAYOUT -> ProductGeneralInfoViewHolder(view, listener)
            ProductMostHelpfulReviewViewHolder.LAYOUT -> ProductMostHelpfulReviewViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }

}