package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.affiliate_share_item.view.*

class AffiliateShareItemViewHolder(itemView: View, private val shareButtonInterface: ShareButtonInterface?)
    : AbstractViewHolder<AffiliateShareModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_share_item
    }

    override fun bind(element: AffiliateShareModel?) {
        itemView.share_button.run {
            isLoading = element?.buttonLoad == true
            if (element?.isLinkGenerationEnabled == true){
                val iconCopyGreen = getIconUnifyDrawable(itemView.context, IconUnify.COPY, MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                setDrawable(iconCopyGreen)
                buttonType = UnifyButton.Type.MAIN
                buttonVariant = UnifyButton.Variant.GHOST
                setOnClickListener {
                    isLoading = true
                    shareButtonInterface?.onShareButtonClick(element.name, element.id, element.serviceFormat)
                }
            }else {
                val iconCopyGray = getIconUnifyDrawable(itemView.context, IconUnify.COPY, MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN300))
                setDrawable(iconCopyGray)
                buttonType = UnifyButton.Type.ALTERNATE
                buttonVariant = UnifyButton.Variant.GHOST
                setOnClickListener(null)
            }
        }
        itemView.share_platform.text = element?.name
        if(element?.iconId != null) {
            itemView.share_icon.show()
            itemView.share_icon.setImage(element.iconId)
        } else {
            itemView.share_icon.gone()
        }
    }

}
