package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailProductCardItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailProductsCardAdapter(private val actionListener: SomDetailAdapter.ActionListener?) :
    RecyclerView.Adapter<SomDetailProductsCardAdapter.ViewHolder>() {
    var listProducts = mutableListOf<SomDetailOrder.Data.GetSomDetail.Products>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_product_card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProducts.getOrNull(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding<DetailProductCardItemBinding>()

        fun bind(element: SomDetailOrder.Data.GetSomDetail.Products?) {
            element?.run {
                binding?.run {
                    root.setOnClickListener {
                        actionListener?.onClickProduct(orderDetailId.toIntOrZero())
                    }
                    ivProduct.loadImage(thumbnail)
                    tvProductName.text = name
                    tvProductDesc.text = StringBuilder("$quantity x $priceText")
                    if (note.isNotEmpty()) {
                        dividerProduct.visibility = View.VISIBLE
                        tvProductNotes.visibility = View.VISIBLE
                        tvProductNotes.text =
                            note.replace("\\n", System.getProperty("line.separator") ?: "")
                    } else {
                        dividerProduct.visibility = View.GONE
                        tvProductNotes.visibility = View.GONE
                    }
                }
            }
        }
    }
}