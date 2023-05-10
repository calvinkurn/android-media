package com.tokopedia.addon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemAddonBinding

class AddOnViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_addon, parent, false)
    }

    private val binding: ItemAddonBinding? by viewBinding()

    fun bind(item: Pair<String, List<String>>) {
        binding?.apply {
            tfTitle.text = item.first
            rvAddonChild.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            rvAddonChild.adapter = AddOnChildAdapter().apply {
                setItems(item.second)
            }
        }
    }

}
