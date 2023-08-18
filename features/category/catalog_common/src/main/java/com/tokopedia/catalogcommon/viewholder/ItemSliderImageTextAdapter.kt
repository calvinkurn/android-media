package com.tokopedia.catalogcommon.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.databinding.ItemSliderTextImageBinding
import com.tokopedia.kotlin.extensions.view.loadImageRounded

class ItemSliderImageTextAdapter(private val itemList: List<SliderImageTextUiModel.ItemSliderImageText>) : RecyclerView.Adapter<ItemSliderImageTextAdapter.ViewHolder>() {


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
//        holder.bindToView(itemUiModel)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: ItemSliderTextImageBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val ivImage = itemView.ivImage
//        private val tvHighlight = itemView.tvHighlight
//        private val tvTitle = itemView.tvTitle
//        private val tvDescription = itemView.tvDescription

        init {
            val widthPercentage = 0.65
            val recyclerViewWidth = itemView.root.resources.displayMetrics.widthPixels

            val desiredWidth = (recyclerViewWidth * widthPercentage).toInt()
            itemView.root.layoutParams.width = desiredWidth
        }

//        fun bindToView(itemUiModel: SliderImageTextUiModel.ItemSliderImageText){
//            ivImage.loadImageRounded(itemUiModel.image,8f)
//            tvHighlight.text = itemUiModel.textHighlight
//            tvTitle.text = itemUiModel.textTitle
//            tvDescription.text = itemUiModel.textDescription
//            overrideWidgetTheme(itemUiModel.textColor)
//        }
//
//        private fun overrideWidgetTheme(fontColor: Int){
//            tvHighlight.setTextColor(fontColor)
//            tvTitle.setTextColor(fontColor)
//            tvDescription.setTextColor(fontColor)
//        }

    }
}
