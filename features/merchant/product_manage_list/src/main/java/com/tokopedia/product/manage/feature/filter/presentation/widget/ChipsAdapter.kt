package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.databinding.ItemChipsProductManageBinding
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.FilterDataDiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class ChipsAdapter(private val listener: ChipClickListener, private val canSelectMany: Boolean, private val title: String = "") : RecyclerView.Adapter<ChipsAdapter.ItemViewHolder>() {
    private var data: MutableList<FilterDataUiModel> = mutableListOf()

    companion object {
        const val MAXIMUM_CHIPS = 5
        const val KEY_SELECT_BUNDLE = "key_is_select_bundle"
    }

    fun setData(element: FilterUiModel) {
        var numSelected = 0
        element.data.forEach {
            if(it.select) numSelected++
        }
        var dataToDisplay = element.data
        if(numSelected < MAXIMUM_CHIPS) {
            if(element.data.size > MAXIMUM_CHIPS) {
                dataToDisplay = element.data.subList(0, MAXIMUM_CHIPS)
            }
        } else {
            dataToDisplay = element.data.subList(0, numSelected)
        }
        val diffUtilCallback = FilterDataDiffUtil(data, dataToDisplay)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        data.clear()
        data.addAll(dataToDisplay)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemChipsProductManageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads.getOrNull(0) as? Bundle
            bundle?.keySet()?.forEach { key ->
                if(key == KEY_SELECT_BUNDLE) {
                    holder.bind(data[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ItemViewHolder(private val binding: ItemChipsProductManageBinding,
                               private val chipClickListener: ChipClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(element: FilterDataUiModel) {
            binding.chipsItem.run {
                centerText = true
                chipText = element.name
                chipSize = ChipsUnify.SIZE_MEDIUM
                chipType = if(element.select) {
                    ChipsUnify.TYPE_SELECTED
                } else {
                    ChipsUnify.TYPE_NORMAL
                }
                setOnClickListener {
                    chipClickListener.onChipClicked(element, canSelectMany, title)
                }
            }
        }
    }

    interface ChipClickListener {
        fun onChipClicked(element: FilterDataUiModel, canSelectMany: Boolean, title: String)
    }
}