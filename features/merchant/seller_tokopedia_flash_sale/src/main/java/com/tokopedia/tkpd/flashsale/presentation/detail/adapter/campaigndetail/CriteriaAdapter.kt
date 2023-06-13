package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCriteriaBinding

class CriteriaAdapter: RecyclerView.Adapter<CriteriaAdapter.CriteriaViewHolder>() {

    private var data: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemCriteriaBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<String>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class CriteriaViewHolder(private val binding: StfsItemCriteriaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            val numberText = "${adapterPosition.inc()}."
            binding.tfNumber.text = numberText
            binding.tfText.text = title
        }
    }
}
