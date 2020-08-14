package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import kotlinx.android.synthetic.main.search_inspiration_carousel_option_info.view.*

class InspirationCarouselOptionInfoViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselViewModel.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_info
    }

    override fun bind(item: InspirationCarouselViewModel.Option) {
        val productOption = item.product.getOrNull(0) ?: return

        bindProductImage(productOption.imgUrl)

        bindOptionTitle(item.title)
        bindOnClickListener(item)

        bindProductName(productOption.name)
        bindProductDesc(productOption.description)
    }

    private fun bindProductImage(imgUrl: String) {
        itemView.optionInfoImage?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(itemView.context, itemView.optionInfoImage, imgUrl)
        }
    }

    private fun bindOptionTitle(title: String) {
        itemView.optionInfoTitle?.shouldShowWithAction(title.isNotEmpty()) {
            itemView.optionInfoTitle?.text = MethodChecker.fromHtml(title)
        }
    }

    private fun bindProductName(name: String) {
        itemView.optionInfoName?.shouldShowWithAction(name.isNotEmpty()) {
            itemView.optionInfoName?.text = MethodChecker.fromHtml(name)
        }
    }

    private fun bindProductDesc(descriptions: List<String>) {
        itemView.optionInfoDesc?.shouldShowWithAction(descriptions.isNotEmpty()) {
            val desc = descriptions.joinToString(separator = "\n")
            itemView.optionInfoDesc?.text = desc
        }
    }

    private fun bindOnClickListener(item: InspirationCarouselViewModel.Option) {
        itemView.optionInfoCardView?.setOnClickListener { _ ->
            val product = item.product.getOrNull(0) ?: return@setOnClickListener
            inspirationCarouselListener.onInspirationCarouselInfoProductClicked(product)
        }
    }
}