package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationLearnUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationLearnVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationLearnUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_learn_item
    }

    override fun bind(element: AffiliateEducationLearnUiModel?) {

    }
}
