package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home_component.listener.BestSellerChipListener
import com.tokopedia.home_component.viewholders.BestSellerChipViewHolder
import com.tokopedia.home_component.visitable.BestSellerChipDataModel

internal class BestSellerChipAdapter(
    private val listener: BestSellerChipListener
): ListAdapter<BestSellerChipDataModel, BestSellerChipViewHolder>(
    BestSellerChipDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestSellerChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(BestSellerChipViewHolder.LAYOUT, parent, false)
        return BestSellerChipViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: BestSellerChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BestSellerChipDiffUtil: DiffUtil.ItemCallback<BestSellerChipDataModel>() {
        override fun areItemsTheSame(
            oldItem: BestSellerChipDataModel,
            newItem: BestSellerChipDataModel
        ): Boolean = oldItem.title == newItem.title

        override fun areContentsTheSame(
            oldItem: BestSellerChipDataModel,
            newItem: BestSellerChipDataModel
        ): Boolean = oldItem.isActivated == newItem.isActivated
    }
}
