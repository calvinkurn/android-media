package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.viewholder.ProductNameRecViewHolder

class ProductNameRecAdapter : RecyclerView.Adapter<ProductNameRecViewHolder>() {

    private var productNameInput: String = ""
    private var productNameRecommendations: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductNameRecViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.product_name_rec_item, parent, false)
        return ProductNameRecViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return productNameRecommendations.size
    }

    override fun onBindViewHolder(holder: ProductNameRecViewHolder, position: Int) {
        val productNameRecommendation = productNameRecommendations[position]
        holder.bindData(productNameInput, productNameRecommendation)
    }

    fun setProductNameRecommendations(nameRecommendations: List<String>) {
        productNameRecommendations = nameRecommendations
        notifyDataSetChanged()
    }

    fun setProductNameInput(productNameInput: String) {
        this.productNameInput = productNameInput
    }
}