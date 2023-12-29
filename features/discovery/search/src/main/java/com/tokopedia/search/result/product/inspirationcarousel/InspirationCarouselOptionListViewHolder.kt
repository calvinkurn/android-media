package com.tokopedia.search.result.product.inspirationcarousel

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionListBinding
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class InspirationCarouselOptionListViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselDataView.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_list
    }
    private var binding: SearchInspirationCarouselOptionListBinding? by viewBinding()

    override fun bind(item: InspirationCarouselDataView.Option) {
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
        binding?.optionTitle?.shouldShowWithAction(title.isNotEmpty()) {
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

    private fun bindOnClickListener(item: InspirationCarouselDataView.Option) {
        val binding = binding ?: return
        binding.viewAllOption.setOnClickListener { _ ->
            inspirationCarouselListener.onInspirationCarouselSeeAllClicked(item)
        }
        binding.optionListCardView.setOnClickListener { _ ->
            val product = item.product.getOrNull(0) ?: return@setOnClickListener
            inspirationCarouselListener.onInspirationCarouselListProductClicked(product)
        }
    }

    private fun bindImpressionListener(product: InspirationCarouselDataView.Option.Product) {
        binding?.productImage?.addOnImpressionListener(product, createViewHintListener(product))
    }

    private fun createViewHintListener(product: InspirationCarouselDataView.Option.Product): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                inspirationCarouselListener.onInspirationCarouselListProductImpressed(product)
            }
        }
    }

    private fun bindProductImage(imgUrl: String) {
        binding?.productImage?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            it.loadImage(imgUrl)
        }
    }

    private fun bindProductName(productName: String) {
        binding?.productName?.shouldShowWithAction(productName.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(productName)
        }
    }

    private fun bindProductPrice(productPrice: String) {
        binding?.productPrice?.shouldShowWithAction(productPrice.isNotEmpty()) {
            it.text = productPrice
        }
    }

    private fun bindSalesAndRating(product: InspirationCarouselDataView.Option.Product) {
        bindRatingSalesFloat(product)
        bindTextIntegrityWithSalesRatingFloat(product)
    }

    private fun bindRatingSalesFloat(product: InspirationCarouselDataView.Option.Product) {
        val binding = binding ?: return
        val willShowSalesRatingFloat = product.willShowRating()

        binding.optionListCardImageSalesRatingFloat.showWithCondition(willShowSalesRatingFloat)

        binding.optionListCardSalesRatingFloat.shouldShowWithAction(willShowSalesRatingFloat) {
            it.text = product.ratingAverage
        }
    }

    private fun bindTextIntegrityWithSalesRatingFloat(product: InspirationCarouselDataView.Option.Product) {
        val binding = binding ?: return
        val labelGroupViewModel = product.getLabelIntegrity()

        binding.optionListCardImageSalesRatingFloatLine.showWithCondition(product.willShowSalesAndRating())

        binding.optionListCardTextViewSales.initLabelGroup(labelGroupViewModel)
    }

    private fun Typography.initLabelGroup(labelGroupData: LabelGroupDataView?) {
        if (labelGroupData == null) hide()
        else showTypography(labelGroupData)
    }

    private fun Typography.showTypography(labelGroupDataView: LabelGroupDataView) {
        shouldShowWithAction(labelGroupDataView.title.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(labelGroupDataView.title)
            it.setTextColor(labelGroupDataView.type.toUnifyTextColor(context))
        }
    }

    private fun String?.toUnifyTextColor(context: Context): Int {
        return try{
            when(this) {
                SearchConstant.ProductCardLabel.TEXT_DARK_ORANGE -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400)
                SearchConstant.ProductCardLabel.TEXT_DARK_RED -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                SearchConstant.ProductCardLabel.LABEL_INTEGRITY_TYPE -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
                SearchConstant.ProductCardLabel.TEXT_LIGHT_GREY -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44)
                else -> Color.parseColor(this)
            }
        } catch (throwable: Throwable){
            Timber.w(throwable)
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        }
    }
}
