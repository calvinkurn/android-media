package com.tokopedia.product.detail.estimasiongkir.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.ServiceProduct
import kotlinx.android.synthetic.main.item_service_product.view.*

class ServiceProductAdapter : RecyclerView.Adapter<ServiceProductAdapter.ServiceProductViewHolder>() {
    private val products: MutableList<ServiceProduct> = mutableListOf()

    fun replaceProducts(products: List<ServiceProduct>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceProductViewHolder {
        return ServiceProductViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_service_product, parent, false))
    }

    override fun onBindViewHolder(holder: ServiceProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    inner class ServiceProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: ServiceProduct) {
            itemView.label_view.title = product.name
            itemView.label_view.setContent(product.price.priceFmt)
        }
    }
}
