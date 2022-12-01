package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateEducationEventRVVH(
    itemView: View,
    private val affiliateEducationEventArticleClickInterface: AffiliateEducationEventArticleClickInterface?
) : AbstractViewHolder<AffiliateEducationEventRVUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_event_list
    }

    private var eventAdapter: AffiliateAdapter = AffiliateAdapter(
        AffiliateAdapterFactory(
            affiliateEducationEventArticleClickInterface = affiliateEducationEventArticleClickInterface
        )
    )
    private var eventData: AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem? =
        null

    private val rvEvent = itemView.findViewById<RecyclerView>(R.id.rv_edu_event)
    private val tvSeeMore = itemView.findViewById<Typography>(R.id.event_lihat_semua)
    private val rvLayoutManager =
        LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)

    override fun bind(element: AffiliateEducationEventRVUiModel?) {
        eventData = element?.event
        tvSeeMore.setOnClickListener {
            affiliateEducationEventArticleClickInterface?.onSeeMoreClick(
                PAGE_EDUCATION_EVENT,
                element?.event?.articles?.getOrNull(0)?.categories?.getOrNull(0)?.id.toString()
            )
            sendEducationClickEvent()
        }
        rvEvent?.apply {
            layoutManager = rvLayoutManager
            adapter = eventAdapter
        }
        eventAdapter.setVisitables(
            element?.event?.articles?.map { AffiliateEducationEventUiModel(it) }
        )
    }

    private fun sendEducationClickEvent() {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_LIHAT_SEMUA_EVENT_CARD,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE,
            userId = UserSession(itemView.context).userId,
            eventLabel = ""
        )
    }
}
