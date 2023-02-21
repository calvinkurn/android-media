package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliateEducationTopicTutorialClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationTutorialRVVH(
    itemView: View,
    affiliateEducationTopicTutorialClickInterface: AffiliateEducationTopicTutorialClickInterface?
) : AbstractViewHolder<AffiliateEducationTutorialRVUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_tutorial_list
    }

    private val articleTopicAdapter =
        AffiliateAdapter(
            AffiliateAdapterFactory(
                affiliateEducationTopicTutorialClickInterface = affiliateEducationTopicTutorialClickInterface
            )
        )
    private val rvArticleTopic = itemView.findViewById<RecyclerView>(R.id.rv_education_tutorial)
    private val rvLayoutManager =
        LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)

    override fun bind(element: AffiliateEducationTutorialRVUiModel?) {
        rvArticleTopic?.apply {
            layoutManager = rvLayoutManager
            adapter = articleTopicAdapter
        }
        articleTopicAdapter.setVisitables(
            element?.articleTopicList?.map { AffiliateEducationTutorialUiModel(it) }
        )
    }
}
