package com.tokopedia.product.manage.item.price

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.price.model.ProductCategory
import kotlinx.android.synthetic.main.item_product_category_recommendation.view.*

class ProductCategoryRecommendationAdapter(private val categoryRecommendationList: MutableList<ProductCategory>,
                                           var listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var selectedPosition = -1

    interface Listener {
        fun onCategoryRecommendationChoosen(productCategory: ProductCategory)
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_category_recommendation, parent, false)
        return ProductCategoryRecommendationViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.titleTextView.text = categoryRecommendationList[position].categoryName
        holder.itemView.radioButtonStatus.isChecked = selectedPosition == position
    }

    override fun getItemCount() = categoryRecommendationList.size

    override fun getItemId(position: Int): Long {
        return categoryRecommendationList[position].categoryId.toLong()
    }

    fun replaceData(categoryRecommendationList: List<ProductCategory>) {
        this.categoryRecommendationList.clear()
        this.categoryRecommendationList.addAll(categoryRecommendationList)
        notifyDataSetChanged()
    }

    fun getSelectedCategory(): ProductCategory{
        return categoryRecommendationList[selectedPosition]
    }

    fun setSelectedCategory(productCategory: ProductCategory){
        for ((index, value) in categoryRecommendationList.withIndex()){
            if(value.categoryName == productCategory.categoryName){
                selectedPosition = index
                notifyDataSetChanged()
                break
            }
        }
    }

    inner class ProductCategoryRecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            setView()
        }
        fun setView(){
            itemView.setOnClickListener({
                selectedPosition = adapterPosition
                notifyDataSetChanged()
                listener.onCategoryRecommendationChoosen(getSelectedCategory())
            })
        }
    }
}