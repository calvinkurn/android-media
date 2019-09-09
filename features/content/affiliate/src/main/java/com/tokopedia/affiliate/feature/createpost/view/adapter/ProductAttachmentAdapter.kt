package com.tokopedia.affiliate.feature.createpost.view.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
                               private val onDeleteProduct: ((Int)->Unit)? = null)
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
                val layoutParams = card_product_attachment.layoutParams
                layoutParams.width = if (itemCount == 1) fullWidth else standardWidth
                card_product_attachment.layoutParams = layoutParams

                image_product.loadImageWithoutPlaceholder(relatedProductItem.image)
                product_name.text = MethodChecker.fromHtmlPreserveLineBreak(relatedProductItem.name)
                product_price.text = relatedProductItem.price
                product_price.setTextColor(ContextCompat.getColor(
                        context, if (relatedProductItem.type == TYPE_AFFILIATE) R.color.af_commission_blue
                        else R.color.Yellow_Y500))
                product_rating.gone()

                delete.setOnClickListener { removeProduct(adapterPosition) }
            }
        }

        val fullWidth = with(itemView.context.resources){
            displayMetrics.widthPixels - getDimensionPixelSize(R.dimen.dp_16) * 2
        }

        val standardWidth = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_260)
    }

    private fun removeProduct(adapterPosition: Int) {
        products.removeAt(adapterPosition)
        notifyDataSetChanged()
        onDeleteProduct?.invoke(adapterPosition)
    }

    fun updateProduct(products: List<RelatedProductItem>){
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }
}