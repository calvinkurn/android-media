package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import kotlinx.android.synthetic.main.item_som_product_bundling_product.view.*

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailProductBundlingAdapter(
        private val actionListener: SomDetailAdapter.ActionListener?
) : RecyclerView.Adapter<SomDetailProductBundlingAdapter.ViewHolder>() {

    var products = emptyList<SomDetailOrder.Data.GetSomDetail.Products>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_som_product_bundling_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shouldShowDivider = products.size.minus(1) != position
        holder.bind(products[position], shouldShowDivider)
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: SomDetailOrder.Data.GetSomDetail.Products, shouldShowDivider: Boolean) {
            with(itemView) {
                setOnClickListener {
                    actionListener?.onClickProduct(product.orderDetailId.toIntOrZero())
                }
                imgSomBundleProduct.loadImage(product.thumbnail)
                tvSomBundleProductName.text = product.name
                tvSomBundlePrice.text = StringBuilder("${product.quantity} x ${product.priceText}")
                if (product.note.isNotEmpty()) {
                    tvSomBundleNotes.visible()
                    tvSomBundleNotes.text = product.note.replace("\\n", System.getProperty("line.separator")
                            ?: "")
                } else {
                    tvSomBundleNotes.gone()
                }

                dividerSomBundle.isVisible = shouldShowDivider
            }
        }
    }
}