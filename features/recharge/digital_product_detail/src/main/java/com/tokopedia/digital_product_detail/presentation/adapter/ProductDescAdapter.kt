package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital_product_detail.databinding.ItemProductDescBinding

class ProductDescAdapter(
    private val productDescriptions: List<String>
): RecyclerView.Adapter<ProductDescAdapter.ProductDescViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDescViewHolder {
        val view = ItemProductDescBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductDescViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductDescViewHolder, position: Int) {
        holder.bind(productDescriptions[position])
    }

    override fun getItemCount(): Int = productDescriptions.size

    inner class ProductDescViewHolder(
        private val binding: ItemProductDescBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(desc: String) {
            binding.tvProductDesc.text = MethodChecker.fromHtml(desc)
        }
    }
}