package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateEducationArticleVH(
    itemView: View,
    private val affiliateEducationEventArticleClickInterface: AffiliateEducationEventArticleClickInterface?
) : AbstractViewHolder<AffiliateEducationArticleUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_widget_item
    }

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

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
        itemView.findViewById<View>(R.id.article_widget_container)?.setOnClickListener {
            sendEducationClickEvent(element?.article?.title, element?.article?.articleId.toString())
            affiliateEducationEventArticleClickInterface?.onDetailClick(
                PAGE_EDUCATION_ARTICLE,
                element?.article?.slug.orEmpty()
            )
        }
    }

    private fun sendEducationClickEvent(creativeName: String?, bannerId: String?) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_LATEST_ARTICLE_CARD,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE,
            bannerId,
            position = 0,
            bannerId,
            userSessionInterface.userId,
            creativeName
        )
    }
}
