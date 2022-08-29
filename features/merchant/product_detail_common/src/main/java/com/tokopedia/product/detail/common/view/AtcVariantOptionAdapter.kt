package com.tokopedia.product.detail.common.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 08/03/20
 */
class AtcVariantOptionAdapter(val listener: AtcVariantListener) : RecyclerView.Adapter<BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>>() {

    private val optionList: MutableList<VariantOptionWithAttribute> = mutableListOf()

    fun setData(newOptionList: List<VariantOptionWithAttribute>) {

        val callback = AtcVariantOptionDiffUtilCallback(optionList, newOptionList)
        val diffResult = DiffUtil.calculateDiff(callback)

        diffResult.dispatchUpdatesTo(this)
        optionList.clear()
        optionList.addAll(newOptionList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAtcVariantItemViewHolder<VariantOptionWithAttribute> {
        val view = LayoutInflater.from(parent.context)
            .inflate(ItemVariantChipViewHolder.LAYOUT, parent, false)
        return ItemVariantChipViewHolder(view, listener)
    }

    override fun getItemCount(): Int = optionList.size

    override fun onBindViewHolder(holderItem: BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>, position: Int) {
        val option = optionList[position]
        holderItem.bind(option)

        if (option.currentState == VariantConstant.STATE_SELECTED) listener.onSelectionChanged(holderItem.itemView, position)
    }

    override fun getItemViewType(position: Int) = 0

    inner class AtcVariantOptionDiffUtilCallback(
            private val oldList: List<VariantOptionWithAttribute>,
            private val newList: List<VariantOptionWithAttribute>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].variantId == newList[newItemPosition].variantId
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].currentState == newList[newItemPosition].currentState && oldList[oldItemPosition].flashSale == newList[newItemPosition].flashSale
        }
    }
}