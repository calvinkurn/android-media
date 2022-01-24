package com.tokopedia.search.result.product.violation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultViolationProductsEmptySearchLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

class ViolationViewHolder(
    itemView: View,
    private val violationListener: ViolationListener
) : AbstractViewHolder<ViolationDataView>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_violation_products_empty_search_layout
    }
    private var binding: SearchResultViolationProductsEmptySearchLayoutBinding? by viewBinding()

    override fun bind(element: ViolationDataView?) {
        val binding = binding?: return
        if (element == null) return

        binding.searchResultViolationProductsEmptySearchTitle.text = element.headerText
        binding.searchResultViolationProductsEmptySearchMessage.text = element.descriptionText
        binding.searchResultViolationProductsEmptySearchImage.loadImage(element.imageUrl)
        bindViolationButton(binding, element.violationButton)
    }

    private fun bindViolationButton(
        binding: SearchResultViolationProductsEmptySearchLayoutBinding,
        violationButton: ViolationButtonDataView
    ) {
        if(violationButton.isVisible) {
            binding.buttonSearchResultViolationProductsEmpty.apply{
                text = violationButton.text
                buttonType = violationButton.type
                buttonVariant = violationButton.variant
                visible()
                setOnClickListener {
                    violationListener.onViolationButtonClick(violationButton.ctaUrl)
                }
            }
        } else {
            binding.buttonSearchResultViolationProductsEmpty.gone()
        }
    }
}