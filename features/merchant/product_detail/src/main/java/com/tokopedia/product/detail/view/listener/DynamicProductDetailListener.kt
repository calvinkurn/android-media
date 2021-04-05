package com.tokopedia.product.detail.view.listener

import android.app.Application
import android.util.SparseIntArray
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleView
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifyprinciples.Typography

interface DynamicProductDetailListener {
    fun getApplicationContext(): Application?
    fun getLifecycleFragment(): Lifecycle
    fun refreshPage()
    fun isNavOld(): Boolean
    fun getFragmentTrackingQueue(): TrackingQueue?

    /**
     * ProductMediaViewHolder
     */
    fun onVideoFullScreenClicked()
    fun onVideoVolumeCLicked(isMute: Boolean)
    fun onVideoStateChange(stopDuration: Long, videoDuration: Long)
    fun getProductVideoCoordinator(): ProductVideoCoordinator?
    /**
     * ProductSnapshotViewHolder
     */
    fun onImageClicked(position: Int)
    fun onMainImageClicked(componentTrackDataModel: ComponentTrackDataModel?, position: Int)
    fun onFabWishlistClicked(isActive: Boolean, componentTrackDataModel: ComponentTrackDataModel)
    fun getProductFragmentManager(): FragmentManager
    fun showAlertCampaignEnded()
    fun txtTradeinClicked(componentTrackDataModel: ComponentTrackDataModel)
    fun onSwipePicture(type: String, url: String, position: Int, componentTrackDataModel: ComponentTrackDataModel?)
    fun shouldShowWishlist(): Boolean

    /**
     * ProductInfoViewHolder
     */
    fun gotoVideoPlayer(youtubeVideos: List<YoutubeVideo>, index: Int)
    fun gotoDescriptionTab(descriptionText: String, componentTrackDataModel: ComponentTrackDataModel)
    fun onCategoryClicked(url: String, componentTrackDataModel: ComponentTrackDataModel)
    fun onEtalaseClicked(url: String, componentTrackDataModel: ComponentTrackDataModel)
    fun goToApplink(url: String)

    fun onBbiInfoClick(url: String, title: String, componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductDiscussionViewHolder
     */
    fun onDiscussionClicked(componentTrackDataModel: ComponentTrackDataModel?)
    fun onDiscussionRefreshClicked()
    fun onDiscussionSendQuestionClicked(componentTrackDataModel: ComponentTrackDataModel?)
    fun goToTalkReading(componentTrackDataModel: ComponentTrackDataModel, numberOfThreadsShown: String)
    fun goToTalkReply(questionId: String, componentTrackDataModel: ComponentTrackDataModel, numberOfThreadsShown: String)

    /**
     * ProductReviewViewHolder
     */
    fun onSeeAllLastItemImageReview(componentTrackDataModel: ComponentTrackDataModel?)
    fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int, componentTrackDataModel: ComponentTrackDataModel?)
    fun onReviewClick()
    fun onSeeAllTextView(componentTrackDataModel: ComponentTrackDataModel?)

    /**
     * ProductMerchantVoucherViewHolder
     */
    fun isOwner(): Boolean
    fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int, dataTrackDataModel: ComponentTrackDataModel)
    fun onItemMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, componentTrackDataModel: ComponentTrackDataModel)
    fun onSeeAllMerchantVoucherClick(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductShopCredibilityViewHolder
     */
    fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel)
    fun gotoShopDetail(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductRecommendationAnnotationChipViewHolder
     */
    fun onChipFilterClicked(recommendationDataModel: ProductRecommendationDataModel, annotationChip: AnnotationChip, position: Int, filterPosition: Int)

    /**
     * ProductRecommendationViewHolder
     */
    fun onSeeAllRecomClicked(pageName: String, applink: String, componentTrackDataModel: ComponentTrackDataModel)
    fun eventRecommendationClick(recomItem: RecommendationItem, chipValue: String, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel)
    fun eventRecommendationImpression(recomItem: RecommendationItem, chipValue: String, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel)
    fun onThreeDotsClick(recomItem: RecommendationItem, adapterPosition: Int, carouselPosition: Int)
    fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool?
    fun getRecommendationCarouselSavedState(): SparseIntArray
    fun sendTopAdsClick(topAdsUrl: String, productId: String, productName: String, productImageUrl: String)
    fun sendTopAdsImpression(topAdsUrl: String, productId: String, productName: String, productImageUrl: String)

    /**
     * PdpComparisonWidgetViewHolder
     */
    fun getStickyTitleView(): StickyTitleView?

    /**
     * ProductGeneralInfoViewHolder
     */
    fun onInfoClicked(name: String, componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductRecom
     */
    fun loadTopads(pageName: String)

    /**
     * ProductDefaultErrorViewHolder
     */
    fun onRetryClicked(forceRefresh: Boolean)
    fun goToHomePageClicked()
    fun goToWebView(url: String)

    /**
     * ImpressionComponent
     */
    fun onImpressComponent(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductNotifyMeViewHolder
     */
    fun onNotifyMeClicked(data: ProductNotifyMeDataModel, componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductTickerViewHolder
     */
    fun onTickerGeneralClicked(tickerTitle: String, tickerType: Int, url: String, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String)
    fun onTickerShopClicked(tickerTitle: String, tickerType: Int, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String)
    fun onTickerGoToRecomClicked(tickerTitle: String, tickerType: Int, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String)

    /**
     * ProductTopAdsImageViewHolder
     */
    fun onTopAdsImageViewClicked(model: TopAdsImageDataModel, applink: String?, bannerId: String, bannerName: String)
    fun onTopAdsImageViewImpression(model: TopAdsImageDataModel, bannerId: String, bannerName: String)

    /**
     * ProductDetailInfoViewHolder
     */
    fun onSeeMoreDescriptionClicked(dataContent: List<ProductDetailInfoContent>, componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductReportViewHolder
     */
    fun reportProductFromComponent(componentTrackDataModel: ComponentTrackDataModel?)

    /**
     * ProductMiniSocialProofChipViewHolder
     */
    fun onBuyerPhotosClicked(componentTrackDataModel: ComponentTrackDataModel?)

    /**
     * ProductShippingViewHolder
     */
    fun openShipmentClickedBottomSheet(title:String, labelShipping:String, isCod:Boolean, componentTrackDataModel:ComponentTrackDataModel?)
    fun clickShippingComponentError(errorCode: Int, title:String, componentTrackDataModel: ComponentTrackDataModel?)
    fun showCoachmark(view: Typography?, isBoeType:Boolean)
}