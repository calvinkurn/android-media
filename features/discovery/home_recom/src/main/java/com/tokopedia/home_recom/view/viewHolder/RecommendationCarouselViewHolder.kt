package com.tokopedia.home_recom.view.viewHolder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.dataModel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationItemDataModel
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationCarouselViewHolder(view: View) : AbstractViewHolder<RecommendationCarouselDataModel>(view) {

    private val title: TextView by lazy { view.findViewById<TextView>(R.id.product_name) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }

    override fun bind(element: RecommendationCarouselDataModel) {
        title.text = element.title
        setupRecyclerView(element)
    }

    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel){
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = object : RecyclerView.Adapter<RecommendationItemViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationItemViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                return RecommendationItemViewHolder(view)
            }

            override fun getItemCount(): Int = dataModel.products.size

            override fun onBindViewHolder(holder: RecommendationItemViewHolder, position: Int) {
                holder.bind(RecommendationItemDataModel(dataModel.products[position], dataModel.listener))
            }
        }
    }
}