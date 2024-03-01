package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_pdp_emoney.databinding.ItemEmoneyImageCardListBinding

class EmoneyPDPImagesCardListAdapter: RecyclerView.Adapter<EmoneyPDPImagesCardListAdapter.EmoneyPDPImageCardViewHolder>() {

    var imageList = emptyList<String>()

    fun renderList(list: List<String>) {
        imageList = list
        notifyItemChanged(Int.ONE, imageList.size)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: EmoneyPDPImageCardViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmoneyPDPImageCardViewHolder {
        val binding = ItemEmoneyImageCardListBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return EmoneyPDPImageCardViewHolder(binding)
    }

    inner class EmoneyPDPImageCardViewHolder(val binding: ItemEmoneyImageCardListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            with(binding) {
                binding.imgEmoneyCard.loadImage(imageUrl)
            }
        }
    }
}
