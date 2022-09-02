package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnItemLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductOptionViewHolder

class ProductOptionAdapter(private val listener: ProductOptionViewHolder.Listener): RecyclerView.Adapter<ProductOptionViewHolder>() {

    private val optionItems: MutableList<OptionUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductOptionViewHolder {
        val binding = TokofoodItemAddOnItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductOptionViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProductOptionViewHolder, position: Int) {
        optionItems.getOrNull(position)?.let {
            holder.bindData(it, position)
        }
    }

    override fun getItemCount(): Int = optionItems.size

    fun setData(items: List<OptionUiModel>) {
        optionItems.clear()
        optionItems.addAll(items)
    }

    fun updateData(previousIndex: Int, items: List<OptionUiModel>) {
        setData(items)
        if (previousIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(previousIndex)
        }
    }

    fun updateSelectableData(items: List<OptionUiModel>) {
        setData(items)
        items.forEachIndexed { index, optionUiModel ->
            if (!optionUiModel.isSelected) {
                notifyItemChanged(index)
            }
        }
    }

}