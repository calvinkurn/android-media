package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.databinding.ItemPaidShippingTitleBinding
import com.tokopedia.logisticcart.shipping.model.PaidSectionInfoUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper

class PaidShippingInfoViewHolder(val binding: ItemPaidShippingTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val LAYOUT = 6
    }

    fun bindData(data: PaidSectionInfoUiModel, listener: ShippingDurationAdapterListener?) {
        binding.run {
            if (data.showCollapsableIcon) {
                if (data.isCollapsed) {
                    iconPaidShippingCollapse.setImage(IconUnify.CHEVRON_UP)
                } else {
                    iconPaidShippingCollapse.setImage(IconUnify.CHEVRON_DOWN)
                }
                iconPaidShippingCollapse.setOnClickListener {
                    listener?.onCollapseClicked(!data.isCollapsed)
                }
                iconPaidShippingCollapse.visible()
            } else {
                iconPaidShippingCollapse.gone()
            }

            if (data.title.isNotEmpty()) {
                tvPaidShippingTitle.text = HtmlLinkHelper(root.context, data.title).spannedString
                tvPaidShippingTitle.visible()
            } else {
                tvPaidShippingTitle.gone()
            }
        }
    }
}
