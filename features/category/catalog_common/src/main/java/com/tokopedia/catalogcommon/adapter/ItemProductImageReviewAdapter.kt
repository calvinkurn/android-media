package com.tokopedia.catalogcommon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.databinding.ItemBuyerImageProductReviewBinding
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel
import com.tokopedia.media.loader.JvmMediaLoader
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ItemProductImageReviewAdapter(
    private val itemList: List<BuyerReviewUiModel.ImgReview>,
    private val onItemClick: (position: Int) -> Unit = {}
): RecyclerView.Adapter<ItemProductImageReviewAdapter.ViewHolder>(){

    private val lastImageIndexToShow = 3
    private val totalMaxImgShown = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductImageReviewAdapter.ViewHolder {
        val binding = ItemBuyerImageProductReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ItemProductImageReviewAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int {
        return if (itemList.size <= lastImageIndexToShow) {
            itemList.size
        } else {
            totalMaxImgShown
        }
    }

    inner class ViewHolder(
        itemView: ItemBuyerImageProductReviewBinding,
        onItemClick: (position: Int) -> Unit
    ): RecyclerView.ViewHolder(itemView.root) {

        private val imgProduct: ImageUnify = itemView.cardBrImgProduct
        private val bgProductOverlay: View = itemView.cardBrImgProductTextOverlay
        private val txtProductCounter: Typography = itemView.tgpProductReviewCounter

        init {
            itemView.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(itemUiModel: BuyerReviewUiModel.ImgReview, position: Int) {
            JvmMediaLoader.loadImage(imgProduct, itemUiModel.imgUrl)

            if (position == lastImageIndexToShow && itemList.size > totalMaxImgShown) {
                bgProductOverlay.visibility = View.VISIBLE
                txtProductCounter.visibility = View.VISIBLE
                txtProductCounter.text = "+${itemList.size - totalMaxImgShown} Lainnya"
            } else {
                bgProductOverlay.visibility = View.GONE
                txtProductCounter.visibility = View.GONE
            }
        }
    }

}
