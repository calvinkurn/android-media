package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.interfaces.AddSocialInterface
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.affiliate_share_item.view.*

class AffiliateShareItemViewHolder(itemView: View, private val shareButtonInterface: ShareButtonInterface?,
                                   private val addSocialInterface: AddSocialInterface?)
    : AbstractViewHolder<AffiliateShareModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_share_item
    }

    override fun bind(element: AffiliateShareModel?) {
        if(element?.type == AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL){
            itemView.checkbox.show()
            itemView.share_button.hide()
            itemView.checkbox.isChecked = element.isChecked
            itemView.checkbox.setOnCheckedChangeListener { _ , isChecked ->
                addSocialInterface?.onSocialChecked(adapterPosition,isChecked)
            }
        }else {
            val iconCopyGreen = getIconUnifyDrawable(itemView.context, IconUnify.COPY, MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            itemView.share_button.run {
                show()
                itemView.checkbox.hide()
                setDrawable(iconCopyGreen)
                isLoading = element?.buttonLoad == true
                setOnClickListener {
                    isLoading = true
                    shareButtonInterface?.onShareButtonClick(element?.name,element?.id, element?.serviceFormat)
                }
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
