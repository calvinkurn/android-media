package com.tokopedia.review.feature.createreputation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.BaseImageReviewViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.DefaultImageReviewViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.ImageReviewViewHolder

class ImageReviewAdapter(private val imageClickListener: ImageClickListener) : RecyclerView.Adapter<BaseImageReviewViewHolder<*>>() {

    companion object {
        const val TYPE_DEFAULT = 1
        const val TYPE_IMAGE = 2
    }

    private var imageReviewData: MutableList<BaseImageReviewUiModel> = mutableListOf()

    fun setImageReviewData(data: List<BaseImageReviewUiModel>) {
        imageReviewData = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(view: ViewGroup, position: Int): BaseImageReviewViewHolder<*> {
        return when (position) {
            TYPE_DEFAULT -> {
                DefaultImageReviewViewHolder(LayoutInflater.from(view.context).inflate(R.layout.item_add_image_review, view, false), imageClickListener)
            }
            TYPE_IMAGE -> {
                return ImageReviewViewHolder(LayoutInflater.from(view.context).inflate(R.layout.item_image_chooser_review, view, false), imageClickListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseImageReviewViewHolder<*>, position: Int) {
        when (holder) {
            is DefaultImageReviewViewHolder -> holder.bind((imageReviewData[position] as DefaultImageReviewUiModel))
            is ImageReviewViewHolder -> holder.bind((imageReviewData[position] as ImageReviewUiModel))

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (imageReviewData[position]) {
            is DefaultImageReviewUiModel -> TYPE_DEFAULT
            is ImageReviewUiModel -> TYPE_IMAGE
            else -> 0
        }
    }

    override fun getItemCount(): Int = imageReviewData.size

    fun isEmpty(): Boolean {
        return imageReviewData.filterIsInstance<ImageReviewUiModel>().isEmpty()
    }
}
