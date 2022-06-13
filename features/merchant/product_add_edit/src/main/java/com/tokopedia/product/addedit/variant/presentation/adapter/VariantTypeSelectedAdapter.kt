package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeSelectedViewHolder

class VariantTypeSelectedAdapter : RecyclerView.Adapter<VariantTypeSelectedViewHolder>() {

    private var items: MutableList<Pair<String, Boolean>> = mutableListOf()
    private var onEditButtonClickedListener: ((position: Int) -> Unit)? = null
    private var onDeleteButtonClickedListener: ((position: Int) -> Unit)? = null
    private var onWarningListener: ((warningText: String) -> Unit)? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantTypeSelectedViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_variant_type_selected, parent, false)
        return initViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: VariantTypeSelectedViewHolder, position: Int) {
        val variantUnitValue = items[position].first
        val isCustomVariant = items[position].second
        holder.bindData(variantUnitValue, isCustomVariant)
    }

    private fun initViewHolder(rootView: View): VariantTypeSelectedViewHolder {
        val viewHolder = VariantTypeSelectedViewHolder(rootView)

        viewHolder.binding?.iconDelete?.setOnClickListener {
            val position = viewHolder.adapterPosition
            onDeleteButtonClickedListener?.invoke(position)
        }
        viewHolder.binding?.iconEdit?.setOnClickListener {
            val position = viewHolder.adapterPosition
            val isEnabled = items.getOrNull(position)?.second.orFalse()
            val warningText = it.context.getString(R.string.error_cannot_change_variant_recommendation)

            if (isEnabled) {
                onEditButtonClickedListener?.invoke(position)
            } else {
                onWarningListener?.invoke(warningText)
            }
        }

        return viewHolder
    }

    fun setData(items: List<Pair<String, Boolean>>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun setOnEditButtonClickedListener(listener: (position: Int) -> Unit) {
        onEditButtonClickedListener = listener
    }

    fun setOnDeleteButtonClickedListener(listener: (position: Int) -> Unit) {
        onDeleteButtonClickedListener = listener
    }

    fun setOnWarningListener(listener: (warningText: String) -> Unit) {
        onWarningListener = listener
    }
}