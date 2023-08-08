package com.tokopedia.product.detail.common.bmgm.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class BMGMProductItemAdapter : ListAdapter<BMGMUiModel.Product, BMGMProductItemViewHolder>(DIFF_ITEMS) {

    companion object {
        private val DIFF_ITEMS = object : DiffUtil.ItemCallback<BMGMUiModel.Product>() {
            override fun areItemsTheSame(
                oldItem: BMGMUiModel.Product,
                newItem: BMGMUiModel.Product
            ): Boolean = oldItem.loadMoreText == newItem.loadMoreText &&
                oldItem.imageUrl == newItem.imageUrl

            override fun areContentsTheSame(
                oldItem: BMGMUiModel.Product,
                newItem: BMGMUiModel.Product
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

    fun submit(products: List<BMGMUiModel.Product>, onClick: () -> Unit) {
        this.onClick = onClick
        submitList(products)
    }
}
