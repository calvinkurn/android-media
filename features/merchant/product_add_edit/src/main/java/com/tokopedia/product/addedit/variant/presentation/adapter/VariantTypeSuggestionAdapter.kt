package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.AllVariant
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeSuggestionViewHolder

class VariantTypeSuggestionAdapter : RecyclerView.Adapter<VariantTypeSuggestionViewHolder>() {

    private var highlightCharLength: Int = 0
    private var items: MutableList<AllVariant> = mutableListOf()
    private var onItemClickedListener: ((position: Int, variantTypeName: String) -> Unit)? = null
    private var firstItemDisabled = false

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantTypeSuggestionViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_variant_type_suggestion, parent, false)
        return initViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: VariantTypeSuggestionViewHolder, position: Int) {
        val variantUnitValue = items[position].variantName
        val enabled = !(position.isZero() && firstItemDisabled)
        holder.bindData(variantUnitValue, highlightCharLength, enabled)
    }

    private fun initViewHolder(rootView: View): VariantTypeSuggestionViewHolder {
        val viewHolder = VariantTypeSuggestionViewHolder(rootView)

        // only set listener if the text is enabled
        viewHolder.binding?.root?.setOnClickListener {
            if (viewHolder.binding?.variantTypeName?.isEnabled.orFalse()) {
                val position = viewHolder.adapterPosition
                onItemClickedListener?.invoke(position, items[position].variantName)
            }
        }
        return viewHolder
    }

    fun setData(items: List<AllVariant>) {
        this.items = items.toMutableList()
        this.items.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, {
            it.variantName
        }))
        notifyDataSetChanged()
    }

    fun getItemFromVariantName(variantTypeName: String): AllVariant? {
        return items.find { it.variantName.equals(variantTypeName, ignoreCase = true) }
    }

    fun setHighlightCharLength(length: Int) {
        highlightCharLength = length
        notifyDataSetChanged()
    }

    fun setDisableFirst(isDisabled: Boolean) {
        firstItemDisabled = isDisabled
        notifyItemChanged(Int.ZERO)
    }

    fun setOnItemClickedListener(listener: (position: Int, variantTypeName: String) -> Unit) {
        onItemClickedListener = listener
    }
}