package com.tokopedia.checkout.view.feature.emptycart2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecommendationItemUiModel
import com.tokopedia.checkout.view.feature.emptycart2.viewholder.RecommendationItemViewHolder

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class RecommendationAdapter(val listener: ActionListener, val itemWidth: Int) : RecyclerView.Adapter<RecommendationItemViewHolder>() {

    private var recommendationUiModels = ArrayList<RecommendationItemUiModel>()

    fun setData(recommendationUiModels: ArrayList<RecommendationItemUiModel>) {
        this.recommendationUiModels = recommendationUiModels
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(RecommendationItemViewHolder.LAYOUT, parent, false)
        return RecommendationItemViewHolder(view, listener, itemWidth)
    }

    override fun getItemCount(): Int {
        return recommendationUiModels.size
    }

    override fun onBindViewHolder(holder: RecommendationItemViewHolder, position: Int) {
        holder.bind(recommendationUiModels[position])
    }

}