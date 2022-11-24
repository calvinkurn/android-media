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
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateEducationTutorialVH(
    itemView: View,
    private val affiliateEducationTopicTutorialClickInterface: AffiliateEducationTopicTutorialClickInterface?
) : AbstractViewHolder<AffiliateEducationTutorialUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_tutorial_item
    }

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    override fun bind(element: AffiliateEducationTutorialUiModel?) {
        itemView.findViewById<ImageUnify>(R.id.ic_tutorial_topic)
            .loadImage(element?.articleTopic?.icon?.url)
        itemView.findViewById<Typography>(R.id.tv_tutorial_topic).text =
            element?.articleTopic?.title

        if (bindingAdapterPosition == 0) {
            itemView.findViewById<ImageUnify>(R.id.ic_tutorial_topic).hide()
            itemView.findViewById<View>(R.id.tutorial_content_container)
                .setBackgroundResource(R.drawable.affiliate_education_tutorial_cover_gradient)
            itemView.findViewById<View>(R.id.tutorial_cover_bg).show()
            itemView.findViewById<Typography>(R.id.tv_tutorial_topic).apply {
                weightType = Typography.BOLD
                setWeight(Typography.DISPLAY_3)
                setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
            }
        } else {
            itemView.findViewById<CardView>(R.id.cv_tutorial_topic)?.setOnClickListener {
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
    private fun sendEducationClickEvent(creativeName: String?, eventId: String?, actionKeys: String, categoryKeys: String) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            actionKeys,
            categoryKeys,
            eventId,
            position = 0,
            eventId,
            userSessionInterface.userId,
            creativeName
        )
    }
}
