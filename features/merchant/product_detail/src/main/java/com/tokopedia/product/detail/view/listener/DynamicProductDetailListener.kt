package com.tokopedia.product.detail.view.listener

import android.util.SparseIntArray
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.ticker.TickerActionBs
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewItemData
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ImageUnify

interface DynamicProductDetailListener {
    fun refreshPage()
    fun isNavOld(): Boolean
    fun getFragmentTrackingQueue(): TrackingQueue?
    fun getVariantString(): String
    fun getParentViewModelStoreOwner(): ViewModelStore
    fun getParentLifeCyclerOwner(): LifecycleOwner
    fun getRemoteConfigInstance(): RemoteConfig?

    /**
     * ProductMediaViewHolder
     */
    fun onVideoFullScreenClicked()
    fun onVideoVolumeCLicked(isMute: Boolean)
    fun onVideoStateChange(stopDuration: Long, videoDuration: Long)
    fun getProductVideoCoordinator(): ProductVideoCoordinator?
    fun onThumbnailImpress(
        position: Int,
        media: MediaDataModel,
        componentTrackDataModel: ComponentTrackDataModel?
    )

    fun trackThumbnailClicked(
        position: Int,
        media: MediaDataModel,
        componentTrackDataModel: ComponentTrackDataModel?
    )

    fun onMerchantVoucherSummaryClicked(shopId: String, @MvcSource source: Int, productId: String)
    fun showThumbnailImage(): Boolean

    /**
     * ProductSnapshotViewHolder
     */
    fun onImageClicked(position: Int)
    fun onMainImageClicked(componentTrackDataModel: ComponentTrackDataModel?, position: Int)
    fun onFabWishlistClicked(isActive: Boolean, componentTrackDataModel: ComponentTrackDataModel)
    fun getProductFragmentManager(): FragmentManager
    fun showAlertCampaignEnded()
    fun txtTradeinClicked(componentTrackDataModel: ComponentTrackDataModel)
    fun onSwipePicture(
        type: String,
        url: String,
        position: Int,
        variantOptionId: String,
        componentTrackDataModel: ComponentTrackDataModel?
    )

    fun shouldShowWishlist(): Boolean

    /**
     * ProductInfoViewHolder
     */
    fun onCategoryClicked(url: String, componentTrackDataModel: ComponentTrackDataModel)
    fun onEtalaseClicked(url: String, componentTrackDataModel: ComponentTrackDataModel)
    fun goToApplink(url: String)
    fun goToEducational(url: String)

    fun onBbiInfoClick(url: String, title: String, componentTrackDataModel: ComponentTrackDataModel)

    /**
     * BestSellerViewHolder
     */
    fun onClickBestSeller(componentTrackDataModel: ComponentTrackDataModel, appLink: String)

    /**
     * OneLinerViewHolder
     */
    fun onImpressStockAssurance(componentTrackDataModel: ComponentTrackDataModel, label: String)
    fun onClickInformationIconAtStockAssurance(
        componentTrackDataModel: ComponentTrackDataModel,
        appLink: String,
        label: String
    )
    fun showOneLinersImsCoachMark(view: ImageUnify?)

    /**
     * ProductDiscussionViewHolder
     */
    fun onDiscussionClicked(componentTrackDataModel: ComponentTrackDataModel?)
    fun onDiscussionRefreshClicked()
    fun onDiscussionSendQuestionClicked(componentTrackDataModel: ComponentTrackDataModel?)
    fun goToTalkReading(
        componentTrackDataModel: ComponentTrackDataModel,
        numberOfThreadsShown: String
    )

    fun goToTalkReply(
        questionId: String,
        componentTrackDataModel: ComponentTrackDataModel,
        numberOfThreadsShown: String
    )

    /**
     * ProductReviewViewHolder
     */
    fun onSeeAllLastItemMediaReview(componentTrackDataModel: ComponentTrackDataModel?)
    fun onMediaReviewClick(
        reviewID: String,
        position: Int,
        componentTrackDataModel: ComponentTrackDataModel?,
        detailedMediaResult: ProductrevGetReviewMedia
    )

    fun onReviewClick()
    fun onSeeAllTextView(componentTrackDataModel: ComponentTrackDataModel?)
    fun onSeeReviewCredibility(
        reviewID: String,
        reviewerUserID: String,
        userStatistics: String,
        userLabel: String,
        componentTrackData: ComponentTrackDataModel
    )

    /**
     * ProductMerchantVoucherViewHolder
     */
    fun isOwner(): Boolean

    /**
     * FintechWidgetViewHolder
     */
    fun fintechRedirection(
        fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass,
        redirectionUrl: String
    )

    /**
     * ProductShopCredibilityViewHolder
     */
    fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel)
    fun onShopMultilocClicked(componentTrackDataModel: ComponentTrackDataModel)
    fun gotoShopDetail(componentTrackDataModel: ComponentTrackDataModel)
    fun onShopTickerClicked(
        tickerDataResponse: ShopInfo.TickerDataResponse,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun onShopTickerImpressed(
        tickerDataResponse: ShopInfo.TickerDataResponse,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun onShopCredibilityImpressed(
        countLocation: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    /**
     * [ProductShopAdditionalViewHolder]
     */
    fun onLearnButtonShopAdditionalClicked(
        componentTrackDataModel: ComponentTrackDataModel,
        eventLabel: String
    )

    /**
     * ProductRecommendationAnnotationChipViewHolder
     */
    fun onChipFilterClicked(
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip,
        position: Int,
        filterPosition: Int
    )

    /**
     * ProductRecommendationViewHolder
     */
    fun onSeeAllRecomClicked(
        recommendationWidget: RecommendationWidget,
        pageName: String,
        applink: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun eventRecommendationClick(
        recomItem: RecommendationItem,
        chipValue: String,
        position: Int,
        pageName: String,
        title: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun eventRecommendationImpression(
        recomItem: RecommendationItem,
        chipValue: String,
        position: Int,
        pageName: String,
        title: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun onThreeDotsClick(recomItem: RecommendationItem, adapterPosition: Int, carouselPosition: Int)
    fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool?
    fun getRecommendationCarouselSavedState(): SparseIntArray
    fun sendTopAdsClick(
        topAdsUrl: String,
        productId: String,
        productName: String,
        productImageUrl: String
    )

    fun sendTopAdsImpression(
        topAdsUrl: String,
        productId: String,
        productName: String,
        productImageUrl: String
    )

    fun onChannelRecommendationEmpty(channelPosition: Int, data: RecommendationWidget?)
    fun onRecommendationBannerImpressed(data: RecommendationWidget, templateNameType: String)
    fun onRecommendationBannerClicked(
        appLink: String,
        data: RecommendationWidget,
        templateNameType: String
    )

    fun onRecomAddToCartNonVariantQuantityChangedClick(
        recomItem: RecommendationItem,
        quantity: Int,
        adapterPosition: Int,
        itemPosition: Int
    )

    fun onRecomAddVariantClick(
        recomItem: RecommendationItem,
        adapterPosition: Int,
        itemPosition: Int
    )

    /**
     * ProductGeneralInfoViewHolder
     */
    fun onInfoClicked(
        appLink: String,
        name: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    /**
     * ProductRecom
     */
    fun loadTopads(pageName: String)
    fun loadViewToView(pageName: String)

    fun loadPlayWidget()

    /**
     * ProductDefaultErrorViewHolder
     */
    fun onRetryClicked(forceRefresh: Boolean)
    fun goToHomePageClicked()
    fun goToWebView(url: String)
    fun onImpressPageNotFound()

    /**
     * ImpressionComponent
     */
    fun onImpressComponent(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductNotifyMeViewHolder
     */
    fun onNotifyMeClicked(
        data: ProductNotifyMeDataModel,
        componentTrackDataModel: ComponentTrackDataModel
    )

    /**
     * ProductTickerViewHolder
     */
    fun onTickerShopClicked(
        tickerTitle: String,
        tickerType: Int,
        componentTrackDataModel: ComponentTrackDataModel?,
        tickerDescription: String,
        applink: String,
        actionType: String,
        tickerActionBs: TickerActionBs?
    )

    fun onTickerGoToRecomClicked(
        tickerTitle: String,
        tickerType: Int,
        componentTrackDataModel: ComponentTrackDataModel?,
        tickerDescription: String
    )

    /**
     * ProductTopAdsImageViewHolder
     */
    fun onTopAdsImageViewClicked(
        model: TopAdsImageDataModel,
        applink: String?,
        bannerId: String,
        bannerName: String
    )

    fun onTopAdsImageViewImpression(
        model: TopAdsImageDataModel,
        bannerId: String,
        bannerName: String
    )

    /**
     * ProductDetailInfoViewHolder
     */
    fun onSeeMoreDescriptionClicked(
        infoData: ProductDetailInfoDataModel,
        componentTrackDataModel: ComponentTrackDataModel
    )
    fun onSeeMoreSpecificationClicked(
        infoData: ProductDetailInfoDataModel,
        componentTrackDataModel: ComponentTrackDataModel
    )

    /**
     * ProductReportViewHolder
     */
    fun reportProductFromComponent(componentTrackDataModel: ComponentTrackDataModel?)

    /**
     * ProductMiniSocialProofChipViewHolder
     */
    fun onBuyerPhotosClicked(componentTrackDataModel: ComponentTrackDataModel?)

    fun onSocialProofItemClickTracking(
        socialProofId: String,
        trackData: ComponentTrackDataModel?
    )

    /**
     * ProductShippingViewHolder
     */
    fun openShipmentClickedBottomSheet(
        title: String,
        chipsLabel: List<String>,
        isCod: Boolean,
        isScheduled: Boolean,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun clickShippingComponentError(
        errorCode: Int,
        title: String,
        componentTrackDataModel: ComponentTrackDataModel?
    )

    fun onImpressScheduledDelivery(
        labels: List<String>,
        componentTrackDataModel: ComponentTrackDataModel
    )

    /**
     * ProductArViewHolder
     */
    fun showArCoachMark(view: ConstraintLayout?)
    fun hideArCoachMark()
    fun goToArPage(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductCategoryCarouselViewHolder
     */
    fun onCategoryCarouselImageClicked(
        url: String,
        categoryTitle: String,
        categoryId: String,
        componentTrackDataModel: ComponentTrackDataModel?
    )

    fun onCategoryCarouselSeeAllClicked(
        url: String,
        componentTrackDataModel: ComponentTrackDataModel?
    )

    /**
     * ProductBundlingViewHolder
     */
    fun onImpressionProductBundling(
        bundleId: String,
        bundleType: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun onClickCheckBundling(
        bundleId: String,
        bundleType: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun onClickActionButtonBundling(
        bundleId: String,
        bundleType: String,
        componentTrackDataModel: ComponentTrackDataModel
    )

    fun onClickProductInBundling(
        bundleId: String,
        bundleProductId: String,
        componentTrackDataModel: ComponentTrackDataModel,
        isOldBundlingWidget: Boolean = true
    )

    /**
     * ContentWidgetViewHolder
     */
    fun onImpressChannelCard(
        componentTrackDataModel: ComponentTrackDataModel,
        item: PlayWidgetChannelUiModel
    )

    fun onClickChannelCard(
        componentTrackDataModel: ComponentTrackDataModel,
        item: PlayWidgetChannelUiModel
    )

    fun onClickBannerCard(componentTrackDataModel: ComponentTrackDataModel)
    fun onClickViewAll(componentTrackDataModel: ComponentTrackDataModel)
    fun onClickToggleReminderChannel(
        componentTrackDataModel: ComponentTrackDataModel,
        item: PlayWidgetChannelUiModel,
        isRemindMe: Boolean
    )

    /**
     * ProductDetailNavigation / Navigation Bar / Tab
     */
    fun onImpressBackToTop(label: String)
    fun onImpressProductDetailNavigation(labels: List<String>)
    fun onClickProductDetailnavigation(position: Int, label: String)
    fun updateNavigationTabPosition()

    fun onImpressRecommendationVertical(componentTrackDataModel: ComponentTrackDataModel)
    fun startVerticalRecommendation(pageName: String)
    fun getRecommendationVerticalTrackData(): ComponentTrackDataModel?

    /**
     * ViewToView widget recommendation
     */
    fun onViewToViewImpressed(
        data: ViewToViewItemData,
        title: String,
        itemPosition: Int,
        adapterPosition: Int
    )
    fun onViewToViewClicked(
        data: ViewToViewItemData,
        title: String,
        itemPosition: Int,
        adapterPosition: Int
    )
    fun onViewToViewReload(pageName: String)

    /**
     * Thumbnail Variant
     */
    fun onThumbnailVariantSelected(variantId: String, categoryKey: String)

    fun onThumbnailVariantImpress(data: VariantOptionWithAttribute, position: Int)
}
