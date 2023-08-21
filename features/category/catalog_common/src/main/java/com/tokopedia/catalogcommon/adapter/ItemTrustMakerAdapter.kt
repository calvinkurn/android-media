package com.tokopedia.catalogcommon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.databinding.ItemSliderTextImageBinding
import com.tokopedia.catalogcommon.databinding.ItemTrustmakerBinding
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageRounded

class ItemTrustMakerAdapter(private val itemList: List<TrustMakerUiModel.ItemTrustMakerUiModel>) : RecyclerView.Adapter<ItemTrustMakerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrustmakerBinding.inflate(
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

    inner class ViewHolder(itemView: ItemTrustmakerBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val ivImage = itemView.ivImage
        private val tvSubTitle = itemView.tvSubTitle
        private val tvTitle = itemView.tvTitle

        init {
            val widthPercentage = 0.60
            val recyclerViewWidth = itemView.root.resources.displayMetrics.widthPixels

            val desiredWidth = (recyclerViewWidth * widthPercentage).toInt()
            itemView.root.layoutParams.width = desiredWidth
        }

        fun bindToView(itemUiModel: TrustMakerUiModel.ItemTrustMakerUiModel){
            ivImage.loadImage(itemUiModel.icon)
            tvTitle.text = itemUiModel.title
            tvSubTitle.text = itemUiModel.subTitle
            overrideWidgetTheme(itemUiModel.textColor)
        }

        private fun overrideWidgetTheme(fontColor: Int){
            tvTitle.setTextColor(fontColor)
            tvSubTitle.setTextColor(fontColor)
        }

    }
}
