package com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.databinding.ItemMvcHeaderChipBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.CashbackTypeChipUiModel

class CashbackTypeAdapter(val cashbackChipList: List<CashbackTypeChipUiModel>,
                          private val onSelectedType: (Int) -> Unit = {}) : RecyclerView.Adapter<CashbackTypeAdapter.CashbackTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashbackTypeViewHolder {
        return CashbackTypeViewHolder(ItemMvcHeaderChipBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = cashbackChipList.size

    override fun onBindViewHolder(holder: CashbackTypeViewHolder, position: Int) {
        holder.bind(cashbackChipList[position])
    }

    inner class CashbackTypeViewHolder(private val binding: ItemMvcHeaderChipBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cashbackChip: CashbackTypeChipUiModel) {
            binding.itemChipMvc.run {
                chipText = context?.resources?.getString(cashbackChip.cashbackType.chipTitleRes).toBlankOrString()
                chipType =
                    if (cashbackChip.isActive) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                if (!cashbackChip.isActive) {
                    setOnClickListener {
                        onSelectedType(position)
                    }
                }
            }
        }
    }
}