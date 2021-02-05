package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent

/**
 * Created by Yehezkiel on 01/02/21
 */
class ProductDetailInfoAdapter : RecyclerView.Adapter<ProductDetailInfoAdapter.ItemProductDetailInfoViewHolder>() {

    private var listOfContent: List<ProductDetailInfoContent> = listOf()

    fun updateData(data: List<ProductDetailInfoContent>) {
        listOfContent = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductDetailInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info_product_detail, parent, false)
        return ItemProductDetailInfoViewHolder(view)
    }

    override fun getItemCount(): Int = listOfContent.size

    override fun onBindViewHolder(holder: ItemProductDetailInfoViewHolder, position: Int) {
        holder.bind(listOfContent[position])
    }

    inner class ItemProductDetailInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val detailTitle: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.info_detail_title)
        private val detailDesc: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.info_detail_value)

        fun bind(data: ProductDetailInfoContent) {
            detailTitle?.text = data.title
            detailDesc?.text = data.subtitle
        }
    }
}