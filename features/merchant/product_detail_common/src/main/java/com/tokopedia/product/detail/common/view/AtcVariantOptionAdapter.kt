package com.tokopedia.product.detail.common.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atc_variant.views.viewholder.item.ItemVariantChipViewHolder
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 08/03/20
 */
class AtcVariantOptionAdapter(val listener: AtcVariantListener) : RecyclerView.Adapter<BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>>() {

    private val TYPE_IMAGE = 1
    private val TYPE_CHIP = 3

    private val optionList: MutableList<VariantOptionWithAttribute> = mutableListOf()

    fun setData(newOptionList: List<VariantOptionWithAttribute>) {

        val callback = AtcVariantOptionDiffUtilCallback(optionList, newOptionList)
        val diffResult = DiffUtil.calculateDiff(callback)

        diffResult.dispatchUpdatesTo(this)
        optionList.clear()
        optionList.addAll(newOptionList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAtcVariantItemViewHolder<VariantOptionWithAttribute> {
        when (viewType) {
            TYPE_CHIP -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(ItemVariantChipViewHolder.LAYOUT, parent, false)
                return ItemVariantChipViewHolder(view, listener)
            }
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(ItemVariantImageViewHolder.LAYOUT, parent, false)
                return ItemVariantImageViewHolder(view, listener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = optionList.size

    override fun onBindViewHolder(holderItem: BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>, position: Int) {
        val option = optionList[position]
        holderItem.bind(option)

        if (option.currentState == VariantConstant.STATE_SELECTED) listener.onSelectionChanged(holderItem.itemView, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (optionList[position].hasCustomImages) {
            TYPE_IMAGE
        } else {
            TYPE_CHIP
        }
    }

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