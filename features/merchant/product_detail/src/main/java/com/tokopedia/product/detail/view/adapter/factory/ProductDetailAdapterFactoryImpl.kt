package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.component.shipment.ShipmentUiModel
import com.tokopedia.product.detail.component.shipment.ShipmentViewHolder
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
import com.tokopedia.product.detail.view.fragment.delegate.PdpCallbackDelegate
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ContentWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.FintechWidgetV2ViewHolder
import com.tokopedia.product.detail.view.viewholder.FintechWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.GlobalBundlingViewHolder
import com.tokopedia.product.detail.view.viewholder.LoadingViewHolder
import com.tokopedia.product.detail.view.viewholder.OneLinersViewHolder
import com.tokopedia.product.detail.view.viewholder.PageErrorViewHolder
import com.tokopedia.product.detail.view.viewholder.PdpComparisonWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.PdpRecommendationWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductArViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductCategoryCarouselViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductContentViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductCustomInfoTitleViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductCustomInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductDiscussionMostHelpfulViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductGeneralInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMerchantVoucherSummaryViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniShopWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMiniSocialProofStockViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecomWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductReportViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductShimmeringViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductShopAdditionalViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductShopCredibilityViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductSingleVariantViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductTickerInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductTopAdsImageViewHolder
import com.tokopedia.product.detail.view.viewholder.SDUIViewHolder
import com.tokopedia.product.detail.view.viewholder.TabletLeftSectionViewHolder
import com.tokopedia.product.detail.view.viewholder.TabletRightSectionViewHolder
import com.tokopedia.product.detail.view.viewholder.TopAdsHeadlineViewHolder
import com.tokopedia.product.detail.view.viewholder.ViewToViewWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.a_plus_content.APlusImageUiModel
import com.tokopedia.product.detail.view.viewholder.a_plus_content.APlusImageViewHolder
import com.tokopedia.product.detail.view.viewholder.bmgm.BMGMUiModel
import com.tokopedia.product.detail.view.viewholder.bmgm.BMGMViewHolder
import com.tokopedia.product.detail.view.viewholder.campaign.ui.OngoingCampaignViewHolder
import com.tokopedia.product.detail.view.viewholder.campaign.ui.ProductNotifyMeViewHolder
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.OngoingCampaignUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.ProductNotifyMeUiModel
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.DynamicOneLinerUiModel
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.DynamicOneLinerViewHolder
import com.tokopedia.product.detail.view.viewholder.gwp.GWPUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.GWPViewHolder
import com.tokopedia.product.detail.view.viewholder.media.ProductMediaViewHolder
import com.tokopedia.product.detail.view.viewholder.product_detail_info.ProductDetailInfoViewHolder
import com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.ProductThumbnailVariantViewHolder
import com.tokopedia.product.detail.view.viewholder.promo_price.ui.ProductPriceUiModel
import com.tokopedia.product.detail.view.viewholder.promo_price.ui.ProductPriceViewHolder
import com.tokopedia.product.detail.view.viewholder.review.ui.ProductReviewViewHolder
import com.tokopedia.product.detail.view.viewholder.show_review.ProductShopReviewViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.ProductMiniSocialProofViewHolder

class ProductDetailAdapterFactoryImpl(
    private val listener: ProductDetailListener,
    private val variantListener: AtcVariantListener,
    private val userId: String,
    private val playWidgetCoordinator: PlayWidgetCoordinator,
    private val affiliateCookieHelper: AffiliateCookieHelper,
    private val pdpCallback: PdpCallbackDelegate

) : BaseAdapterTypeFactory(), ProductDetailAdapterFactory {
    override fun type(data: ProductRecommendationDataModel): Int {
        return ProductRecommendationViewHolder.LAYOUT
    }

    override fun type(data: ProductGeneralInfoDataModel): Int {
        return ProductGeneralInfoViewHolder.LAYOUT
    }

    override fun type(data: ProductMostHelpfulReviewUiModel): Int {
        return ProductReviewViewHolder.LAYOUT
    }

    override fun type(data: ProductDiscussionMostHelpfulDataModel): Int {
        return ProductDiscussionMostHelpfulViewHolder.LAYOUT
    }

    override fun type(data: PageErrorDataModel): Int {
        return PageErrorViewHolder.LAYOUT
    }

    override fun type(data: ProductNotifyMeUiModel): Int {
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
        return com.tokopedia.product.detail.view.viewholder.ShipmentViewHolder.LAYOUT
    }

    override fun type(data: ProductMerchantVoucherSummaryDataModel): Int {
        return ProductMerchantVoucherSummaryViewHolder.LAYOUT
    }

    override fun type(data: PdpComparisonWidgetDataModel): Int {
        return PdpComparisonWidgetViewHolder.LAYOUT
    }

    override fun type(data: ProductSingleVariantDataModel): Int {
        return if (data.isThumbnailType) {
            ProductThumbnailVariantViewHolder.LAYOUT
        } else {
            ProductSingleVariantViewHolder.LAYOUT
        }
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

    override fun type(data: ContentWidgetDataModel): Int {
        return ContentWidgetViewHolder.LAYOUT
    }

    override fun type(data: ArButtonDataModel): Int {
        return ProductArViewHolder.LAYOUT
    }

    override fun type(data: FintechWidgetDataModel): Int {
        return FintechWidgetViewHolder.LAYOUT
    }

    override fun type(data: FintechWidgetV2DataModel): Int {
        return FintechWidgetV2ViewHolder.LAYOUT
    }

    override fun type(data: LoadingDataModel): Int {
        return LoadingViewHolder.LAYOUT
    }

    override fun type(data: GlobalBundlingDataModel): Int {
        return GlobalBundlingViewHolder.LAYOUT
    }

    override fun type(data: ProductShopAdditionalDataModel): Int {
        return ProductShopAdditionalViewHolder.LAYOUT
    }

    override fun type(data: ViewToViewWidgetDataModel): Int {
        return ViewToViewWidgetViewHolder.LAYOUT
    }

    override fun type(data: ProductCustomInfoTitleDataModel): Int {
        return ProductCustomInfoTitleViewHolder.LAYOUT
    }

    override fun type(data: ProductShopReviewDataModel): Int {
        return ProductShopReviewViewHolder.LAYOUT
    }

    override fun type(data: PdpRecommendationWidgetDataModel): Int {
        return PdpRecommendationWidgetViewHolder.LAYOUT
    }

    override fun type(data: OngoingCampaignUiModel): Int {
        return OngoingCampaignViewHolder.LAYOUT
    }

    override fun type(data: DynamicOneLinerUiModel): Int {
        return DynamicOneLinerViewHolder.LAYOUT
    }

    override fun type(data: APlusImageUiModel): Int {
        return APlusImageViewHolder.LAYOUT
    }

    override fun type(data: BMGMUiModel): Int {
        return BMGMViewHolder.LAYOUT
    }

    override fun type(data: ShipmentUiModel): Int {
        return ShipmentViewHolder.LAYOUT
    }

    override fun type(data: ProductPriceUiModel): Int {
        return ProductPriceViewHolder.LAYOUT
    }

    override fun type(data: ProductTabletLeftSectionDataModel): Int {
        return TabletLeftSectionViewHolder.LAYOUT
    }

    override fun type(data: ProductTabletRightSectionDataModel): Int {
        return TabletRightSectionViewHolder.LAYOUT
    }

    override fun type(data: GWPUiModel): Int {
        return GWPViewHolder.LAYOUT
    }

    override fun type(data: SDUIDataModel): Int {
        return SDUIViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            FintechWidgetViewHolder.LAYOUT -> FintechWidgetViewHolder(view, listener)
            FintechWidgetV2ViewHolder.LAYOUT -> FintechWidgetV2ViewHolder(view, listener)
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(
                view,
                listener,
                affiliateCookieHelper
            )

            PdpRecommendationWidgetViewHolder.LAYOUT -> PdpRecommendationWidgetViewHolder(
                view,
                listener
            )

            ProductDiscussionMostHelpfulViewHolder.LAYOUT -> ProductDiscussionMostHelpfulViewHolder(
                view,
                listener
            )

            ProductGeneralInfoViewHolder.LAYOUT -> ProductGeneralInfoViewHolder(view, listener)
            ProductReviewViewHolder.LAYOUT -> ProductReviewViewHolder(
                view,
                listener,
                pdpCallback.review
            )

            ProductShimmeringViewHolder.LAYOUT -> ProductShimmeringViewHolder(view)
            PageErrorViewHolder.LAYOUT -> PageErrorViewHolder(view, listener)
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

            ProductMiniShopWidgetViewHolder.LAYOUT -> ProductMiniShopWidgetViewHolder(
                view,
                listener
            )

            ProductTickerInfoViewHolder.LAYOUT -> ProductTickerInfoViewHolder(view, listener)
            ProductShopCredibilityViewHolder.LAYOUT -> ProductShopCredibilityViewHolder(
                view,
                listener
            )

            ProductCustomInfoViewHolder.LAYOUT -> ProductCustomInfoViewHolder(view, listener)
            ProductTopAdsImageViewHolder.LAYOUT -> ProductTopAdsImageViewHolder(view, listener)
            ProductDetailInfoViewHolder.LAYOUT -> ProductDetailInfoViewHolder(view, listener)
            ProductReportViewHolder.LAYOUT -> ProductReportViewHolder(view, listener)
            com.tokopedia.product.detail.view.viewholder.ShipmentViewHolder.LAYOUT -> com.tokopedia.product.detail.view.viewholder.ShipmentViewHolder(
                view,
                listener
            )

            ShipmentViewHolder.LAYOUT -> ShipmentViewHolder(view, listener)
            ProductMerchantVoucherSummaryViewHolder.LAYOUT -> ProductMerchantVoucherSummaryViewHolder(
                view,
                listener
            )

            PdpComparisonWidgetViewHolder.LAYOUT -> PdpComparisonWidgetViewHolder(view, listener)
            ProductSingleVariantViewHolder.LAYOUT -> ProductSingleVariantViewHolder(
                view,
                variantListener,
                listener
            )

            ProductThumbnailVariantViewHolder.LAYOUT -> ProductThumbnailVariantViewHolder(
                view,
                variantListener,
                listener
            )

            OneLinersViewHolder.LAYOUT -> OneLinersViewHolder(view, listener)
            ProductRecomWidgetViewHolder.LAYOUT -> ProductRecomWidgetViewHolder(view, listener)
            ProductCategoryCarouselViewHolder.LAYOUT -> ProductCategoryCarouselViewHolder(
                view,
                listener
            )

            TopAdsHeadlineViewHolder.LAYOUT -> TopAdsHeadlineViewHolder(view, userId, listener)
            ContentWidgetViewHolder.LAYOUT -> {
                val playWidgetView: View? = view.findViewById(R.id.pdp_play_widget_view)
                if (playWidgetView != null) {
                    ContentWidgetViewHolder(
                        view,
                        PlayWidgetViewHolder(
                            itemView = playWidgetView,
                            coordinator = playWidgetCoordinator
                        ),
                        listener
                    )
                } else {
                    super.createViewHolder(view, type)
                }
            }
            LoadingViewHolder.LAYOUT -> LoadingViewHolder(view)
            GlobalBundlingViewHolder.LAYOUT -> GlobalBundlingViewHolder(view, listener)
            ProductShopAdditionalViewHolder.LAYOUT -> ProductShopAdditionalViewHolder(
                view = view,
                listener = listener
            )

            ProductArViewHolder.LAYOUT -> ProductArViewHolder(view, listener)
            ViewToViewWidgetViewHolder.LAYOUT -> ViewToViewWidgetViewHolder(view, listener)
            ProductCustomInfoTitleViewHolder.LAYOUT -> ProductCustomInfoTitleViewHolder(view = view)
            ProductShopReviewViewHolder.LAYOUT -> ProductShopReviewViewHolder(
                view = view,
                listener = listener
            )

            OngoingCampaignViewHolder.LAYOUT -> OngoingCampaignViewHolder(view, listener)
            DynamicOneLinerViewHolder.LAYOUT -> DynamicOneLinerViewHolder(
                view,
                pdpCallback.dynamicOneLiner
            )
            APlusImageViewHolder.LAYOUT -> APlusImageViewHolder(view, listener)
            BMGMViewHolder.LAYOUT -> BMGMViewHolder(view, listener)
            ProductPriceViewHolder.LAYOUT -> ProductPriceViewHolder(view, pdpCallback.productPrice)
            TabletLeftSectionViewHolder.LAYOUT -> TabletLeftSectionViewHolder(view, listener, this)
            TabletRightSectionViewHolder.LAYOUT -> TabletRightSectionViewHolder(view, listener, this)
            GWPViewHolder.LAYOUT -> GWPViewHolder(view, pdpCallback.gwp)
            SDUIViewHolder.LAYOUT -> SDUIViewHolder(view, pdpCallback.sdui)
            else -> super.createViewHolder(view, type)
        }
    }
}
