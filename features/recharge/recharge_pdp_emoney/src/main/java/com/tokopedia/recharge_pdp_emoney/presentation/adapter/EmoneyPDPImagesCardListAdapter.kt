package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_pdp_emoney.databinding.ItemEmoneyImageCardListBinding
import com.tokopedia.recharge_pdp_emoney.presentation.model.EmoneyImageModel

class EmoneyPDPImagesCardListAdapter: RecyclerView.Adapter<EmoneyPDPImagesCardListAdapter.EmoneyPDPImageCardViewHolder>() {

    var imageList = emptyList<EmoneyImageModel>()

    fun renderList(list: List<EmoneyImageModel>) {
        imageList = list
        notifyItemChanged(Int.ZERO, imageList.size)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: EmoneyPDPImageCardViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmoneyPDPImageCardViewHolder {
        val binding = ItemEmoneyImageCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmoneyPDPImageCardViewHolder(binding)
    }

    inner class EmoneyPDPImageCardViewHolder(val binding: ItemEmoneyImageCardListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(image: EmoneyImageModel) {
            with(binding) {
                imgEmoneyCard.loadImage(image.imageUrl)
            }
        }
    }
}
