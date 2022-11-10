package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationArticleTopicRVVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationArticleTopicRVUiModel>(itemView) {

    private lateinit var articleTopicAdapter: AffiliateAdapter

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_article_topic_list
    }

    override fun bind(element: AffiliateEducationArticleTopicRVUiModel?) {
        articleTopicAdapter =
            AffiliateAdapter(AffiliateAdapterFactory())
        val rvArticleTopic = itemView.findViewById<RecyclerView>(R.id.rv_education_article_topic)
        val rvLayoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        rvArticleTopic?.apply {
            layoutManager = rvLayoutManager
            adapter = articleTopicAdapter
        }
        articleTopicAdapter.addMoreData(
            element?.articleTopicList?.map { AffiliateEducationArticleUiModel(it) }
        )
    }
}
