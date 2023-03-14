package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemAsgcLabelBinding

/**
 * Created By : Muhammad Furqan on 14/03/23
 */
class FeedAsgcTagAdapter(private val labelList: List<String>) :
    RecyclerView.Adapter<FeedAsgcTagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAsgcLabelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(labelList[position])
    }

    override fun getItemCount(): Int = labelList.size

    class ViewHolder(val binding: ItemAsgcLabelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(label: String) {
            binding.tyFeedAsgcLabel.text = label
        }
    }
}
