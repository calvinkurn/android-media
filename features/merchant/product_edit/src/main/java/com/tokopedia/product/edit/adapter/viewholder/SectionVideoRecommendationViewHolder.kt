package com.tokopedia.product.edit.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.adapter.ProductAddVideoRecommendationFeaturedAdapter
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

class SectionVideoRecommendationViewHolder(itemView: View) : AbstractViewHolder<VideoRecommendationViewModel>(itemView) {

    private lateinit var productAddVideoRecommendationFeaturedAdapter: ProductAddVideoRecommendationFeaturedAdapter
    private lateinit var recyclerView: RecyclerView

    init {
        findViews(itemView)
    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        var list : ArrayList<String> = ArrayList()
        list.add("aw")
        list.add("aw")
        list.add("aw")

        productAddVideoRecommendationFeaturedAdapter = ProductAddVideoRecommendationFeaturedAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = productAddVideoRecommendationFeaturedAdapter
        recyclerView.setHasFixedSize(true)
    }

    override fun bind(videoRecommendationViewModel: VideoRecommendationViewModel) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_section_video_recommendation
    }
}