package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateEduCategoryChipClick
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ChipsUnify

class AffiliateEduCategoryChipVH(
    itemView: View,
    private val affiliateEduCategoryChipClick: AffiliateEduCategoryChipClick?
) : AbstractViewHolder<AffiliateEduCategoryChipModel>(itemView) {

    private val eduCategoryChip: ChipsUnify =
        itemView.findViewById(R.id.chip_edu_category_item)

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_edu_category_chip
    }

    override fun bind(element: AffiliateEduCategoryChipModel?) {
        eduCategoryChip.let {
            it.chipText = element?.chipType?.title
            it.chipType =
                if (element?.chipType?.isSelected == true) ChipsUnify.TYPE_SELECTED
                else ChipsUnify.TYPE_NORMAL
            it.setOnClickListener {
                affiliateEduCategoryChipClick?.onChipClick(element?.chipType)
            }
        }
    }
}
