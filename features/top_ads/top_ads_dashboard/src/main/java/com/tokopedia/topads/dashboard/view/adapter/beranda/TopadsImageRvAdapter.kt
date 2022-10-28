package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.dashboard.data.model.beranda.ImageModel
import com.tokopedia.topads.dashboard.databinding.ItemSingleImageTopadsBinding

class TopadsImageRvAdapter : RecyclerView.Adapter<TopadsImageRvAdapter.ViewHolder>() {

    private val list = mutableListOf<ImageModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSingleImageTopadsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addItems(items: List<ImageModel>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val binding: ItemSingleImageTopadsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageModel) {
            binding.image.setImageUrl(item.imageUrl)
            if (item.overLappingText.isNullOrBlank())
                binding.txtOverlappingText.hide()
            else {
                binding.txtOverlappingText.show()
                binding.txtOverlappingText.text = item.overLappingText
            }
        }
    }
}