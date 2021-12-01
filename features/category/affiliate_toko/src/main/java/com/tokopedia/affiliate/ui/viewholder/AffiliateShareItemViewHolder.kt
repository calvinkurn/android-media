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
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

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
            itemView.findViewById<UnifyButton>(R.id.share_button)?.hide()
            itemView.findViewById<CheckboxUnify>(R.id.checkbox)?.run {
                show()
                isChecked = element.isChecked
                setOnCheckedChangeListener { _ , isChecked ->
                    addSocialInterface?.onSocialChecked(adapterPosition,isChecked)
                }
            }
        }else {
            itemView.findViewById<UnifyButton>(R.id.share_button)?.run {
                show()
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
        }

        itemView.findViewById<Typography>(R.id.share_platform)?.text = element?.name
        if(element?.iconId != null) {
            itemView.findViewById<IconUnify>(R.id.share_icon)?.run {
                show()
                setImage(element.iconId)
            }
        } else {
            itemView.findViewById<IconUnify>(R.id.share_icon)?.gone()
        }
    }

}
