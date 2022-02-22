package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
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
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ContentWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.FintechWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.OneLinersViewHolder
import com.tokopedia.product.detail.view.viewholder.PageErrorViewHolder
import com.tokopedia.product.detail.view.viewholder.PdpComparisonWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductBundlingViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductCategoryCarouselViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductContentViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductCustomInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductDetailInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductDiscussionMostHelpfulViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductGeneralInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMediaViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMerchantVoucherSummaryViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniShopWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofStockViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductNotifyMeViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecomWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductReportViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductReviewViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductShimmeringViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductShipmentViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductShopCredibilityViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductSingleVariantViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductTickerInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductTopAdsImageViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductVariantViewHolder
import com.tokopedia.product.detail.view.viewholder.TopAdsHeadlineViewHolder

class DynamicProductDetailAdapterFactoryImpl(
    private val listener: DynamicProductDetailListener,
    private val variantListener: AtcVariantListener,
    private val userId: String,
    private val playWidgetCoordinator: PlayWidgetCoordinator
)
    : BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory {
    override fun type(data: ProductRecommendationDataModel): Int {
        return ProductRecommendationViewHolder.LAYOUT
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

    override fun type(data: ProductMiniSocialProofStockDataModel): Int {
        return ProductMiniSocialProofStockViewHolder.LAYOUT
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

    override fun type(data: ProductSingleVariantDataModel): Int {
        return ProductSingleVariantViewHolder.LAYOUT
    }

    override fun type(data: ProductMiniShopWidgetDataModel): Int {
        return ProductMiniShopWidgetViewHolder.LAYOUT
    }

    override fun type(data: ProductRecomWidgetDataModel): Int {
        return ProductRecomWidgetViewHolder.LAYOUT
    }

    override fun type(data: OneLinersDataModel): Int {
        return OneLinersViewHolder.LAYOUT
    }

    override fun type(data: ProductCategoryCarouselDataModel): Int {
        return ProductCategoryCarouselViewHolder.LAYOUT
    }

    override fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int {
        return TopAdsHeadlineViewHolder.LAYOUT
    }

    override fun type(data: ProductBundlingDataModel): Int {
        return ProductBundlingViewHolder.LAYOUT
    }

    override fun type(data: ContentWidgetDataModel): Int {
        return ContentWidgetViewHolder.LAYOUT
    }

    override fun type(data: FintechWidgetDataModel): Int {
        return FintechWidgetViewHolder.LAYOUT
    }


    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            FintechWidgetViewHolder.LAYOUT -> FintechWidgetViewHolder(view,listener)
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(
                view,
                listener
            )
            ProductDiscussionMostHelpfulViewHolder.LAYOUT -> ProductDiscussionMostHelpfulViewHolder(
                view,
                listener
            )
            ProductGeneralInfoViewHolder.LAYOUT -> ProductGeneralInfoViewHolder(view, listener)
            ProductReviewViewHolder.LAYOUT -> ProductReviewViewHolder(view, listener)
            ProductShimmeringViewHolder.LAYOUT -> ProductShimmeringViewHolder(view)
            PageErrorViewHolder.LAYOUT -> PageErrorViewHolder(view, listener)
            ProductVariantViewHolder.LAYOUT -> ProductVariantViewHolder(
                view,
                variantListener,
                listener
            )
            ProductNotifyMeViewHolder.LAYOUT -> ProductNotifyMeViewHolder(view, listener)
            ProductMediaViewHolder.LAYOUT -> ProductMediaViewHolder(view, listener)
            ProductContentViewHolder.LAYOUT -> ProductContentViewHolder(view, listener)
            ProductMiniSocialProofViewHolder.LAYOUT -> ProductMiniSocialProofViewHolder(
                view,
                listener
            )
            ProductMiniSocialProofStockViewHolder.LAYOUT -> ProductMiniSocialProofStockViewHolder(
                view,
                listener
            )

            ProductMiniShopWidgetViewHolder.LAYOUT -> ProductMiniShopWidgetViewHolder(view, listener)
            ProductTickerInfoViewHolder.LAYOUT -> ProductTickerInfoViewHolder(view, listener)
            ProductShopCredibilityViewHolder.LAYOUT -> ProductShopCredibilityViewHolder(view, listener)
            ProductCustomInfoViewHolder.LAYOUT -> ProductCustomInfoViewHolder(view, listener)
            ProductTopAdsImageViewHolder.LAYOUT -> ProductTopAdsImageViewHolder(view, listener)
            ProductDetailInfoViewHolder.LAYOUT -> ProductDetailInfoViewHolder(view, listener)
            ProductReportViewHolder.LAYOUT -> ProductReportViewHolder(view, listener)
            ProductShipmentViewHolder.LAYOUT -> ProductShipmentViewHolder(view, listener)
            ProductMerchantVoucherSummaryViewHolder.LAYOUT -> ProductMerchantVoucherSummaryViewHolder(view, listener)
            PdpComparisonWidgetViewHolder.LAYOUT -> PdpComparisonWidgetViewHolder(view, listener)
            ProductSingleVariantViewHolder.LAYOUT -> ProductSingleVariantViewHolder(view, variantListener, listener)
            OneLinersViewHolder.LAYOUT -> OneLinersViewHolder(view, listener)
            ProductRecomWidgetViewHolder.LAYOUT -> ProductRecomWidgetViewHolder(view, listener)
            ProductCategoryCarouselViewHolder.LAYOUT -> ProductCategoryCarouselViewHolder(view, listener)
            TopAdsHeadlineViewHolder.LAYOUT -> TopAdsHeadlineViewHolder(view, userId, listener)
            ProductBundlingViewHolder.LAYOUT -> ProductBundlingViewHolder(view, listener)
            ContentWidgetViewHolder.LAYOUT -> {
                val playWidgetView: View? = view.findViewById(R.id.pdp_play_widget_view)
                if (playWidgetView != null) {
                    ContentWidgetViewHolder(
                        view, listener, PlayWidgetViewHolder(
                            itemView = playWidgetView,
                            coordinator = playWidgetCoordinator
                        )
                    )
                } else super.createViewHolder(view, type)
            }
            else -> super.createViewHolder(view, type)
        }
    }

}