package com.tokopedia.buyerorderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.databinding.ItemPofDetailRefundedBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofSummaryInfoUiModel

class ItemPofDetailRefundedAdapter(
    private val pofSummaryInfoList: List<PofSummaryInfoUiModel>
) : RecyclerView.Adapter<ItemPofDetailRefundedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPofDetailRefundedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = pofSummaryInfoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (pofSummaryInfoList.isNotEmpty()) {
            holder.bind(pofSummaryInfoList[position])
        }
    }

    inner class ViewHolder(private val binding: ItemPofDetailRefundedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PofSummaryInfoUiModel) {
            with(binding) {
                tvPofUnfulfilledDetailLabel.text = item.label
                tvPofUnfulfilledDetailValue.text = item.value
            }
        }
    }
}
