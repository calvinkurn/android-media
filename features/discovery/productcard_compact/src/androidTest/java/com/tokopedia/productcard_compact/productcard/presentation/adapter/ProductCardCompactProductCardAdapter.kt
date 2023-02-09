package com.tokopedia.productcard_compact.productcard.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard_compact.test.R
import com.tokopedia.productcard_compact.productcard.presentation.model.ProductCardCompactProductCardMatcherModel

internal class ProductCardCompactProductCardAdapter(
    private val productCardModelMatchers: List<ProductCardCompactProductCardMatcherModel>
): RecyclerView.Adapter<ProductCardCompactProductCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardCompactProductCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tokopedianow_product_card_test, null)
        return ProductCardCompactProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productCardModelMatchers.size
    }

    override fun onBindViewHolder(holder: ProductCardCompactProductCardViewHolder, position: Int) {
        holder.bind(productCardModelMatchers[position].model)
    }
}
