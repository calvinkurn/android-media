package com.tokopedia.campaign.components.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.viewholder.ChooseProductViewHolder
import com.tokopedia.campaign.databinding.ItemChooseProductBinding
import com.tokopedia.campaign.entity.ChooseProductItem

class ChooseProductDelegateAdapter:
    DelegateAdapter<ChooseProductItem, ChooseProductViewHolder>(ChooseProductItem::class.java) {

    private var listener: ChooseProductListener? = null

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemChooseProductBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ChooseProductViewHolder(binding, listener)
    }

    override fun bindViewHolder(item: ChooseProductItem, viewHolder: ChooseProductViewHolder) {
        viewHolder.bind(item)
    }

    fun setListener(listener: ChooseProductListener) {
        this.listener = listener
    }

    interface ChooseProductListener {
        fun onChooseProductClicked(index: Int, item: ChooseProductItem)
        fun onDetailClicked(index: Int, item: ChooseProductItem)
    }
}