package com.tokopedia.product.detail.view.listener

import android.app.Application
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface DynamicProductDetailListener {
    val onViewClickListener: View.OnClickListener
    fun getApplicationContext(): Application?

    /**
     * ProductSnapshotViewHolder
     */
    fun onImageClicked(position: Int)
    fun onFabWishlistClicked(isActive: Boolean)
    fun getProductFragmentManager(): FragmentManager
    fun showAlertCampaignEnded()
    fun txtTradeinClicked()
    fun onSwipePicture(swipeDirection: String)

    /**
     * ProductInfoViewHolder
     */
    fun gotoVideoPlayer(videos: List<Video>, index: Int)
    fun gotoDescriptionTab(data: DescriptionData, listOfCatalog: ArrayList<Specification>)

    /**
     * ProductDiscussionViewHolder
     */
    fun onDiscussionClicked()

    /**
     * ProductReviewViewHolder
     */
    fun onSeeAllReviewClick()
    fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int)
    fun onReviewClick()
    fun onImageHelpfulReviewClick(listOfImages: List<String>, position: Int, reviewId: String?)

    /**
     * ProductMerchantVoucherViewHolder
     */
    fun isOwner(): Boolean
    fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int)
    fun onItemMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel)
    fun onSeeAllMerchantVoucherClick()

    /**
     * ProductSocialProofViewHolder
     */
    fun onShipmentClicked()

    /**
     * ProductOpenShopViewHolder
     */
    fun openShopClicked()

    /**
     * ProductRecommendationViewHolder
     */
    fun onSeeAllRecomClicked(pageName: String, applink: String)
    fun eventRecommendationClick(recomItem: RecommendationItem, position: Int, pageName: String, title: String)
    fun eventRecommendationImpression(recomItem: RecommendationItem, position: Int, pageName: String, title: String)
    fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool?

    /**
     * ProductGeneralInfoViewHolder
     */
    fun onInfoClicked(name: String)
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
    fun goToTobacooError(url: String)
}