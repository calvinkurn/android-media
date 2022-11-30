package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateEducationEventVH(
    itemView: View,
    private val affiliateEducationEventArticleClickInterface: AffiliateEducationEventArticleClickInterface?
) : AbstractViewHolder<AffiliateEducationEventUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_event_item
    }

    private val imageEvent = itemView.findViewById<ImageView>(R.id.image_event)
    private val eventCategory = itemView.findViewById<Typography>(R.id.event_category)
    private val eventTitle = itemView.findViewById<Typography>(R.id.event_title)
    private val eventDate = itemView.findViewById<Typography>(R.id.event_date)
    private val eventDetail = itemView.findViewById<UnifyButton>(R.id.button_event_detail)

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    override fun bind(element: AffiliateEducationEventUiModel?) {
        imageEvent.loadImage(element?.event?.thumbnail?.android)
        eventCategory.text = element?.event?.categories?.get(0)?.title
        eventTitle.text = element?.event?.title
        eventDate.text = element?.event?.description
        eventDetail.setOnClickListener {
            sendEducationClickEvent(element?.event?.title, element?.event?.articleId.toString())
            affiliateEducationEventArticleClickInterface?.onDetailClick(
                PAGE_EDUCATION_EVENT,
                element?.event?.slug.orEmpty()
            )
        }
    }

    private fun sendEducationClickEvent(creativeName: String?, eventId: String?) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_EVENT_CARD,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE,
            eventId,
            position = 0,
            eventId,
            userSessionInterface.userId,
            creativeName
        )
    }
}
