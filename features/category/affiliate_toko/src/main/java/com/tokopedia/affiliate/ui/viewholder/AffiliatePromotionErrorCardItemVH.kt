package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.hide
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
    }

    override fun bind(element: AffiliatePromotionErrorCardModel) {
        itemView.findViewById<Typography>(R.id.error_title).text = element.error.title
        itemView.findViewById<Typography>(R.id.error_msg).text = element.error.message
        itemView.findViewById<ImageUnify>(R.id.error_image).run {
            //loadImageWithoutPlaceholder(element.error.error_cta?.firstOrNull()?.cta_image?.android ?:"")
        }
        // TODO Remove
        val button1 : UnifyButton = itemView.findViewById<UnifyButton>(R.id.lihat_kategori).apply {
            text = element.error.error_cta?.firstOrNull()?.cta_text
        }
        val button2 : UnifyButton = itemView.findViewById<UnifyButton>(R.id.ganti_link).apply { text = "Ganti Link" }
        when(element.error.error_type){
            0 -> {
                button1.hide()
                button2.show()
            }

            1 -> {
                button1.show()
                button2.show()
            }
            2 -> {
                button1.hide()
                button2.show()
            }

        }

    }
}
