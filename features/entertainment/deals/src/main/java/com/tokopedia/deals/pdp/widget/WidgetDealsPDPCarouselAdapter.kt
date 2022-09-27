package com.tokopedia.deals.pdp.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.databinding.ItemDealsPdpImageSliderBinding
import com.tokopedia.media.loader.loadImage

class WidgetDealsPDPCarouselAdapter(private val images: MutableList<String>) :
    RecyclerView.Adapter<WidgetDealsPDPCarouselAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemDealsPdpImageSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SliderViewHolder(binding)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun addImages(list: List<String>) {
        images.clear()
        images.addAll(list)
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(val binding: ItemDealsPdpImageSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            with(binding) {
                imageBanner.loadImage(url)
            }
        }
    }
}
