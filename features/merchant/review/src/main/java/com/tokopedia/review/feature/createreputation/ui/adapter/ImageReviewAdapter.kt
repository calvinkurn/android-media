package com.tokopedia.review.feature.createreputation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.review.R
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewViewModel
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewViewModel
import com.tokopedia.review.feature.createreputation.ui.listener.OnAddImageClickListener
import com.tokopedia.review.feature.createreputation.ui.viewholder.BaseImageReviewViewHolder
import com.tokopedia.review.feature.createreputation.ui.viewholder.DefaultImageReviewViewHolder
import com.tokopedia.review.feature.createreputation.ui.viewholder.ImageReviewViewHolder

class ImageReviewAdapter(private val onAddImageClickListener: OnAddImageClickListener) : RecyclerView.Adapter<BaseImageReviewViewHolder<*>>() {

    companion object {
        const val TYPE_DEFAULT = 1
        const val TYPE_IMAGE = 2
    }

    private var imageReviewData: MutableList<BaseImageReviewViewModel> = mutableListOf()

    fun setImageReviewData(data: List<BaseImageReviewViewModel>) {
        imageReviewData = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(view: ViewGroup, position: Int): BaseImageReviewViewHolder<*> {
        return when (position) {
            TYPE_DEFAULT -> {
                DefaultImageReviewViewHolder(LayoutInflater.from(view.context).inflate(R.layout.item_add_image_review, view, false), onAddImageClickListener)
            }
            TYPE_IMAGE -> {
                return ImageReviewViewHolder(LayoutInflater.from(view.context).inflate(R.layout.item_image_chooser_review, view, false), onAddImageClickListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseImageReviewViewHolder<*>, position: Int) {
        when (holder) {
            is DefaultImageReviewViewHolder -> holder.bind((imageReviewData[position] as DefaultImageReviewModel))
            is ImageReviewViewHolder -> holder.bind((imageReviewData[position] as ImageReviewViewModel))

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (imageReviewData[position]) {
            is DefaultImageReviewModel -> TYPE_DEFAULT
            is ImageReviewViewModel -> TYPE_IMAGE
            else -> 0
        }
    }

    override fun getItemCount(): Int = imageReviewData.size

}
