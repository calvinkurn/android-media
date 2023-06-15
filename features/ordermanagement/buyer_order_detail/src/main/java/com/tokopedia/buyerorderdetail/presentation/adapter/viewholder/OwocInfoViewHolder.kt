package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemOwocInfoBinding
import com.tokopedia.buyerorderdetail.presentation.model.OwocBomDetailSectionUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class OwocInfoViewHolder(
    view: View,
    private val listener: Listener
) : AbstractViewHolder<OwocBomDetailSectionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_owoc_info
    }

    private val binding = ItemOwocInfoBinding.bind(itemView)

    override fun bind(element: OwocBomDetailSectionUiModel) {
        with(binding) {
            setOWOCSectionImage(element.imageUrl)
            setOWOCSectionTitle(element.sectionTitle)
            setOWOCSectionDesc(element.sectionDesc)
            setOWOCSectionClickListener(element)
        }
    }

    private fun ItemOwocInfoBinding.setOWOCSectionClickListener(element: OwocBomDetailSectionUiModel) {
        root.setOnClickListener {
            listener.onOwocInfoClicked(element.txId)
        }
        icOWOCSectionChevronRight.setOnClickListener {
            listener.onOwocInfoClicked(element.txId)
        }
    }

    private fun ItemOwocInfoBinding.setOWOCSectionTitle(title: String) {
        tvOwOCSectionTitle.text = MethodChecker.fromHtml(title)
    }

    private fun ItemOwocInfoBinding.setOWOCSectionDesc(desc: String) {
        tvOwOCSectionDesc.text = desc
    }

    private fun ItemOwocInfoBinding.setOWOCSectionImage(imageUrl: String) {
        if (imageUrl.isNotBlank()) {
            ivOWOCSection.show()
            ivOWOCSection.loadImage(imageUrl)
        } else {
            ivOWOCSection.hide()
        }
    }

    interface Listener {
        fun onOwocInfoClicked(txId: String)
    }
}
