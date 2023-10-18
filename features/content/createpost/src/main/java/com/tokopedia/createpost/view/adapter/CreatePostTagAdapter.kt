package com.tokopedia.createpost.view.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

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
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CreatePostViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val productName: Typography = view.findViewById(R.id.productName)
        private val productPrice: Typography = view.findViewById(R.id.productPrice)
        private val productImage: ImageUnify = view.findViewById(R.id.productImage)
        private val productDeleteButton: IconUnify = view.findViewById(R.id.product_content_tag_delete_button)

        fun bind(productData: RelatedProductItem) {
            productName.text = productData.name
            productPrice.text = productData.price
            productImage.setImageUrl(productData.image)
            productDeleteButton.setOnClickListener { removeProduct(adapterPosition) }
        }
    }

    private fun removeProduct(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRangeRemoved(adapterPosition, 1)
        onDeleteProduct?.invoke(adapterPosition)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProduct(products: List<RelatedProductItem>) {
        this.itemList.clear()
        this.itemList.addAll(products)
        notifyDataSetChanged()
    }

}
