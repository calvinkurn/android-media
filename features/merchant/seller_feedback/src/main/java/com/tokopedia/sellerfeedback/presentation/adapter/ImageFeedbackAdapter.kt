package com.tokopedia.sellerfeedback.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.presentation.uimodel.AddImageFeedbackUiModel
import com.tokopedia.sellerfeedback.presentation.uimodel.BaseImageFeedbackUiModel
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.sellerfeedback.presentation.viewholder.AddImageFeedbackViewHolder
import com.tokopedia.sellerfeedback.presentation.viewholder.BaseImageFeedbackViewHolder
import com.tokopedia.sellerfeedback.presentation.viewholder.ImageFeedbackViewHolder

class ImageFeedbackAdapter(private val imageClickListener: BaseImageFeedbackViewHolder.ImageClickListener) : RecyclerView.Adapter<BaseImageFeedbackViewHolder<*>>() {

    companion object {
        const val TYPE_DEFAULT = 1
        const val TYPE_IMAGE = 2
        const val MAX_IMAGE = 3
    }

    private val addImageFeedbackUiModel by lazy { AddImageFeedbackUiModel() }

    private var imageFeedbackData = mutableListOf<BaseImageFeedbackUiModel>()

    fun setImageFeedbackData(data: List<ImageFeedbackUiModel>) {
        imageFeedbackData = data.toMutableList()
        checkAddImageFeedback()
        notifyDataSetChanged()
    }

    fun removeImage(item: BaseImageFeedbackUiModel) {
        imageFeedbackData.remove(item)
        checkAddImageFeedback()
        notifyDataSetChanged()
    }

    private fun checkAddImageFeedback() {
        with(imageFeedbackData) {
            if (size < MAX_IMAGE && last() !is AddImageFeedbackUiModel) {
                imageFeedbackData.add(addImageFeedbackUiModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseImageFeedbackViewHolder<*> {
        return when (viewType) {
            TYPE_DEFAULT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_image_feedback, parent, false)
                AddImageFeedbackViewHolder(view, imageClickListener)
            }
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_chooser_feedback, parent, false)
                ImageFeedbackViewHolder(view, imageClickListener)
            }
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun onBindViewHolder(holder: BaseImageFeedbackViewHolder<*>, position: Int) {
        when (holder) {
            is AddImageFeedbackViewHolder -> holder.bind((imageFeedbackData[position] as AddImageFeedbackUiModel))
            is ImageFeedbackViewHolder -> holder.bind((imageFeedbackData[position] as ImageFeedbackUiModel))
        }
    }

    override fun getItemCount() = imageFeedbackData.size

    override fun getItemViewType(position: Int): Int {
        return when (imageFeedbackData[position]) {
            is AddImageFeedbackUiModel -> TYPE_DEFAULT
            is ImageFeedbackUiModel -> TYPE_IMAGE
            else -> 0
        }
    }
}