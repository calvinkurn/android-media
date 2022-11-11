package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateEducationEventVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationEventUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_event_item
    }

    override fun bind(element: AffiliateEducationEventUiModel?) {
        itemView.findViewById<ImageUnify>(R.id.image_event).loadImage(element?.event?.thumbnail?.android)
        itemView.findViewById<Typography>(R.id.event_category).text = element?.event?.categories?.get(0)?.title
        itemView.findViewById<Typography>(R.id.event_title).text = element?.event?.title
        itemView.findViewById<Typography>(R.id.event_date).text = element?.event?.modifiedDate
    }
}
