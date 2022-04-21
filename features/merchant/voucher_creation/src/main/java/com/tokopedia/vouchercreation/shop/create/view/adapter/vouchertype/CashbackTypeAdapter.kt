package com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.CashbackTypeChipUiModel
import kotlinx.android.synthetic.main.item_mvc_header_chip.view.*

class CashbackTypeAdapter(val cashbackChipList: List<CashbackTypeChipUiModel>,
                          private val onSelectedType: (Int) -> Unit = {}) : RecyclerView.Adapter<CashbackTypeAdapter.CashbackTypeViewHolder>() {

    class CashbackTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashbackTypeViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mvc_header_chip, parent, false)
        return CashbackTypeViewHolder(view)
    }

    override fun getItemCount(): Int = cashbackChipList.size

    override fun onBindViewHolder(holder: CashbackTypeViewHolder, position: Int) {
        val cashbackChip = cashbackChipList[position]
        holder.itemView.itemChipMvc?.run {
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