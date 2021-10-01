package com.tokopedia.createpost.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.kotlin.extensions.view.inflateLayout
import kotlinx.android.synthetic.main.content_item_product_tag_view.view.*

/**
 * @author by shruti on 01/08/21.
 */

class CreatePostTagAdapter(
    private val itemList: MutableList<RelatedProductItem> = mutableListOf(),
    private val onDeleteProduct: ((Int) -> Unit)? = null,
) :
    RecyclerView.Adapter<CreatePostTagAdapter.CreatePostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatePostViewHolder {
        return CreatePostViewHolder(parent.inflateLayout(R.layout.content_item_product_tag_view))

    }

    override fun onBindViewHolder(holder: CreatePostViewHolder, position: Int) {
        holder.bind(itemList[position])    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CreatePostViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(productData: RelatedProductItem) {
            with(itemView){
                productName.text = productData.name
                productPrice.text = productData.price
                productImage.setImageUrl(productData.image)

                product_content_tag_delete_button.setOnClickListener { removeProduct(adapterPosition) }
            }
        }
    }
    private fun removeProduct(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRangeRemoved(adapterPosition, 1)
        onDeleteProduct?.invoke(adapterPosition)
    }
    fun updateProduct(products: List<RelatedProductItem>){
        this.itemList.clear()
        this.itemList.addAll(products)
        notifyDataSetChanged()
    }

}