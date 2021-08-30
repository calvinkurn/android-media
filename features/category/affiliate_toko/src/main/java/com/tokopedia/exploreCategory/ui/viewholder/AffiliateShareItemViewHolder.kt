package com.tokopedia.exploreCategory.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate_toko.R
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShareVHViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.affiliate_share_item.view.*

class AffiliateShareItemViewHolder(itemView: View)
    : AbstractViewHolder<AffiliateShareVHViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_share_item
    }

    override fun bind(element: AffiliateShareVHViewModel?) {
        val iconCopyGreen = getIconUnifyDrawable(itemView.context, IconUnify.COPY, MethodChecker.getColor(itemView.context, R.color.Unify_GN500))
        itemView.share_button.setDrawable(iconCopyGreen)
        itemView.share_platform.text = element?.name
        if(element?.iconId!=null) {
            itemView.share_icon.setImage(element.iconId, newLightEnable = MethodChecker.getColor(itemView.context, R.color.Unify_NN950))
        } else {
            itemView.share_icon.gone()
        }
        setObserver()
    }

    private fun setObserver() {

    }
}
