package com.tokopedia.product.edit.price.viewholder

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.product.edit.price.ProductCategoryRecommendationAdapter
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import kotlinx.android.synthetic.main.partial_product_edit_category.view.*

class ProductEditCategoryCatalogViewHolder(var view: View, var listener: Listener, context: Context?): ProductCategoryRecommendationAdapter.Listener{

    private val categoryRecommendationList = ArrayList<ProductCategory>()
    private val productCategoryRecommendationAdapter: ProductCategoryRecommendationAdapter

    interface Listener {
        fun onCategoryRecommendationChoosen(productCategory: ProductCategory)
        fun onLabelCatalogClicked()
        fun onLabelCategoryClicked()
    }

    init {
        view.labelCatalog.setOnClickListener { listener.onLabelCatalogClicked() }

        categoryRecommendationList.add(ProductCategory().apply {
            categoryId = 1
            categoryName = "test"
        })
        categoryRecommendationList.add(ProductCategory().apply {
            categoryId = 2
            categoryName = "qwe"
        })
        categoryRecommendationList.add(ProductCategory().apply {
            categoryId = 3
            categoryName = "dsa"
        })
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
        view.labelCategory.setContent(productCategory.categoryName)
        productCategoryRecommendationAdapter.setSelectedCategory(productCategory)
    }

    fun setCatalogChosen(productCatalog: ProductCatalog){
        view.labelCatalog.setContent(productCatalog.catalogName)
    }
}