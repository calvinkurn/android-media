package com.tokopedia.product.detail.view.adapter.variant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.data.model.variant.VariantCategory
import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute
import com.tokopedia.product.detail.view.listener.ProductVariantListener
import com.tokopedia.product.detail.view.viewholder.variant.BaseVariantViewHolder
import com.tokopedia.product.detail.view.viewholder.variant.VariantChipViewHolder
import com.tokopedia.product.detail.view.viewholder.variant.VariantColorViewHolder
import com.tokopedia.product.detail.view.viewholder.variant.VariantImageViewHolder

/**
 * Created by Yehezkiel on 2020-02-28
 */
class VariantOptionAdapter(val listener: ProductVariantListener) : RecyclerView.Adapter<BaseVariantViewHolder<VariantOptionWithAttribute>>() {

    private val TYPE_IMAGE = 1
    private val TYPE_COLOR = 2
    private val TYPE_CHIP = 3
    private val IDENTIFIER_COLOR = "colour"

    private var variantCategory: VariantCategory = VariantCategory()

    fun setData(data: VariantCategory) {
        this.variantCategory = data
        notifyDataSetChanged()
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

    override fun getItemCount(): Int = variantCategory.variantOptions.size

    override fun onBindViewHolder(holder: BaseVariantViewHolder<VariantOptionWithAttribute>, position: Int) {
        holder.bind(variantCategory.variantOptions[position])
    }

    override fun onBindViewHolder(holder: BaseVariantViewHolder<VariantOptionWithAttribute>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(variantCategory.variantOptions[position], payloads.firstOrNull() as? Int
                    ?: 0)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (variantCategory.identifier == IDENTIFIER_COLOR) {
            if (variantCategory.hasCustomImage) {
                TYPE_IMAGE
            } else {
                TYPE_COLOR
            }
        } else {
            TYPE_CHIP
        }
    }
}