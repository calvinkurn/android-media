package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import kotlinx.android.synthetic.main.search_inspiration_carousel_option_list.view.*

class InspirationCarouselOptionListViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselViewModel.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_list
    }

    override fun bind(item: InspirationCarouselViewModel.Option) {
        bindOptionTitle(item.title)
        bindOnClickListener(item)

        val productOption = item.product.getOrNull(0) ?: return

        bindProductImage(productOption.imgUrl)
        bindProductName(productOption.name)
        bindProductPrice(productOption.priceStr)
        bindProductRating(productOption.rating)
        bindReviewCount(productOption.countReview)
    }

    private fun bindOptionTitle(title: String) {
        itemView.optionTitle?.shouldShowWithAction(title.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(title)
        }
    }

    private fun <T : View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            this.visibility = View.VISIBLE
            action(this)
        } else {
            this.visibility = View.INVISIBLE
        }
    }

    private fun bindOnClickListener(item: InspirationCarouselViewModel.Option) {
        itemView.viewAllOption?.setOnClickListener { _ ->
            inspirationCarouselListener.onInspirationCarouselSeeAllClicked(item)
        }
        itemView.optionListCardView?.setOnClickListener { _ ->
            val product = item.product.getOrNull(0) ?: return@setOnClickListener
            inspirationCarouselListener.onInspirationCarouselListProductClicked(product)
        }
    }

    private fun bindProductImage(imgUrl: String) {
        itemView.productImage?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(itemView.context, it, imgUrl)
        }
    }

    private fun bindProductName(productName: String) {
        itemView.productName?.shouldShowWithAction(productName.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(productName)
        }
    }

    private fun bindProductPrice(productPrice: String) {
        itemView.productPrice?.shouldShowWithAction(productPrice.isNotEmpty()) {
            itemView.productPrice?.text = productPrice
        }
    }

    private fun bindProductRating(rating: Int) {
        itemView.productRating?.shouldShowWithAction(rating > 0){
            itemView.imageViewRating1?.setImageResource(getRatingDrawable(rating >= 1))
            itemView.imageViewRating2?.setImageResource(getRatingDrawable(rating >= 2))
            itemView.imageViewRating3?.setImageResource(getRatingDrawable(rating >= 3))
            itemView.imageViewRating4?.setImageResource(getRatingDrawable(rating >= 4))
            itemView.imageViewRating5?.setImageResource(getRatingDrawable(rating >= 5))
        }
    }

    private fun getRatingDrawable(isActive: Boolean): Int {
        return if(isActive) R.drawable.search_inspiration_carousel_ic_rating_active
        else R.drawable.search_inspiration_carousel_ic_rating_default
    }

    private fun bindReviewCount(reviewCount: Int){
        itemView.productReviewCount?.shouldShowWithAction(reviewCount > 0) {
            it.text = getReviewCountFormattedAsText(reviewCount)
        }
    }

    private fun getReviewCountFormattedAsText(reviewCount: Int): String {
        return "($reviewCount)"
    }
}