package com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.databinding.ItemMvcHeaderChipBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.CashbackTypeChipUiModel

class CashbackTypeAdapter(val cashbackChipList: List<CashbackTypeChipUiModel>,
                          private val onSelectedType: (Int) -> Unit = {}) : RecyclerView.Adapter<CashbackTypeAdapter.CashbackTypeViewHolder>() {

    class CashbackTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var binding: ItemMvcHeaderChipBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashbackTypeViewHolder {
        binding = ItemMvcHeaderChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CashbackTypeViewHolder(binding!!.root)
    }

    override fun getItemCount(): Int = cashbackChipList.size

    override fun onBindViewHolder(holder: CashbackTypeViewHolder, position: Int) {
        val cashbackChip = cashbackChipList[position]
        binding?.itemChipMvc?.run {
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