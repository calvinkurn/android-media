package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PATTERN
import com.tokopedia.affiliate.YYYY_MM_DD_HH_MM_SS
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleUiModel
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateEducationArticleVH(
    itemView: View,
    private val affiliateEducationEventArticleClickInterface: AffiliateEducationEventArticleClickInterface?
) : AbstractViewHolder<AffiliateEducationArticleUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_widget_item
    }

    private val imageArticleWidget = itemView.findViewById<ImageUnify>(R.id.image_article_widget)
    private val itemTitle = itemView.findViewById<Typography>(R.id.article_widget_item_title)
    private val itemDetail = itemView.findViewById<Typography>(R.id.article_widget_item_detail)
    private val widgetContainer = itemView.findViewById<View>(R.id.article_widget_container)

    override fun bind(element: AffiliateEducationArticleUiModel?) {
        imageArticleWidget.loadImage(element?.article?.thumbnail?.android)
        itemTitle.text = element?.article?.title
        val readMinute = itemView.context.getString(
            R.string.article_widget_detail_read,
            element?.article?.attributes?.readTime
        )
        itemDetail.text =
            itemView.context.getString(
                R.string.article_widget_detail,
                element?.article?.categories?.get(0)?.title,
                DateUtils().formatDate(
                    currentFormat = YYYY_MM_DD_HH_MM_SS,
                    newFormat = PATTERN,
                    dateString = element?.article?.modifiedDate.orEmpty()
                ),
                readMinute
            )
        widgetContainer?.setOnClickListener {
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
            UserSession(itemView.context).userId,
            creativeName
        )
    }
}
