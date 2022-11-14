package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.interfaces.AffiliateEducationSeeAllCardClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateEducationSeeAllVH(
    itemView: View,
    private val seeAllCardClickInterface: AffiliateEducationSeeAllCardClickInterface?
) : AbstractViewHolder<AffiliateEducationSeeAllUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_see_all_item
    }

    override fun bind(element: AffiliateEducationSeeAllUiModel?) {
        itemView.findViewById<ImageUnify>(R.id.image_see_all_result)
            .loadImage(element?.article?.thumbnail?.android)
        itemView.findViewById<Typography>(R.id.see_all_item_title).text =
            element?.article?.title
        itemView.findViewById<Typography>(R.id.see_all_item_detail).text =
            when (element?.pageType) {
                PAGE_EDUCATION_EVENT -> {
                    itemView.context.getString(
                        R.string.see_all_event_widget_detail,
                        element.article?.categories?.get(0)?.title,
                        element.article?.modifiedDate
                    )
                }
                PAGE_EDUCATION_ARTICLE -> {
                    itemView.context.getString(
                        R.string.article_widget_detail,
                        element.article?.categories?.get(0)?.title,
                        element.article?.modifiedDate,
                        element.article?.attributes?.readTime
                    )
                }
                else -> ""
            }
        itemView.findViewById<View>(R.id.see_all_container)?.setOnClickListener {
            seeAllCardClickInterface?.onCardClick(
                element?.pageType.orEmpty(),
                element?.article?.slug.orEmpty()
            )
        }
    }
}
