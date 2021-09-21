package com.tokopedia.product.addedit.productlimitation.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationActionItemModel

class ProductLimitationItemAdapter: RecyclerView.Adapter<ProductLimitationItemViewHolder>() {

    private var items: MutableList<ProductLimitationActionItemModel> = mutableListOf()
    private var itemOnClick: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductLimitationItemViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_product_limitation, parent, false)
        return setupViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProductLimitationItemViewHolder, position: Int) {
        val item = items[position]
        holder.bindData(item)
    }

    private fun setupViewHolder(rootView: View): ProductLimitationItemViewHolder {
        return ProductLimitationItemViewHolder(rootView).apply {
            setOnClickListener { position ->
                val actionUrl = items[position].actionUrl
                itemOnClick.invoke(actionUrl)
            }
        }
    }

    fun getData(): List<ProductLimitationActionItemModel> {
        return items
    }

    fun setData(items: List<ProductLimitationActionItemModel>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun setOnItemClick (listener: (String) -> Unit) {
        itemOnClick = listener
    }
}