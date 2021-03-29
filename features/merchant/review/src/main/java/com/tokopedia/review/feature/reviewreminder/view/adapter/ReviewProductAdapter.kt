package com.tokopedia.review.feature.reviewreminder.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderData
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewProductAdapter : RecyclerView.Adapter<ReviewProductAdapter.ViewHolder>() {

    private val products = mutableListOf<ProductrevGetReminderData>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_review_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = products[position]
        holder.image.urlSrc = item.productThumbnail
        holder.name.text = item.productName
        holder.rating.text = item.avgRating.toString()
        holder.buyer.text = context?.getString(
                R.string.review_reminder_item_product_buyer,
                item.buyerCount.toString()
        )
    }

    fun updateList(products: List<ProductrevGetReminderData>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageUnify = view.findViewById(R.id.image_item)
        val name: Typography = view.findViewById(R.id.text_name)
        val rating: Typography = view.findViewById(R.id.text_rating)
        val buyer: Typography = view.findViewById(R.id.text_buyer)
    }
}