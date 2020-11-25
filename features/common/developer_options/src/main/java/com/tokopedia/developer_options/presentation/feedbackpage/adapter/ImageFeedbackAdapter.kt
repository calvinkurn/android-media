package com.tokopedia.developer_options.presentation.feedbackpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.DefaultFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.ImageFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.listener.ImageClickListener
import com.tokopedia.developer_options.presentation.feedbackpage.viewholder.BaseImageFeedbackViewHolder
import com.tokopedia.developer_options.presentation.feedbackpage.viewholder.DefaultImageFeedbackViewHolder
import com.tokopedia.developer_options.presentation.feedbackpage.viewholder.ImageFeedbackViewHolder

class ImageFeedbackAdapter(private val imageClickListener: ImageClickListener): RecyclerView.Adapter<BaseImageFeedbackViewHolder<*>>() {

    companion object {
        const val TYPE_DEFAULT = 1
        const val TYPE_IMAGE = 2
    }

    private var imageFeedbackData: MutableList<BaseImageFeedbackUiModel> = mutableListOf()

    fun setImageFeedbackData(data: List<BaseImageFeedbackUiModel>) {
        imageFeedbackData = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return (imageFeedbackData[position] as ImageFeedbackUiModel).imageUrl.toLongOrNull()?: RecyclerView.NO_ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseImageFeedbackViewHolder<*> {
        return when (viewType) {
            TYPE_DEFAULT -> {
                DefaultImageFeedbackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_image_feedback, parent, false), imageClickListener)
            }
            TYPE_IMAGE -> {
                ImageFeedbackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_chooser_feedback, parent, false), imageClickListener)
            }
            else -> throw IllegalArgumentException("Invalid")
        }
    }

    override fun getItemCount(): Int = imageFeedbackData.size

    override fun onBindViewHolder(holder: BaseImageFeedbackViewHolder<*>, position: Int) {
        when (holder) {
            is DefaultImageFeedbackViewHolder -> holder.bind((imageFeedbackData[position] as DefaultFeedbackUiModel))
            is ImageFeedbackViewHolder -> holder.bind((imageFeedbackData[position] as ImageFeedbackUiModel))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (imageFeedbackData[position]) {
            is DefaultFeedbackUiModel -> TYPE_DEFAULT
            is ImageFeedbackUiModel -> TYPE_IMAGE
            else -> 0
        }
    }

    fun isEmpty(): Boolean {
        return imageFeedbackData.filterIsInstance<ImageFeedbackUiModel>().isEmpty()
    }

}