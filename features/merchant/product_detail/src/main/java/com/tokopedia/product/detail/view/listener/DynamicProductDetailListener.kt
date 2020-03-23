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
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface DynamicProductDetailListener {
    fun getApplicationContext(): Application?
    fun getLifecycleFragment(): Lifecycle

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

    /**
     * ProductInfoViewHolder
     */
    fun gotoVideoPlayer(videos: List<Video>, index: Int)
    fun gotoDescriptionTab(data: DescriptionData, listOfCatalog: ArrayList<Specification>, componentTrackDataModel: ComponentTrackDataModel)
    fun onCategoryClicked(url: String, componentTrackDataModel: ComponentTrackDataModel)
    fun onEtalaseClicked(url: String, componentTrackDataModel: ComponentTrackDataModel)
    fun goToApplink(url: String)

    /**
     * ProductDiscussionViewHolder
     */
    fun onDiscussionClicked(componentTrackDataModel: ComponentTrackDataModel?)
    fun onLastDiscussionClicked(talkId:String, componentTrackDataModel: ComponentTrackDataModel?)

    /**
     * ProductReviewViewHolder
     */
    fun onSeeAllReviewClick(componentTrackDataModel: ComponentTrackDataModel?)
    fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int, componentTrackDataModel: ComponentTrackDataModel?)
    fun onReviewClick()
    fun onImageHelpfulReviewClick(listOfImages: List<String>, position: Int, reviewId: String?, componentTrackDataModel: ComponentTrackDataModel?)

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
     * ProductOpenShopViewHolder
     */
    fun openShopClicked()

    /**
     * ProductShopInfoViewHolder
     */
    fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel)

    /**
     * ProductRecommendationViewHolder
     */
    fun onSeeAllRecomClicked(pageName: String, applink: String, componentTrackDataModel: ComponentTrackDataModel)
    fun eventRecommendationClick(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel)
    fun eventRecommendationImpression(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel)
    fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool?
    fun getRecommendationCarouselSavedState(): SparseIntArray

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
     * Imei Checker
     */
    fun checkImei()

}