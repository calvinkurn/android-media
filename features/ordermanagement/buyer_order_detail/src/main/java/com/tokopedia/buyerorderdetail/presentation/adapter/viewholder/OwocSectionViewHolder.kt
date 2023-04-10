package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemOwocSectionBinding
import com.tokopedia.buyerorderdetail.presentation.model.OwocBomDetailSectionUiModel
import com.tokopedia.media.loader.loadImage

class OwocSectionViewHolder(
    view: View
): AbstractViewHolder<OwocBomDetailSectionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_owoc_section
    }

    private val binding = ItemOwocSectionBinding.bind(itemView)

    override fun bind(element: OwocBomDetailSectionUiModel) {
        with(binding) {
            setOWOCSectionImage(element.imageUrl)
            setOWOCSectionTitle(element.sectionTitle)
            setOWOCSectionDesc(element.sectionDesc)
            setOWOCSectionClickListener()
        }
    }

    private fun ItemOwocSectionBinding.setOWOCSectionClickListener() {
        root.setOnClickListener {

        }
        icOWOCSectionChevronRight.setOnClickListener {

        }
    }

    private fun ItemOwocSectionBinding.setOWOCSectionTitle(title: String) {
        tvOwOCSectionTitle.text = title
    }

    private fun ItemOwocSectionBinding.setOWOCSectionDesc(desc: String) {
        tvOwOCSectionDesc.text = desc
    }

    private fun ItemOwocSectionBinding.setOWOCSectionImage(imageUrl: String) {
        ivOWOCSection.loadImage(imageUrl)
    }
}
