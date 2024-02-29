package com.tokopedia.bmsm_widget.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.bmsm_widget.databinding.ItemProductGiftBinding
import com.tokopedia.bmsm_widget.presentation.adapter.diffutil.ProductGiftDiffUtilCallback
import com.tokopedia.bmsm_widget.presentation.adapter.viewholder.ProductGiftViewHolder
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.kotlin.extensions.view.ONE

/**
 * Created by @ilhamsuaib on 06/12/23.
 */

class GiftWidgetAdapter : ListAdapter<ProductGiftUiModel, ProductGiftViewHolder>(
    ProductGiftDiffUtilCallback.createDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductGiftViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductGiftBinding.inflate(inflater, parent, false)
        return ProductGiftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductGiftViewHolder, position: Int) {
        val itemWidthMatchParent = itemCount <= Int.ONE
        holder.bind(getItem(position), itemWidthMatchParent)
    }
}