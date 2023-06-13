package com.tokopedia.affiliate.ui.viewholder

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE_TOPIC
import com.tokopedia.affiliate.interfaces.AffiliateEducationTopicTutorialClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateEducationArticleTopicVH(
    itemView: View,
    private val affiliateEducationTopicTutorialClickInterface: AffiliateEducationTopicTutorialClickInterface?
) : AbstractViewHolder<AffiliateEducationArticleTopicUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_topic_item

        private const val CORNER_SIZE_16 = 16f
        private const val CORNER_SIZE_64 = 64f
    }

    private val customCornerShape: MaterialShapeDrawable by lazy {
        val shape = ShapeAppearanceModel
            .builder()
            .setTopLeftCornerSize(CORNER_SIZE_16)
            .setTopRightCornerSize(CORNER_SIZE_16)
            .setBottomLeftCornerSize(CORNER_SIZE_16)
            .setBottomRightCornerSize(CORNER_SIZE_64)
            .build()
        MaterialShapeDrawable(shape).apply {
            this.fillColor = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_enabled)
                ),
                intArrayOf(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                )
            )
        }
    }

    override fun bind(element: AffiliateEducationArticleTopicUiModel?) {
        itemView.findViewById<ImageUnify>(R.id.ic_article_topic)
            .loadImage(element?.articleTopic?.icon?.url)
        itemView.findViewById<Typography>(R.id.tv_article_topic).text = element?.articleTopic?.title
        itemView.findViewById<CardView>(R.id.cv_artice_topic).setOnClickListener {
            affiliateEducationTopicTutorialClickInterface?.onCardClick(
                PAGE_EDUCATION_ARTICLE_TOPIC,
                element?.articleTopic?.id.toString()
            )
            sendEducationClickEvent(
                element?.articleTopic?.title,
                element?.articleTopic?.id.toString(),
                AffiliateAnalytics.ActionKeys.CLICK_ARTICLE_CATEGORY,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE
            )
        }
        ViewCompat.setBackground(
            itemView.findViewById<CardView>(R.id.cv_artice_topic),
            customCornerShape
        )
    }

    private fun sendEducationClickEvent(
        creativeName: String?,
        eventId: String?,
        actionKeys: String,
        categoryKeys: String
    ) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            actionKeys,
            categoryKeys,
            eventId,
            position = 0,
            eventId,
            UserSession(itemView.context).userId,
            creativeName
        )
    }
}
