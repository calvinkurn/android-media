package com.tokopedia.broadcast.message.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.broadcast.message.data.model.MyProduct
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.view.viewholder.PlaceholderViewHolder
import kotlinx.android.synthetic.main.item_product.view.*

class BroadcastMessageProductItemAdapter(private val addProductListener: (() -> Unit),
                                         private val removeProduct: ((Int, Int) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val MAX_PRODUCT = 3
        private const val TYPE_PLACEHOLDER = 1
        private const val TYPE_ITEM = 2
    }
    private val products = mutableListOf<MyProduct>()

    override fun getItemViewType(position: Int): Int {
        return if (products.size < 3 && position == products.size){
            TYPE_PLACEHOLDER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PLACEHOLDER){
            PlaceholderViewHolder(LayoutInflater.from(parent.context).inflate(PlaceholderViewHolder.LAYOUT,
                    parent, false))
        } else {
            ProductItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product,
                    parent, false))
        }
    }

    override fun getItemCount(): Int = if (products.size < MAX_PRODUCT) products.size+1 else MAX_PRODUCT

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductItemViewHolder){
            holder.bind(products[position])
        } else {
            holder.itemView.setOnClickListener {addProductListener()}
        }
    }

    fun addProduct(product: MyProduct){
        products.add(product)
        notifyDataSetChanged()
    }

    fun removeProductAt(position: Int){
        val productId = products[position].productId
        products.removeAt(position)
        notifyDataSetChanged()
        removeProduct(productId, position)
    }

    fun addProducts(products: List<MyProduct>) {
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    fun clearProducts() {
        products.clear()
        notifyDataSetChanged()
    }

    inner class ProductItemViewHolder(val view: View): RecyclerView.ViewHolder(view){

        init {
            itemView.iv_delete.setOnClickListener {
                removeProductAt(adapterPosition)
            }
        }

        fun bind(product: MyProduct){
            ImageHandler.LoadImage(itemView.attach_product_image, product.productThumbnail)
            itemView.attach_product_name.text = product.productName
            itemView.attach_product_price.text = product.productPrice
        }
    }

}