package com.tokopedia.minicart.common.widget.shoppingsummary.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.databinding.ItemShoppingSummaryHeaderBinding

class ShoppingSummaryHeaderViewHolder(private val viewBinding: ItemShoppingSummaryHeaderBinding) :
    AbstractViewHolder<ShoppingSummaryHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_shopping_summary_header
    }

    override fun bind(element: ShoppingSummaryHeaderUiModel) {
        renderShopIcon(element)
        renderTitle(element)
        renderDescription(element)
    }

    private fun renderShopIcon(element: ShoppingSummaryHeaderUiModel) {
        if (element.iconUrl.isNotBlank()) {
            viewBinding.iuShopBadge.loadImage(element.iconUrl)
        }
    }

    private fun renderTitle(element: ShoppingSummaryHeaderUiModel) {
        viewBinding.tpTitle.text = element.title
    }

    private fun renderDescription(element: ShoppingSummaryHeaderUiModel) {
        with(viewBinding) {
            if (element.description.isNotBlank()) {
                tpDescription.text = element.description
                tpDescription.show()
            } else {
                tpDescription.gone()
            }
        }
    }
}
