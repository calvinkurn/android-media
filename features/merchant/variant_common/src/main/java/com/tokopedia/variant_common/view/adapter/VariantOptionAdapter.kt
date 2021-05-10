package com.tokopedia.variant_common.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.holder.BaseVariantViewHolder
import com.tokopedia.variant_common.view.holder.VariantChipViewHolder
import com.tokopedia.variant_common.view.holder.VariantColorViewHolder
import com.tokopedia.variant_common.view.holder.VariantImageViewHolder

/**
 * Created by Yehezkiel on 08/03/20
 */
class VariantOptionAdapter(val listener: ProductVariantListener) : RecyclerView.Adapter<BaseVariantViewHolder<VariantOptionWithAttribute>>() {

    private val TYPE_IMAGE = 1
    private val TYPE_COLOR = 2
    private val TYPE_CHIP = 3
    private val IDENTIFIER_COLOR = "colour"

    private val optionList: MutableList<VariantOptionWithAttribute> = mutableListOf()

    fun setData(newOptionList: List<VariantOptionWithAttribute>) {

        val callback = VariantOptionDiffUtilCallback(optionList, newOptionList)
        val diffResult = DiffUtil.calculateDiff(callback)

        diffResult.dispatchUpdatesTo(this)
        optionList.clear()
        optionList.addAll(newOptionList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVariantViewHolder<VariantOptionWithAttribute> {
        when (viewType) {
            TYPE_CHIP -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(VariantChipViewHolder.LAYOUT, parent, false)
                return VariantChipViewHolder(view, listener)
            }
            TYPE_COLOR -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(VariantColorViewHolder.LAYOUT, parent, false)
                return VariantColorViewHolder(view, listener)
            }
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(VariantImageViewHolder.LAYOUT, parent, false)
                return VariantImageViewHolder(view, listener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = optionList.size

    override fun onBindViewHolder(holder: BaseVariantViewHolder<VariantOptionWithAttribute>, position: Int) {
        val option = optionList[position]
        holder.bind(option)

        if (option.currentState == VariantConstant.STATE_SELECTED) listener.onSelectionChanged(holder.itemView, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (optionList[position].hasCustomImages) {
            TYPE_IMAGE
        } else {
            TYPE_CHIP
        }
    }

    inner class VariantOptionDiffUtilCallback(
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