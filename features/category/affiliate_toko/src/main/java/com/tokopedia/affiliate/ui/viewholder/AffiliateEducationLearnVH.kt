package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateEducationLearnClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationLearnUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationLearnVH(
    itemView: View,
    private val affiliateEducationLearnClickInterface: AffiliateEducationLearnClickInterface?
) : AbstractViewHolder<AffiliateEducationLearnUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_learn_item
    }

    override fun bind(element: AffiliateEducationLearnUiModel?) {
        itemView.findViewById<View>(R.id.bantuan_container).setOnClickListener {
            affiliateEducationLearnClickInterface?.onBantuanClick()
        }
    }
}
