package com.tokopedia.shopdiscount.subsidy.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shopdiscount.databinding.LayoutItemShopDiscountSubsidyProductBinding
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProductSubsidyUiModel
import com.tokopedia.shopdiscount.subsidy.presentation.adapter.viewholder.ShopDiscountSubsidyProductItemViewHolder

class ShopDiscountProductSubsidyAdapter(
    private val listener: ShopDiscountSubsidyProductItemViewHolder.Listener
) : ListAdapter<ShopDiscountProductSubsidyUiModel, ShopDiscountSubsidyProductItemViewHolder>(
    DiffCallback()
) {

    private var listProductSubsidy: List<ShopDiscountProductSubsidyUiModel> = listOf()
    fun setListProductSubsidy(listProductSubsidy: List<ShopDiscountProductSubsidyUiModel>) {
        this.listProductSubsidy = listProductSubsidy
        submitList(listProductSubsidy)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopDiscountSubsidyProductItemViewHolder {
        val viewBinding = LayoutItemShopDiscountSubsidyProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopDiscountSubsidyProductItemViewHolder(
            viewBinding,
            listProductSubsidy.size,
            listener
        )
    }

    override fun onBindViewHolder(holder: ShopDiscountSubsidyProductItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return listProductSubsidy.size
    }

    override fun getItemViewType(position: Int): Int {
        return ShopDiscountSubsidyProductItemViewHolder.LAYOUT
    }

    fun getTotalSelectedProduct(): Int {
        return listProductSubsidy.filter { it.isSelected }.size
    }

    fun getSelectedProduct(): List<ShopDiscountProductSubsidyUiModel> {
        return listProductSubsidy.filter { it.isSelected }
    }

    fun setProductSelectedStatus(isChecked: Boolean, uiModel: ShopDiscountProductSubsidyUiModel) {
        val newList = listProductSubsidy.toList().map {
            if (it == uiModel) {
                it.copy(isSelected = isChecked)
            } else {
                it.apply {
                    isSelected = it.isSelected
                }
            }
        }
        submitList(newList)
        listProductSubsidy = newList
    }

    fun updateAllSelectedProduct(isChecked: Boolean) {
        val newList = listProductSubsidy.toList().map {
            it.copy().apply {
                isSelected = isChecked
            }
        }
        submitList(newList)
        listProductSubsidy = newList
    }

    class DiffCallback : DiffUtil.ItemCallback<ShopDiscountProductSubsidyUiModel>() {
        override fun areItemsTheSame(
            oldItem: ShopDiscountProductSubsidyUiModel,
            newItem: ShopDiscountProductSubsidyUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ShopDiscountProductSubsidyUiModel,
            newItem: ShopDiscountProductSubsidyUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}
