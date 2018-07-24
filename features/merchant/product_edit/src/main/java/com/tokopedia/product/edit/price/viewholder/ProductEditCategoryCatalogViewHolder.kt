package com.tokopedia.product.edit.price.viewholder

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.product.edit.price.ProductCategoryRecommendationAdapter
import kotlinx.android.synthetic.main.partial_product_edit_category.view.*

class ProductEditCategoryCatalogViewHolder(var view: View, var listener: Listener, context: Context?): ProductCategoryRecommendationAdapter.Listener{

    interface Listener {
        fun onCategoryRecommendationChoosen(value: String)
        fun onLabelCatalogClicked()
        fun onLabelCategoryClicked()
    }

    init {
        view.labelCatalog.setOnClickListener { listener.onLabelCatalogClicked() }

        val categoryRecommendationList = ArrayList<String>()
        categoryRecommendationList.add("a")
        categoryRecommendationList.add("b")
        categoryRecommendationList.add("c")
        val productCategoryRecommendationAdapter = ProductCategoryRecommendationAdapter(categoryRecommendationList, this)
        view.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.adapter = productCategoryRecommendationAdapter
        view.recyclerView.setHasFixedSize(true)
        view.recyclerView.isNestedScrollingEnabled = false
    }

    override fun onCategoryRecommendationChoosen(value: String) {
        listener.onCategoryRecommendationChoosen(value)
    }

    fun setCategoryChosen(value: String){
        view.labelCategory.setContent(value)
    }

    fun setCatalogChosen(value: String){
        view.labelCatalog.setContent(value)
    }

    companion object {
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2
    }
}