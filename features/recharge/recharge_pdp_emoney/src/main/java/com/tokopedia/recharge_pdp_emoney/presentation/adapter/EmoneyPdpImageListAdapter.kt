package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_pdp_emoney.databinding.ItemEmoneyImageListBinding

class EmoneyPdpImagesListAdapter: RecyclerView.Adapter<EmoneyPdpImagesListAdapter.EmoneyPdpImageViewHolder>() {

    var imageList = emptyList<String>()

    fun renderList(list: List<String>) {
        imageList = list
        notifyItemChanged(0, imageList.size)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: EmoneyPdpImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmoneyPdpImageViewHolder {
        val binding = ItemEmoneyImageListBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return EmoneyPdpImageViewHolder(binding)
    }

    inner class EmoneyPdpImageViewHolder(val binding: ItemEmoneyImageListBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(imageUrl: String) {
                with(binding) {
                    binding.itemEmoneyImageList.loadImage(imageUrl)
                }
            }
    }
}
