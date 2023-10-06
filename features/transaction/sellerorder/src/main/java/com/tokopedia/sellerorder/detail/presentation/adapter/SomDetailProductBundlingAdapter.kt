package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSomProductBundlingProductBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailProductBundlingAdapter(
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?
) : RecyclerView.Adapter<SomDetailProductBundlingAdapter.ViewHolder>() {

    var products = emptyList<SomDetailOrder.GetSomDetail.Details.Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_som_product_bundling_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding<ItemSomProductBundlingProductBinding>()

        fun bind(product: SomDetailOrder.GetSomDetail.Details.Product) {
            binding?.run {
                root.setOnClickListener {
                    actionListener?.onClickProduct(product.orderDetailId.toLongOrZero())
                }
                imgSomBundleProduct.loadProductImage(
                    url = product.thumbnail,
                    archivedUrl = TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_SMALL
                )
                tvSomBundleProductName.text = product.name
                tvSomBundlePrice.text = StringBuilder("${product.quantity} x ${product.priceText}")
                if (product.note.isNotEmpty()) {
                    tvSomBundleNotes.visible()
                    tvSomBundleNotes.text = product.note.replace(
                        "\\n",
                        System.getProperty("line.separator")
                            ?: ""
                    )
                } else {
                    tvSomBundleNotes.gone()
                }
            }
        }
    }
}
