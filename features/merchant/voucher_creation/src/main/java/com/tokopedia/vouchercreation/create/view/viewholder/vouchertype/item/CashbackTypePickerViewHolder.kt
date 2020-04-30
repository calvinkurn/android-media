package com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.adapter.vouchertype.CashbackTypeAdapter
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackTypeChipUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackTypePickerUiModel
import kotlinx.android.synthetic.main.mvc_voucher_type_cashback_type.view.*

class CashbackTypePickerViewHolder(itemView: View) : AbstractViewHolder<CashbackTypePickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_type_cashback_type
    }

    private val cashbackChipsList by lazy {
        listOf(
                CashbackTypeChipUiModel(
                        cashbackType = CashbackType.RUPIAH,
                        isActive = true
                ),
                CashbackTypeChipUiModel(
                        cashbackType = CashbackType.PERCENTAGE,
                        isActive = false
                )
        )
    }

    private val cashbackTypeAdapter by lazy {
        CashbackTypeAdapter(cashbackChipsList, ::onChipSelected)
    }

    override fun bind(element: CashbackTypePickerUiModel) {
        itemView.cashbackTypeRecyclerView?.run {
            adapter = cashbackTypeAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun onChipSelected(position: Int) {
        cashbackChipsList.forEachIndexed { index, cashbackTypeChipUiModel ->
            cashbackTypeChipUiModel.isActive = position == index
        }
        cashbackTypeAdapter.notifyDataSetChanged()
    }
}