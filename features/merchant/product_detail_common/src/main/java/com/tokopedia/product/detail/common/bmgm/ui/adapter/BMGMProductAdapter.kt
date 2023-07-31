package com.tokopedia.product.detail.common.bmgm.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class BMGMProductAdapter : ListAdapter<BMGMUiModel.Product, BMGMProductViewHolder>(DIFF_ITEMS) {

    companion object {
        val DIFF_ITEMS = object : DiffUtil.ItemCallback<BMGMUiModel.Product>() {
            override fun areItemsTheSame(
                oldItem: BMGMUiModel.Product,
                newItem: BMGMUiModel.Product
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: BMGMUiModel.Product,
                newItem: BMGMUiModel.Product
            ): Boolean = oldItem.imageUrl == newItem.imageUrl
        }
    }

    private var loadMoreText = ""
    private var onClick: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BMGMProductViewHolder {
        return BMGMProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BMGMProductViewHolder, position: Int) {
        val product = getItem(position) ?: return

        holder.bind(
            product = product,
            loadMoreText = loadMoreText,
            isEndItem = itemCount.dec() == position
        )

        holder.itemView.setOnClickListener {
            onClick.invoke()
        }
    }

    fun submit(products: List<BMGMUiModel.Product>, loadMoreText: String, onClick: () -> Unit) {
        this.loadMoreText = loadMoreText
        this.onClick = onClick
        submitList(products)
    }
}
