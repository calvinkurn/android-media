package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_pdp_emoney.databinding.ItemEmoneyImageBottomSheetBinding
import com.tokopedia.recharge_pdp_emoney.presentation.model.EmoneyImageModel

class EmoneyPDPImagesListBottomSheetAdapter: RecyclerView.Adapter<EmoneyPDPImagesListBottomSheetAdapter.EmoneyPDPImageBottomSheetViewHolder>() {

    var imageList = emptyList<EmoneyImageModel>()

    fun renderList(list: List<EmoneyImageModel>) {
        imageList = list
        notifyItemChanged(Int.ZERO, imageList.size)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: EmoneyPDPImageBottomSheetViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmoneyPDPImageBottomSheetViewHolder {
        val binding = ItemEmoneyImageBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return EmoneyPDPImageBottomSheetViewHolder(binding)
    }

    inner class EmoneyPDPImageBottomSheetViewHolder(val binding: ItemEmoneyImageBottomSheetBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(image: EmoneyImageModel) {
            with(binding) {
                imgBottomSheetEmoney.loadImage(image.imageUrl)
                tgBottomSheetEmoney.text = image.operatorTitle
            }
        }
    }
}
