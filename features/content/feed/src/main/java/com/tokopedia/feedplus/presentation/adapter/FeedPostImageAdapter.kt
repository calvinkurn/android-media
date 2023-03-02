package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedPostImageBinding

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
class FeedPostImageAdapter(val data: List<String>) :
    RecyclerView.Adapter<FeedPostImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemFeedPostImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val binding: ItemFeedPostImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            binding.imgFeedPost.setImageUrl(url)
        }
    }
}
