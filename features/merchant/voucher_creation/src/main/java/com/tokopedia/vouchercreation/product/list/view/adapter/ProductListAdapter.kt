package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemProductListLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.model.ProductVariant
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemVariantViewHolder.OnVariantItemClickListener
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder.OnProductItemClickListener

class ProductListAdapter(
        private val productItemClickListener: OnProductItemClickListener,
        private val variantItemClickListener: OnVariantItemClickListener
) : RecyclerView.Adapter<ProductItemViewHolder>() {

    private var productUiModelList: List<ProductUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val binding = ItemProductListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemViewHolder(binding, productItemClickListener, variantItemClickListener)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bindData(productUiModelList[position])
    }

    override fun getItemCount(): Int {
        return productUiModelList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(productUiModelList: List<ProductUiModel>) {
        this.productUiModelList = productUiModelList
        notifyDataSetChanged()
    }

    fun updateProductVariant(adapterPosition: Int, variants: List<ProductVariant>) {
        productUiModelList[adapterPosition].variants = variants
        notifyItemChanged(adapterPosition)
    }
}