package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class AffiliatePromotionErrorCardItemVH(itemView: View , private val promotionClickInterface: PromotionClickInterface?)
    : AbstractViewHolder<AffiliatePromotionErrorCardModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_promotion_error_item_layout

        const val BUTTON_FILLED = 0
        const val BUTTON_GHOST = 1
        const val BUTTON_TEXT_ONLY = 2
    }

    override fun bind(element: AffiliatePromotionErrorCardModel) {
        itemView.findViewById<Typography>(R.id.error_title).text = element.error.title
        itemView.findViewById<Typography>(R.id.error_msg).text = element.error.message
        itemView.findViewById<ImageUnify>(R.id.error_image).run {
            loadImageWithoutPlaceholder(element.error.errorImage?.android ?: "")
        }

        val buttonIds = arrayListOf(R.id.lihat_kategori,R.id.ganti_link)
        buttonIds.forEach { buttonId ->
            itemView.findViewById<UnifyButton>(buttonId).apply { visibility = View.GONE }
        }
        if(buttonIds.size >= element.error.errorCta?.size ?: 0){
            element.error.errorCta?.forEachIndexed { index, errorCta ->
                itemView.findViewById<UnifyButton>(buttonIds[index]).apply {
                    text = errorCta.ctaText
                    visibility = View.VISIBLE
                    when(errorCta.ctaType){
                        BUTTON_GHOST -> {
                            buttonVariant = UnifyButton.Variant.GHOST
                            buttonType = UnifyButton.Type.ALTERNATE
                        }
                        BUTTON_TEXT_ONLY -> buttonVariant = UnifyButton.Variant.TEXT_ONLY
                        BUTTON_FILLED -> buttonVariant = UnifyButton.Variant.FILLED
                    }
                }
            }
        }
    }
}
