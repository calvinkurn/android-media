package com.tokopedia.search.result.product.inspirationcarousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionInfoBinding
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

class InspirationCarouselOptionInfoViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselDataView.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_info
    }
    private var binding: SearchInspirationCarouselOptionInfoBinding? by viewBinding()

    override fun bind(item: InspirationCarouselDataView.Option) {
        val productOption = item.product.getOrNull(0) ?: return

        bindProductImage(productOption.imgUrl)
        bindImpressionListener(productOption)

        bindOptionTitle(item.title)
        bindOnClickListener(item)

        bindProductName(productOption.name)
        bindProductDesc(productOption.description)

        bindCardInteraction()
    }

    private fun bindProductImage(imgUrl: String) {
        val binding = binding ?: return
        binding.optionInfoImage.shouldShowWithAction(imgUrl.isNotEmpty()) {
            binding.optionInfoImage.loadImage(imgUrl)
        }
    }

    private fun bindImpressionListener(product: InspirationCarouselDataView.Option.Product) {
        binding?.optionInfoImage?.addOnImpressionListener(product, createViewHintListener(product))
    }

    private fun createViewHintListener(product: InspirationCarouselDataView.Option.Product): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                inspirationCarouselListener.onImpressedInspirationCarouselInfoProduct(product)
            }
        }
    }

    private fun bindOptionTitle(title: String) {
        val binding = binding ?: return
        binding.optionInfoTitle.shouldShowWithAction(title.isNotEmpty()) {
            binding.optionInfoTitle.text = MethodChecker.fromHtml(title)
        }
    }

    private fun bindProductName(name: String) {
        val binding = binding ?: return
        binding.optionInfoName.shouldShowWithAction(name.isNotEmpty()) {
            binding.optionInfoName.text = MethodChecker.fromHtml(name)
        }
    }

    private fun bindProductDesc(descriptions: List<String>) {
        val binding = binding ?: return
        binding.optionInfoDesc.shouldShowWithAction(descriptions.isNotEmpty()) {
            val desc = descriptions.joinToString(separator = "\n")
            binding.optionInfoDesc.text = desc
        }
    }

    private fun bindOnClickListener(item: InspirationCarouselDataView.Option) {
        binding?.optionInfoCardView?.setOnClickListener { _ ->
            val product = item.product.getOrNull(0) ?: return@setOnClickListener
            inspirationCarouselListener.onInspirationCarouselInfoProductClicked(product)
        }
    }

    private fun bindCardInteraction() {
        binding?.optionInfoCardView?.animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE
    }
}
