package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliateEducationTopicTutorialClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationArticleTopicRVVH(
    itemView: View,
    affiliateEducationTopicTutorialClickInterface: AffiliateEducationTopicTutorialClickInterface?
) : AbstractViewHolder<AffiliateEducationArticleTopicRVUiModel>(itemView) {

    private var articleTopicAdapter = AffiliateAdapter(
        AffiliateAdapterFactory(
            affiliateEducationTopicTutorialClickInterface = affiliateEducationTopicTutorialClickInterface
        )
    )
    private val rvArticleTopic =
        itemView.findViewById<RecyclerView>(R.id.rv_education_article_topic)
    private val rvLayoutManager =
        LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_topic_list
    }

    override fun bind(element: AffiliateEducationArticleTopicRVUiModel?) {
        rvArticleTopic?.apply {
            layoutManager = rvLayoutManager
            adapter = articleTopicAdapter
        }
        articleTopicAdapter.setVisitables(
            element?.articleTopicList?.map { AffiliateEducationArticleTopicUiModel(it) }
        )
    }
}
