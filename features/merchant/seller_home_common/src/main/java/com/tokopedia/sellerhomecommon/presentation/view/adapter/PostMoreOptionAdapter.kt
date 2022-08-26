package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.databinding.ItemShcPostMoreOptionBinding

/**
 * Created by @ilhamsuaib on 26/08/22.
 */

class PostMoreOptionAdapter(
    private val items: List<String>,
    private val onClicked: () -> Unit
) : RecyclerView.Adapter<PostMoreOptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShcPostMoreOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onClicked)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ItemShcPostMoreOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, onClicked: () -> Unit) {
            binding.tvShcPostMoreTitle.text = item
            binding.root.setOnClickListener {
                onClicked()
            }
        }
    }
}