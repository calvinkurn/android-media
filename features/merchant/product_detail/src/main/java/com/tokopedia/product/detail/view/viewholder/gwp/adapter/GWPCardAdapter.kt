package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.GWPUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardAdapter : ListAdapter<GWPWidgetUiModel.Card, BMGMProductItemViewHolder>(
    DIFF_ITEMS
) {

    companion object {
        private val DIFF_ITEMS = object : DiffUtil.ItemCallback<GWPWidgetUiModel.Card>() {
            override fun areItemsTheSame(
                oldItem: GWPWidgetUiModel.Card,
                newItem: GWPWidgetUiModel.Card
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: GWPWidgetUiModel.Card,
                newItem: GWPWidgetUiModel.Card
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BMGMProductItemViewHolder {
        return BMGMProductItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BMGMProductItemViewHolder, position: Int) {
        val product = getItem(position) ?: return

        holder.bind(product = product)
    }
}
