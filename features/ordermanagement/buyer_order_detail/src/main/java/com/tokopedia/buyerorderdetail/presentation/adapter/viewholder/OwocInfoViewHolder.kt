package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemOwocInfoBinding
import com.tokopedia.buyerorderdetail.presentation.model.OwocBomDetailSectionUiModel
import com.tokopedia.media.loader.loadImage

class OwocInfoViewHolder(
    view: View
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
            setOWOCSectionClickListener()
        }
    }

    private fun ItemOwocInfoBinding.setOWOCSectionClickListener() {
        root.setOnClickListener {
        }
        icOWOCSectionChevronRight.setOnClickListener {
        }
    }

    private fun ItemOwocInfoBinding.setOWOCSectionTitle(title: String) {
        tvOwOCSectionTitle.text = MethodChecker.fromHtml(title)
    }

    private fun ItemOwocInfoBinding.setOWOCSectionDesc(desc: String) {
        tvOwOCSectionDesc.text = desc
    }

    private fun ItemOwocInfoBinding.setOWOCSectionImage(imageUrl: String) {
        ivOWOCSection.loadImage(imageUrl)
    }
}
