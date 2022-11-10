package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateEducationArticleTopicVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationArticleTopicUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_topic_item
    }

    override fun bind(element: AffiliateEducationArticleTopicUiModel?) {
        itemView.findViewById<ImageUnify>(R.id.ic_article_topic)
            .loadImage(element?.articleTopic?.url)
        itemView.findViewById<Typography>(R.id.tv_article_topic).text = element?.articleTopic?.title
    }
}
