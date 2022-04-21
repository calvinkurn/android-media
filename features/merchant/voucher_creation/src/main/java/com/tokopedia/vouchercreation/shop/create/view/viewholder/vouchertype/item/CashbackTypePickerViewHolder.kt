package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertype.CashbackTypeAdapter
import com.tokopedia.vouchercreation.shop.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.CashbackTypeChipUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.CashbackTypePickerUiModel
import kotlinx.android.synthetic.main.mvc_voucher_type_cashback_type.view.*

class CashbackTypePickerViewHolder(itemView: View) : AbstractViewHolder<CashbackTypePickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_type_cashback_type
    }

    private var cashbackChipsList: List<CashbackTypeChipUiModel> = listOf()

    private var cashbackTypeAdapter: CashbackTypeAdapter? = null

    private var onSelectedType: (CashbackType) -> Unit = {}

    override fun bind(element: CashbackTypePickerUiModel) {
        element.currentActiveType.let { type ->
            cashbackChipsList = listOf(
                    CashbackTypeChipUiModel(
                            cashbackType = CashbackType.Rupiah,
                            isActive = type is CashbackType.Rupiah
                    ),
                    CashbackTypeChipUiModel(
                            cashbackType = CashbackType.Percentage,
                            isActive = type is CashbackType.Percentage
                    )
            )
            cashbackTypeAdapter = CashbackTypeAdapter(cashbackChipsList, ::onChipSelected)
        }
        itemView.cashbackTypeRecyclerView?.run {
            cashbackTypeAdapter?.run {
                adapter = this
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        onSelectedType = element.onSelectedType
    }

    private fun onChipSelected(position: Int) {
        cashbackChipsList.forEachIndexed { index, cashbackTypeChipUiModel ->
            cashbackTypeChipUiModel.isActive = position == index
            if (position == index) {
                onSelectedType(cashbackTypeChipUiModel.cashbackType)
            }
        }
        cashbackTypeAdapter?.notifyDataSetChanged()
    }
}