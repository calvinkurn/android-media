package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantValueViewHolder

class VariantValueAdapter(private val removeButtonClickListener: OnRemoveButtonClickListener, private val layoutPosition: Int) :
        RecyclerView.Adapter<VariantValueViewHolder>(),
        VariantValueViewHolder.OnRemoveButtonClickListener {

    interface OnRemoveButtonClickListener {
        fun onRemoveButtonClicked(position: Int, layoutPosition: Int)
    }

    private var items: MutableList<UnitValue> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantValueViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_variant_value, parent, false)
        return VariantValueViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VariantValueViewHolder, position: Int) {
        val variantUnitValue = items[position]
        holder.bindData(variantUnitValue.value)
    }

    override fun onRemoveButtonClicked(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        removeButtonClickListener.onRemoveButtonClicked(position, layoutPosition)
    }

    fun setData(items: List<UnitValue>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addData(item: UnitValue) {
        this.items.add(item)
        notifyDataSetChanged()
    }
}