package com.tokopedia.shopdiscount.subsidy.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shopdiscount.databinding.LayoutItemShopDiscountOptOutReasonBinding
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountOptOutReasonUiModel
import com.tokopedia.shopdiscount.subsidy.presentation.adapter.viewholder.ShopDiscountOptOutOptionItemViewHolder

class ShopDiscountOptOutReasonAdapter(
    private val optOutOptionItemViewHolderListener: ShopDiscountOptOutOptionItemViewHolder.Listener
) : ListAdapter<ShopDiscountOptOutReasonUiModel, ShopDiscountOptOutOptionItemViewHolder>(
    DiffCallback()
) {

    private var listReasonUiModel: List<ShopDiscountOptOutReasonUiModel> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopDiscountOptOutOptionItemViewHolder {
        val viewBinding = LayoutItemShopDiscountOptOutReasonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopDiscountOptOutOptionItemViewHolder(
            viewBinding,
            optOutOptionItemViewHolderListener
        )
    }

    override fun onBindViewHolder(holder: ShopDiscountOptOutOptionItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return listReasonUiModel.size
    }

    override fun getItemViewType(position: Int): Int {
        return ShopDiscountOptOutOptionItemViewHolder.LAYOUT
    }

    fun setListReasonData(listReasonUiModel: List<ShopDiscountOptOutReasonUiModel>) {
        this.listReasonUiModel = listReasonUiModel.toMutableList().apply {
            //Add Lainnya option
            add(
                ShopDiscountOptOutReasonUiModel(
                    isSelected = false,
                    reason = "",
                    isReasonFromResponse = false
                )
            )
        }.toList()
        submitList(this.listReasonUiModel)
    }

    fun selectReason(uiModel: ShopDiscountOptOutReasonUiModel, selected: Boolean) {
        val newList = listReasonUiModel.map {
            if (it == uiModel) {
                it.copy(
                    isSelected = selected,
                    reason = it.reason,
                    isReasonFromResponse = it.isReasonFromResponse
                )
            } else {
                it.copy(
                    isSelected = false,
                    reason = it.reason,
                    isReasonFromResponse = it.isReasonFromResponse
                )
            }
        }.toList()
        submitList(newList)
        listReasonUiModel = newList
    }

    fun setCustomReason(
        uiModel: ShopDiscountOptOutReasonUiModel,
        customReason: String,
        inputError: Boolean
    ) {
        uiModel.reason = customReason
        uiModel.isInputError = inputError
    }

    fun isReasonSelected(): Boolean {
        return listReasonUiModel.any { it.isSelected && it.reason.isNotEmpty() && !it.isInputError }
    }

    fun getSelectedReason(): String {
        return listReasonUiModel.firstOrNull { it.isSelected && it.reason.isNotEmpty() }?.reason.orEmpty()
    }

    class DiffCallback : DiffUtil.ItemCallback<ShopDiscountOptOutReasonUiModel>() {
        override fun areItemsTheSame(
            oldItem: ShopDiscountOptOutReasonUiModel,
            newItem: ShopDiscountOptOutReasonUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ShopDiscountOptOutReasonUiModel,
            newItem: ShopDiscountOptOutReasonUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}
