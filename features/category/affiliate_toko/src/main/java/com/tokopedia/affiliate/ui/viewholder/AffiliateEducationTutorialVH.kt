package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateEducationTutorialVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationTutorialUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_tutorial_item
    }

    override fun bind(element: AffiliateEducationTutorialUiModel?) {
        itemView.findViewById<ImageUnify>(R.id.ic_tutorial_topic)
            .loadImage(element?.articleTopic?.url)
        itemView.findViewById<Typography>(R.id.tv_tutorial_topic).text = element?.articleTopic?.title
    }
}
