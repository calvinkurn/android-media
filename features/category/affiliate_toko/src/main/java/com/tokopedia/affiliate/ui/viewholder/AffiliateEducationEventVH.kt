package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class AffiliateEducationEventVH(
    itemView: View,
    private val affiliateEducationEventArticleClickInterface: AffiliateEducationEventArticleClickInterface?
) : AbstractViewHolder<AffiliateEducationEventUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_event_item
    }

    override fun bind(element: AffiliateEducationEventUiModel?) {
        itemView.findViewById<ImageView>(R.id.image_event)
            .loadImage(element?.event?.thumbnail?.android)
        itemView.findViewById<Typography>(R.id.event_category).text =
            element?.event?.categories?.get(0)?.title
        itemView.findViewById<Typography>(R.id.event_title).text = element?.event?.title
        itemView.findViewById<Typography>(R.id.event_date).text = element?.event?.description
        itemView.findViewById<UnifyButton>(R.id.button_event_detail).setOnClickListener {
            affiliateEducationEventArticleClickInterface?.onDetailClick(
                PAGE_EDUCATION_EVENT,
                element?.event?.slug.orEmpty()
            )
        }
    }
}
