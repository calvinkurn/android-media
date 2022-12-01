package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateEducationArticleRVVH(
    itemView: View,
    private val affiliateEducationEventArticleClickInterface: AffiliateEducationEventArticleClickInterface?
) : AbstractViewHolder<AffiliateEducationArticleRVUiModel>(itemView) {

    private var articleAdapter = AffiliateAdapter(
        AffiliateAdapterFactory(
            affiliateEducationEventArticleClickInterface = affiliateEducationEventArticleClickInterface
        )
    )

    private val rvLayoutManager =
        LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
    private val rvArticle = itemView.findViewById<RecyclerView>(R.id.rv_article_widget)
    private val tvSeeMore = itemView.findViewById<Typography>(R.id.article_widget_lihat_semua)

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_widget_list
    }

    override fun bind(element: AffiliateEducationArticleRVUiModel?) {
        tvSeeMore.setOnClickListener {
            affiliateEducationEventArticleClickInterface?.onSeeMoreClick(
                PAGE_EDUCATION_ARTICLE,
                element?.article?.articles?.getOrNull(0)?.categories?.getOrNull(0)?.id.toString()
            )
            sendEducationClickEvent()
        }
        rvArticle?.apply {
            layoutManager = rvLayoutManager
            adapter = articleAdapter
        }
        articleAdapter.setVisitables(
            element?.article?.articles?.map { AffiliateEducationArticleUiModel(it) }
        )
    }

    private fun sendEducationClickEvent() {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_LIHAT_SEMUA_LATEST_ARTICLE_CARD,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE,
            userId = UserSession(itemView.context).userId,
            eventLabel = ""
        )
    }
}
