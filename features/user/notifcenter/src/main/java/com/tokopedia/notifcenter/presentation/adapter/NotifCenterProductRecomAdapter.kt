package com.tokopedia.notifcenter.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifcenter.domain.pojo.ProductData
import com.tokopedia.notifcenter.presentation.adapter.viewholder.ProductRecommendationMoreViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.ProductRecommendationViewHolder
import com.tokopedia.notifcenter.listener.NotificationItemListener

class NotifCenterProductRecomAdapter(
        val listener: NotificationItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var products: List<ProductData> = emptyList()
    private var totalProduct = products.size
    private val TYPE_PRODUCT = 0
    private val TYPE_PRODUCT_LOAD_MROE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PRODUCT -> ProductRecommendationViewHolder.create(parent, listener)
            TYPE_PRODUCT_LOAD_MROE -> ProductRecommendationMoreViewHolder.create(parent)
            else -> throw IllegalStateException("Invalid viewType: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return getProductRecommendationSize()
    }

    private fun getProductRecommendationSize(): Int {
        return if (products.size < MAX_ITEM) {
            products.size
        } else {
            MAX_ITEM
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasMoreThanMaximum(position)) {
            TYPE_PRODUCT_LOAD_MROE
        } else {
            TYPE_PRODUCT
        }
    }

    private fun hasMoreThanMaximum(position: Int): Boolean {
        return position >= (MAX_ITEM - 1) && products.size > MAX_ITEM
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is ProductRecommendationViewHolder -> viewHolder.bind(products[position])
            is ProductRecommendationMoreViewHolder -> viewHolder.bind(products, position, totalProduct)
        }
    }

    fun updateProductRecommendation(products: List<ProductData>) {
        this.products = products
        notifyDataSetChanged()
    }

    fun updateTotalProductCount(totalProduct: Int) {
        this.totalProduct = totalProduct
    }

    fun getSeenProductRecommendation() : List<ProductData> {
        if (products.isEmpty()) return emptyList()
        val size = getProductRecommendationSize()
        return products.subList(0, size)
    }

    companion object {
        const val MAX_ITEM = 4
    }

}