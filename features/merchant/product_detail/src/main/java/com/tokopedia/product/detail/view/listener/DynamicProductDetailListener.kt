package com.tokopedia.product.detail.view.listener

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tradein.model.TradeInParams

interface DynamicProductDetailListener {
    val onViewClickListener: View.OnClickListener

    /**
     * ProductSnapshotViewHolder
     */
    fun onImageClicked(position: Int)
    fun onFabWishlistClicked(isActive: Boolean)
    fun getChild(): FragmentManager?

    /**
     * ProductInfoViewHolder
     */
    fun openCategory(category: Category.Detail)
    fun gotoEtalase(etalaseId: String, shopID: Int)
    fun gotoVideoPlayer(videos: List<Video>, index: Int)
    fun gotoDescriptionTab(data: DescriptionData, listOfCatalog: ArrayList<Specification>)

    /**
     * ProductDiscussionViewHolder
     */
    fun onDiscussionClicked()

    /**
     * ProductImageReviewViewHolder
     */
    fun onSeeAllReviewClick()
    fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int)
    fun onReviewClick()

    /**
     * ProductMostHelpfulReviewViewHolder
     */
    fun onImageHelpfulReviewClick(listOfImages: List<String>, position: Int, reviewId: String?)

    /**
     * ProductMerchantVoucherViewHolder
     */
    fun isOwner(): Boolean
    fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int)
    fun onItemMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel)
    fun onSeeAllMerchantVoucherClick()

    /**
     * ProductTradeinViewHolder
     */
    fun onTradeinClicked(tradeInParams: TradeInParams)

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

    /**
     * ProductGeneralInfoViewHolder
     */
    fun onDescriptioInfonClicked(name: String)
    fun onInfoClicked(name: String)
}