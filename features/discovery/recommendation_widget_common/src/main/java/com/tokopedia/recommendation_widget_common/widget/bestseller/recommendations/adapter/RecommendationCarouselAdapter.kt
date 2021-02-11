package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory.RecommendationCarouselTypeFactory

/**
 * Created by Lukas on 05/11/20.
 */
class RecommendationCarouselAdapter (private val typeFactory: RecommendationCarouselTypeFactory)
    : RecyclerView.Adapter<AbstractViewHolder<Visitable<RecommendationCarouselTypeFactory>>>(){
    private val listData = mutableListOf<Visitable<RecommendationCarouselTypeFactory>>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<RecommendationCarouselTypeFactory>> {
        val view: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<RecommendationCarouselTypeFactory>>, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.count()

    override fun getItemViewType(position: Int): Int {
        return listData[position].type(typeFactory)
    }

    fun submitList(list: List<Visitable<RecommendationCarouselTypeFactory>>){
        val diffCallback = RecommendationCarouselDiffUtil(listData.toMutableList(), list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listData.clear()
        listData.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}