package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemProductListVariantLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemVariantViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemVariantViewHolder.OnVariantItemClickListener

class VariantListAdapter(private val variantItemClickListener: OnVariantItemClickListener)
    : RecyclerView.Adapter<ProductItemVariantViewHolder>() {

    private var variantList: List<VariantUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemVariantViewHolder {
        val binding = ItemProductListVariantLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return ProductItemVariantViewHolder(binding, variantItemClickListener)
    }

    override fun onBindViewHolder(holder: ProductItemVariantViewHolder, position: Int) {
        holder.bindData(variantList[position])
    }

    override fun getItemCount(): Int {
        return variantList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setVariantList(variantList: List<VariantUiModel>) {
        this.variantList = variantList
        notifyDataSetChanged()
    }
}