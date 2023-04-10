package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.PartialItemOwocAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel

class OwocAddonsViewHolder(itemView: View) : AbstractViewHolder<OwocAddonsListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_owoc_addons_section
    }

    private val partialItemOwocAddonsBinding =
        PartialItemOwocAddonsBinding.bind(this.itemView.findViewById(R.id.owocAddonLayout))

    private var owocPartialProductAddonViewHolder: OwocPartialProductAddonViewHolder? = null

    override fun bind(element: OwocAddonsListUiModel?) {
        if (element == null) return
        owocPartialProductAddonViewHolder =
            OwocPartialProductAddonViewHolder(partialItemOwocAddonsBinding)
        owocPartialProductAddonViewHolder?.bindViews(element)
    }
}
