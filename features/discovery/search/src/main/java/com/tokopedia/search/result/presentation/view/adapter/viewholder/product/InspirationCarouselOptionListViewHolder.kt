package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
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

        bindImpressionListener(productOption)
        bindProductImage(productOption.imgUrl)
        bindProductName(productOption.name)
        bindProductPrice(productOption.priceStr)
        bindSalesAndRating(productOption)
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

    private fun bindImpressionListener(product: InspirationCarouselViewModel.Option.Product) {
        itemView.productImage?.addOnImpressionListener(product, createViewHintListener(product))
    }

    private fun createViewHintListener(product: InspirationCarouselViewModel.Option.Product): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                inspirationCarouselListener.onImpressedInspirationCarouselListProduct(product)
            }
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

    private fun bindSalesAndRating(product: InspirationCarouselViewModel.Option.Product) {
        bindRatingSalesFloat(product)
        bindTextIntegrityWithSalesRatingFloat(product)
    }

    private fun bindRatingSalesFloat(product: InspirationCarouselViewModel.Option.Product) {
        val willShowSalesRatingFloat = product.willShowRating()

        itemView.optionListCardImageSalesRatingFloat?.showWithCondition(willShowSalesRatingFloat)

        itemView.optionListCardSalesRatingFloat?.shouldShowWithAction(willShowSalesRatingFloat) {
            it.text = product.ratingAverage
        }
    }

    private fun bindTextIntegrityWithSalesRatingFloat(product: InspirationCarouselViewModel.Option.Product) {
        val willShowSalesAndRating = product.willShowSalesAndRating()

        itemView.optionListCardImageSalesRatingFloatLine?.showWithCondition(willShowSalesAndRating)

        itemView.optionListCardTextViewSales?.shouldShowWithAction(willShowSalesAndRating) {
            it.text = MethodChecker.fromHtml(product.getLabelIntegrity()?.title)
        }
    }

    private fun LabelGroupViewModel.toProductCardModelLabelGroup(): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(position = position, title = title, type = type, imageUrl = imageUrl)
    }
}