package com.tokopedia.product.edit.price

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import kotlinx.android.synthetic.main.item_product_category_recommendation.view.*

class ProductCategoryRecommendationAdapter(var categoryRecommendationList: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_category_recommendation, parent, false)
        return ProductCategoryRecommendationViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.labelRadioButton.title = categoryRecommendationList[position]
    }

    override fun getItemCount() = categoryRecommendationList.size

    fun replaceData(categoryRecommendationList: ArrayList<String>) {
        this.categoryRecommendationList = categoryRecommendationList
        notifyDataSetChanged()
    }
}