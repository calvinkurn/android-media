package com.tokopedia.product.detail.view.listener

import android.app.Application
import android.util.SparseIntArray
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface DynamicProductDetailListener {
    fun getApplicationContext(): Application?
    fun getLifecycleFragment(): Lifecycle
    fun refreshPage()

    /**
     * ProductSnapshotViewHolder
     */
    fun onImageClicked(position: Int)
    fun onImageClickedTrack(componentTrackDataModel: ComponentTrackDataModel?)
    fun onFabWishlistClicked(isActive: Boolean, componentTrackDataModel: ComponentTrackDataModel)
    fun getProductFragmentManager(): FragmentManager
    fun showAlertCampaignEnded()
    fun txtTradeinClicked(componentTrackDataModel: ComponentTrackDataModel)
    fun onSwipePicture(swipeDirection: String, position: Int, componentTrackDataModel: ComponentTrackDataModel?)
    fun shouldShowWishlist(): Boolean

    /**
     * ProductInfoViewHolder
     */
    fun gotoVideoPlayer(videos: List<Video>, index: Int)
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
    fun onDiscussionSendQuestionClicked(componentTrackDataModel: ComponentTrackDataModel)
    fun goToTalkReading(componentTrackDataModel: ComponentTrackDataModel, numberOfThreadsShown: String)
    fun goToTalkReply(questionId: String, componentTrackDataModel: ComponentTrackDataModel, numberOfThreadsShown: String)

    /**
     * ProductReviewViewHolder
     */
    fun onSeeAllLastItemImageReview(componentTrackDataModel: ComponentTrackDataModel?)
    fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int, componentTrackDataModel: ComponentTrackDataModel?)
    fun onReviewClick()
    fun onImageHelpfulReviewClick(listOfImages: List<String>, position: Int, reviewId: String?, componentTrackDataModel: ComponentTrackDataModel?)
    fun onSeeAllTextView(componentTrackDataModel: ComponentTrackDataModel?)

    /**
     * ProductMerchantVoucherViewHolder
     */
    fun isOwner(): Boolean
    fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int, dataTrackDataModel: ComponentTrackDataModel)
    fun onItemMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, componentTrackDataModel: ComponentTrackDataModel)
    fun onSeeAllMerchantVoucherClick(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductSocialProofViewHolder
     */
    fun onShipmentSocialProofClicked(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductShopInfoViewHolder
     */
    fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel)
    fun gotoShopDetail(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductRecommendationViewHolder
     */
    fun onSeeAllRecomClicked(pageName: String, applink: String, componentTrackDataModel: ComponentTrackDataModel)
    fun eventRecommendationClick(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel)
    fun eventRecommendationImpression(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel)
    fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool?
    fun getRecommendationCarouselSavedState(): SparseIntArray
    fun sendTopAdsClick(topAdsUrl: String, productId: String, productName: String, productImageUrl: String)
    fun sendTopAdsImpression(topAdsUrl: String, productId: String, productName: String, productImageUrl: String)

    /**
     * ProductGeneralInfoViewHolder
     */
    fun onInfoClicked(name: String, componentTrackDataModel: ComponentTrackDataModel)
    fun onValuePropositionClicked(view: Int)

    /**
     * ProductRecom
     */
    fun loadTopads()

    /**
     * ProductDefaultErrorViewHolder
     */
    fun onRetryClicked(forceRefresh:Boolean)
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
     * ProductMiniShopInfoViewHolder
     */
    fun onMiniShopInfoClicked(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductMediaViewHolder
     */
    fun onImageReviewMediaClicked(componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductTickerViewHolder
     */
    fun onTickerGeneralClicked(tickerTitle: String, tickerType: Int, url: String, componentTrackDataModel: ComponentTrackDataModel?)
    fun onTickerShopClicked(tickerTitle: String, tickerType: Int, componentTrackDataModel: ComponentTrackDataModel?)

    /**
     * ProductTopAdsImageViewHolder
     */
    fun onTopAdsImageViewClicked(model: TopAdsImageDataModel, applink: String?, bannerId: String, bannerName: String)
    fun onTopAdsImageViewImpression(model: TopAdsImageDataModel, bannerId: String, bannerName: String)
}