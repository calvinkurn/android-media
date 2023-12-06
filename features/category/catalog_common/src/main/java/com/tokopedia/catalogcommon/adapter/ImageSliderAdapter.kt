package com.tokopedia.catalogcommon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.catalogcommon.databinding.ItemSliderTextImageBinding
import com.tokopedia.catalogcommon.databinding.ItemTrustmakerBinding
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage

class ImageSliderAdapter(private val itemList: List<SliderImageTextUiModel.ItemSliderImageText>) :
    RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSliderTextImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUiModel = itemList[position]
        holder.bindToView(itemUiModel)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: ItemSliderTextImageBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val ivImage = itemView.ivImage
        val tvHighlight = itemView.tvHighlight
        val tvDescription = itemView.tvDescription
        val tvTitle = itemView.tvTitle

        init {
            val widthPercentage = 0.75
            val recyclerViewWidth = itemView.root.resources.displayMetrics.widthPixels
            val desiredWidth = (recyclerViewWidth * widthPercentage).toInt()
            itemView.root.layoutParams.width = desiredWidth
        }
        fun bindToView(itemUiModel: SliderImageTextUiModel.ItemSliderImageText) {

            ivImage.loadImage(itemUiModel.image)
            tvTitle.text = itemUiModel.textDescription
            tvHighlight.text = itemUiModel.textHighlight
            tvDescription.text = itemUiModel.textDescription
        }

    }
}
