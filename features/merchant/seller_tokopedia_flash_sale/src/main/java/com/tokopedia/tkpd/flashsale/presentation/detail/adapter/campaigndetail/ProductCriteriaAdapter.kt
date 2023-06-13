package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCriteriaBinding
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel

class ProductCriteriaAdapter: RecyclerView.Adapter<ProductCriteriaViewHolder>() {

    private var data: List<ProductCriteriaModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCriteriaViewHolder {
        val binding = StfsItemProductCriteriaBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ProductCriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ProductCriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<ProductCriteriaModel>) {
        val diffUtil = CriteriaDiffUtil(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CriteriaDiffUtil(
        private val oldItems: List<ProductCriteriaModel>,
        private val newItems: List<ProductCriteriaModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old.categorySelectionsText == new.categorySelectionsText
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }
    }
}
