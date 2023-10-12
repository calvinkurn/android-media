package com.tokopedia.scp_rewards_widgets.cabinetHeader

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.ItemCabinetHeaderBinding

class CabinetHeaderViewHolder(itemView: View) : AbstractViewHolder<CabinetHeader>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_cabinet_header
    }

    private var binding: ItemCabinetHeaderBinding = ItemCabinetHeaderBinding.bind(itemView)

    override fun bind(item: CabinetHeader) {
        binding.viewCabinetHeader.bindData(item)
    }
}
