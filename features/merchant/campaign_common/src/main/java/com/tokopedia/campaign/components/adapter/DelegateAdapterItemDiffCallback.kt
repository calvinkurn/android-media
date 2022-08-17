package com.tokopedia.campaign.components.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

internal class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<DelegateAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: DelegateAdapterItem,
        newItem: DelegateAdapterItem
    ): Boolean {
        return oldItem::class == newItem::class && oldItem.id() == newItem.id()
    }


    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: DelegateAdapterItem,
        newItem: DelegateAdapterItem
    ): Boolean {
        return oldItem.content() == newItem.content()
    }
}