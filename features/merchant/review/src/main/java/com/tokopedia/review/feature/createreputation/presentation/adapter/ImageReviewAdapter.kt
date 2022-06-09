package com.tokopedia.review.feature.createreputation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.BaseImageReviewViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.DefaultImageReviewViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.ImageReviewViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.old.VideoReviewViewHolder

class ImageReviewAdapter(
    private val imageClickListener: ImageClickListener,
    private val videoListener: VideoReviewViewHolder.Listener
) : RecyclerView.Adapter<BaseImageReviewViewHolder<*>>() {

    companion object {
        const val TYPE_DEFAULT = 1
        const val TYPE_IMAGE = 2
        const val TYPE_VIDEO = 3
    }

    private var imageReviewData: MutableList<Any> = mutableListOf()

    fun setImageReviewData(data: List<Any>) {
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
            TYPE_VIDEO -> {
                return VideoReviewViewHolder(LayoutInflater.from(view.context).inflate(R.layout.item_video_chooser_review, view, false), videoListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseImageReviewViewHolder<*>, position: Int) {
        when (holder) {
            is DefaultImageReviewViewHolder -> holder.bind((imageReviewData[position] as DefaultImageReviewUiModel))
            is ImageReviewViewHolder -> holder.bind((imageReviewData[position] as ImageReviewUiModel))
            is VideoReviewViewHolder -> holder.bind((imageReviewData[position] as CreateReviewMediaUiModel.Video))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (imageReviewData[position]) {
            is DefaultImageReviewUiModel -> TYPE_DEFAULT
            is ImageReviewUiModel -> TYPE_IMAGE
            is CreateReviewMediaUiModel.Video -> TYPE_VIDEO
            else -> 0
        }
    }

    override fun getItemCount(): Int = imageReviewData.size

    fun isEmpty(): Boolean {
        return imageReviewData.count {
            it is ImageReviewUiModel || it is CreateReviewMediaUiModel.Video
        }.isZero()
    }
}
