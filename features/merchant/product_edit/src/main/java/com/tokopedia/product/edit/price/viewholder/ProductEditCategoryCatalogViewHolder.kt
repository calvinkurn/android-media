package com.tokopedia.product.edit.price.viewholder

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.tokopedia.product.edit.price.ProductCategoryRecommendationAdapter
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel
import kotlinx.android.synthetic.main.partial_product_edit_category.view.*

class ProductEditCategoryCatalogViewHolder(var view: View, var listener: Listener, context: Context?): ProductCategoryRecommendationAdapter.Listener{

    private val categoryRecommendationList = mutableListOf<ProductCategory>()
    private val productCategoryRecommendationAdapter: ProductCategoryRecommendationAdapter

    interface Listener {
        fun onCategoryRecommendationChoosen(productCategory: ProductCategory)
    }

    init {
        productCategoryRecommendationAdapter = ProductCategoryRecommendationAdapter(categoryRecommendationList, this)
        view.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.adapter = productCategoryRecommendationAdapter
        view.recyclerView.setHasFixedSize(true)
        view.recyclerView.isNestedScrollingEnabled = false
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        listener.onCategoryRecommendationChoosen(productCategory)
    }

    fun setCategoryChosen(productCategory: ProductCategory){
        if (!TextUtils.isEmpty(productCategory.categoryName)) {
            view.labelCategory.setContent(productCategory.categoryName)
        }
        productCategoryRecommendationAdapter.setSelectedCategory(productCategory)
    }

    fun setCatalogChosen(productCatalog: ProductCatalog){
        if(!TextUtils.isEmpty(productCatalog.catalogName)) {
            view.labelCatalog.setContent(productCatalog.catalogName)
        }
    }

    fun renderRecommendation(categories: List<ProductCategoryPredictionViewModel>){
        productCategoryRecommendationAdapter.replaceData(categories.map {ProductCategory().apply {
            categoryId = it.lastCategoryId
            categoryName = it.printedString
        }})
    }

    fun setVisiblityCatalog(isVisible: Boolean) {
        if(isVisible){
            view.titleCatalog.visibility = View.VISIBLE
            view.labelCatalog.visibility = View.VISIBLE
        }else{
            view.titleCatalog.visibility = View.GONE
            view.labelCatalog.visibility = View.GONE
        }
    }
}