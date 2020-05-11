package com.tokopedia.play.ui.variantsheet.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R
import com.tokopedia.play.ui.variantsheet.adapter.VariantOptionPlaceholderAdapter
import com.tokopedia.play.view.uimodel.VariantPlaceholderUiModel

/**
 * Created by jegul on 14/03/20
 */
class VariantPlaceholderViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val rvPhVariantOption: RecyclerView = itemView.findViewById(R.id.rv_ph_variant_option)

    private val optionPlaceholderAdapter = VariantOptionPlaceholderAdapter()

    init {
        rvPhVariantOption.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        rvPhVariantOption.adapter = optionPlaceholderAdapter
    }

    fun bind(item: VariantPlaceholderUiModel.Category) {
        optionPlaceholderAdapter.setItems(item.optionPlaceholderList)
        optionPlaceholderAdapter.notifyDataSetChanged()
    }
}