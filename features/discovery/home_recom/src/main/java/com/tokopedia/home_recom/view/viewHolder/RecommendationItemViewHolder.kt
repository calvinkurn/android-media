package com.tokopedia.home_recom.view.viewHolder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.dataModel.ProductDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationScrollDataModel

class RecommendationItemViewHolder(
       private val view: View
) : AbstractViewHolder<RecommendationScrollDataModel>(view){

    private val title: TextView by lazy { view.findViewById<TextView>(R.id.product_name) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }

    override fun bind(element: RecommendationScrollDataModel) {
        title.text = element.title
        setupRecyclerView(element.products)
    }

    private fun setupRecyclerView(list: List<ProductDataModel>){
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

    }
}