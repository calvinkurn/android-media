package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.model.AffiliateSearchData
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class AffiliatePromotionErrorCardItemVH(itemView: View)
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
            loadImageWithoutPlaceholder(element.error.error_cta?.firstOrNull()?.cta_image?.android ?:"")
        }
        itemView.findViewById<UnifyButton>(R.id.lihat_kategori).text = element.error.error_cta?.firstOrNull()?.cta_text
        itemView.findViewById<UnifyButton>(R.id.granti_link).text = element.error.error_cta?.firstOrNull()?.cta_text
    }
}
