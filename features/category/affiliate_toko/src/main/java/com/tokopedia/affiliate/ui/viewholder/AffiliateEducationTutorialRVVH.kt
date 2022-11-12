package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationTutorialRVVH(
    itemView: View
) : AbstractViewHolder<AffiliateEducationTutorialRVUiModel>(itemView) {

    private var articleTopicAdapter: AffiliateAdapter? = null

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_tutorial_list
    }

    override fun bind(element: AffiliateEducationTutorialRVUiModel?) {
        articleTopicAdapter =
            AffiliateAdapter(AffiliateAdapterFactory())
        val rvArticleTopic = itemView.findViewById<RecyclerView>(R.id.rv_education_tutorial)
        val rvLayoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        rvArticleTopic?.apply {
            layoutManager = rvLayoutManager
            adapter = articleTopicAdapter
        }
        articleTopicAdapter?.addMoreData(
            element?.articleTopicList?.map { AffiliateEducationTutorialUiModel(it) }
        )
    }
}
