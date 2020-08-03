package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ImageReviewUiModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder.BaseImageReviewViewHolder
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder.DefaultImageReviewViewHolder
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder.ImageReviewViewHolder
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.listener.OnAddImageClickListener

class ImageReviewAdapter(private val onAddImageClickListener: OnAddImageClickListener) : RecyclerView.Adapter<BaseImageReviewViewHolder<*>>() {

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
                DefaultImageReviewViewHolder(LayoutInflater.from(view.context).inflate(R.layout.item_add_image_review_old, view, false), onAddImageClickListener)
            }
            TYPE_IMAGE -> {
                return ImageReviewViewHolder(LayoutInflater.from(view.context).inflate(R.layout.item_image_chooser_review_old, view, false), onAddImageClickListener)
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

}
