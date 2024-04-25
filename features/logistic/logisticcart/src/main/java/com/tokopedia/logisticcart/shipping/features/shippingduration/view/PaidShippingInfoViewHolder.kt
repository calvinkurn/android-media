package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.logisticcart.databinding.ItemPaidShippingTitleBinding
import com.tokopedia.logisticcart.shipping.model.PaidSectionInfoUiModel

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
                    iconPaidShippingCollapse.setImage(IconUnify.CHEVRON_DOWN)
                } else {
                    iconPaidShippingCollapse.setImage(IconUnify.CHEVRON_UP)
                }
                iconPaidShippingCollapse.setOnClickListener {
                    listener?.onCollapseClicked(!data.isCollapsed)
                }
            } else {
                iconPaidShippingCollapse.gone()
            }
        }
    }
}
