package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateEducationArticleVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationArticleUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_widget_item
    }

    override fun bind(element: AffiliateEducationArticleUiModel?) {
        itemView.findViewById<ImageUnify>(R.id.image_article_widget)
            .loadImage(element?.article?.thumbnail?.android)
        itemView.findViewById<Typography>(R.id.article_widget_item_title).text =
            element?.article?.title
        itemView.findViewById<Typography>(R.id.article_widget_item_detail).text =
            itemView.context.getString(
                R.string.article_widget_detail,
                element?.article?.categories?.get(0)?.title,
                element?.article?.modifiedDate,
                element?.article?.attributes?.readTime
            )
    }
}
