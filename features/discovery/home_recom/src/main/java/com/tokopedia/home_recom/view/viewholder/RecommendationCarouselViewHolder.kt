package com.tokopedia.home_recom.view.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationCarouselViewHolder(view: View) : AbstractViewHolder<RecommendationCarouselDataModel>(view) {

    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }
    private val list = mutableListOf<RecommendationCarouselItemDataModel>()
    override fun bind(element: RecommendationCarouselDataModel) {
        title.text = element.title
        setupRecyclerView(element)
    }

    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel){
        list.addAll(dataModel.products)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = object : RecyclerView.Adapter<RecommendationCarouselItemViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCarouselItemViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(RecommendationCarouselItemDataModel.LAYOUT, parent, false)
                return RecommendationCarouselItemViewHolder(view)
            }

            override fun getItemCount(): Int = list.size

            override fun onBindViewHolder(holder: RecommendationCarouselItemViewHolder, position: Int) {
                holder.bind(list[position])
            }
        }
    }

    fun updateWishlist(position: Int, isWishlist: Boolean){
        list[position].productItem.isWishlist = isWishlist
        recyclerView.adapter?.notifyItemChanged(position)
    }
}