package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_TUTORIAL
import com.tokopedia.affiliate.interfaces.AffiliateEducationTopicTutorialClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateEducationTutorialVH(
    itemView: View,
    private val affiliateEducationTopicTutorialClickInterface: AffiliateEducationTopicTutorialClickInterface?
) : AbstractViewHolder<AffiliateEducationTutorialUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_tutorial_item
    }

    private val tutorialIcon = itemView.findViewById<ImageUnify>(R.id.ic_tutorial_topic)
    private val tutorialTopic = itemView.findViewById<Typography>(R.id.tv_tutorial_topic)
    private val tutorialContentContainer =
        itemView.findViewById<View>(R.id.tutorial_content_container)
    private val tutorialCover = itemView.findViewById<View>(R.id.tutorial_cover_bg)
    private val tutorialContainer = itemView.findViewById<CardView>(R.id.cv_tutorial_topic)

    override fun bind(element: AffiliateEducationTutorialUiModel?) {
        tutorialIcon.loadImage(element?.articleTopic?.icon?.url)
        tutorialTopic.text = element?.articleTopic?.title

        if (bindingAdapterPosition == 0) {
            tutorialIcon.hide()
            tutorialCover.show()
            tutorialTopic.apply {
                weightType = Typography.BOLD
                setWeight(Typography.DISPLAY_3)
                text = getString(R.string.affiliate_education_tutorial_cover_title)
                setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0
                    )
                )
            }
        } else {
            tutorialContainer.setOnClickListener {
                affiliateEducationTopicTutorialClickInterface?.onCardClick(
                    PAGE_EDUCATION_TUTORIAL,
                    element?.articleTopic?.id.toString()
                )
                sendEducationClickEvent(
                    element?.articleTopic?.title,
                    element?.articleTopic?.id.toString(),
                    AffiliateAnalytics.ActionKeys.CLICK_TUTORIAL_CATEGORY,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE
                )
            }
        }
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
