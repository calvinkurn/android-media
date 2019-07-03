package com.tokopedia.affiliate.feature.createpost.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.TYPE_AFFILIATE
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import kotlinx.android.synthetic.main.item_product_attachment.view.*

class ProductAttachmentAdapter(private val products: MutableList<RelatedProductItem> = mutableListOf(),
                               private val onDeleteProduct: (()->Unit)? = null)
    : RecyclerView.Adapter<ProductAttachmentAdapter.ProductAttachmentViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAttachmentViewHolder {
        return ProductAttachmentViewHolder(parent.inflateLayout(R.layout.item_product_attachment))
    }

    override fun getItemCount(): Int = products.size

    override fun getItemId(position: Int): Long = products[position].id.toLongOrNull() ?: RecyclerView.NO_ID

    override fun onBindViewHolder(holder: ProductAttachmentViewHolder, position: Int) {
        holder.bind(products[position])
    }


    inner class ProductAttachmentViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(relatedProductItem: RelatedProductItem) {
            with(itemView){
                image_product.loadImageWithoutPlaceholder(relatedProductItem.image)
                product_name.text = MethodChecker.fromHtmlPreserveLineBreak(relatedProductItem.name)
                product_price.text = relatedProductItem.price
                product_price.setTextColor(MethodChecker.getColor(
                        context, if (relatedProductItem.type == TYPE_AFFILIATE) R.color.af_commission_blue
                        else R.color.Yellow_Y500))
                product_rating.gone()

                delete.setOnClickListener { removeProduct(adapterPosition) }
            }
        }
    }

    private fun removeProduct(adapterPosition: Int) {
        products.removeAt(adapterPosition)
        notifyDataSetChanged()
        onDeleteProduct?.invoke()
    }

    fun updateProduct(products: List<RelatedProductItem>){
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }
}