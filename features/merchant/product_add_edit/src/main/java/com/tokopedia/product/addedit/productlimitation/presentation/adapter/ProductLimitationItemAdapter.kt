package com.tokopedia.product.addedit.productlimitation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantPhotoViewHolder

class ProductLimitationItemAdapter: RecyclerView.Adapter<ProductLimitationItemViewHolder>() {

    private var items: MutableList<ProductLimitationModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductLimitationItemViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_product_limitation, parent, false)
        return ProductLimitationItemViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProductLimitationItemViewHolder, position: Int) {
        val item = items[position]
        holder.bindData(item)
    }

    fun getData(): List<ProductLimitationModel> {
        return items
    }

    fun setData(items: List<ProductLimitationModel>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }
}