package com.tokopedia.product.addedit.detail.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.databinding.ItemSimilarProductLayoutBinding
import com.tokopedia.product.addedit.detail.presentation.model.SimilarProduct
import com.tokopedia.product.addedit.detail.presentation.viewholder.SimilarProductViewHolder

class SimilarProductAdapter(private val clickListener: SimilarProductViewHolder.ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var similarProducts: List<SimilarProduct> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSimilarProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimilarProductViewHolder(clickListener, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val similarProduct = similarProducts.getOrNull(position)
        val viewHolder = holder as SimilarProductViewHolder
        similarProduct?.run { viewHolder.bindData(this) }
    }

    override fun getItemCount(): Int {
        return similarProducts.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSimilarProducts(similarProducts: List<SimilarProduct>) {
        this.similarProducts = similarProducts
        notifyDataSetChanged()
    }
}