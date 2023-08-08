package com.tokopedia.product.detail.view.viewholder.bmgm.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class BMGMProductItemAdapter : ListAdapter<BMGMWidgetUiModel.Product, BMGMProductItemViewHolder>(
    DIFF_ITEMS
) {

    companion object {
        private val DIFF_ITEMS = object : DiffUtil.ItemCallback<BMGMWidgetUiModel.Product>() {
            override fun areItemsTheSame(
                oldItem: BMGMWidgetUiModel.Product,
                newItem: BMGMWidgetUiModel.Product
            ): Boolean = oldItem.loadMoreText == newItem.loadMoreText &&
                oldItem.imageUrl == newItem.imageUrl

            override fun areContentsTheSame(
                oldItem: BMGMWidgetUiModel.Product,
                newItem: BMGMWidgetUiModel.Product
            ): Boolean = oldItem == newItem
        }
    }

    private var onClick: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BMGMProductItemViewHolder {
        return BMGMProductItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BMGMProductItemViewHolder, position: Int) {
        val product = getItem(position) ?: return

        holder.bind(product = product)

        holder.itemView.setOnClickListener {
            onClick.invoke()
        }
    }

    fun submit(products: List<BMGMWidgetUiModel.Product>, onClick: () -> Unit) {
        this.onClick = onClick
        submitList(products)
    }
}
