package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.databinding.AddEditProductItemVariantUnitBinding
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantUnitViewHolder

class VariantUnitAdapter(
): RecyclerView.Adapter<VariantUnitViewHolder>() {

    private var data: MutableList<Unit> = mutableListOf()
    private var selectedVariantUnit: Unit? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantUnitViewHolder {
        val binding = AddEditProductItemVariantUnitBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VariantUnitViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: VariantUnitViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it, selectedVariantUnit)
        }
    }

    fun addDataList(newData: List<Unit>) {
        val oldDataSize = data.size
        data.addAll(newData)
        notifyItemRangeInserted(oldDataSize, oldDataSize + newData.size)
    }

    fun setSelected(variantUnit: Unit) {
        selectedVariantUnit = variantUnit
    }
}
