package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCriteriaSelectionBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection

class CriteriaSelectionAdapter(
    private val listener: CriteriaSelectionAdapterListener
) : RecyclerView.Adapter<CriteriaSelectionAdapter.CriteriaViewHolder>() {

    private var data: List<CriteriaSelection> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemCriteriaSelectionBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<CriteriaSelection>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class CriteriaViewHolder(
        private val binding: StfsItemCriteriaSelectionBinding,
        private val listener: CriteriaSelectionAdapterListener
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CriteriaSelection) {
            binding.tfTitle.text = item.categoryTitle
            binding.tfValue.text = "${item.selectionCount}/${item.selectionCountMax}"
            binding.iconMoreInfo.setOnClickListener {
                listener.onCriteriaMoreClicked(item.categoryTitleComplete, item.selectionCount, item.selectionCountMax)
            }
            binding.iconMoreInfo.isVisible = item.hasMoreData
        }
    }

    interface CriteriaSelectionAdapterListener {
        fun onCriteriaMoreClicked(
            categoryTitleComplete: String,
            selectionCount: Int,
            selectionCountMax: Int
        )
    }
}