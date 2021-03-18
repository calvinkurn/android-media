package com.tokopedia.product.addedit.description.presentation.adapter

import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import kotlinx.android.synthetic.main.item_product_add_video.view.*

class VideoLinkTypeFactory: BaseAdapterTypeFactory(){
    private var listener: VideoLinkListener? = null

    fun setVideoLinkListener(listener: VideoLinkListener){
        this.listener = listener
    }

    fun type(videoLinkModel: VideoLinkModel): Int = VideoLinkViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            VideoLinkViewHolder.LAYOUT -> VideoLinkViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    class VideoLinkViewHolder(val view: View?, private val listener: VideoLinkListener?)
        : AbstractViewHolder<VideoLinkModel>(view) {

        override fun bind(element: VideoLinkModel) {
            itemView.textFieldUrl.textAreaInput.apply {
                maxLines = 1
                isSingleLine = true
            }
            itemView.textFieldUrl.apply {
                isLabelStatic = false
                textAreaLabel = getString(R.string.label_video_url_placeholder)
                textAreaPlaceholder = getString(R.string.label_video_url_placeholder)
            }
            itemView.textFieldUrl.apply {
                if (element.inputUrl.isNotEmpty()) {
                    setText(element.inputUrl)
                }
                textAreaInput.let {
                    it.setSelection(element.inputUrl.length)
                    it.doAfterTextChanged { editable ->
                        listener?.onTextChanged(editable.toString(), adapterPosition)
                    }
                }
            }

            loadLayout(element.inputUrl, element.inputImage, element.inputTitle,
                    element.inputDescription, element.errorMessage)

            itemView.textFieldUrl.textAreaIconClose.setOnClickListener {
                itemView.textFieldUrl.clearFocus()
                listener?.onDeleteClicked(element, adapterPosition)
            }

        }

        private fun loadLayout(inputUrl: String,
                               imageUrl: String,
                               inputTitle: String,
                               inputDescription: String,
                               errorMessage: String) {
            itemView.apply {
                cardThumbnail.visibility = if (inputTitle.isEmpty()) View.GONE else View.VISIBLE
                itemView.textFieldUrl.textAreaIconClose.visibility = if (inputUrl.isEmpty()) View.GONE else View.VISIBLE

                try {
                    if(imgThumbnail?.context?.isValidGlideContext() == true)
                        imgThumbnail.urlSrc = imageUrl
                } catch (e: Throwable) { }

                tvVideoTitle.text = inputTitle
                tvVideoSubtitle.text = inputDescription

                if (errorMessage.isNotEmpty() && textFieldUrl.textAreaInput.text.isNotBlank()) {
                    textFieldUrl.isError = true
                    textFieldUrl.textAreaMessage = errorMessage
                } else {
                    textFieldUrl.isError = false
                    textFieldUrl.textAreaMessage = ""
                }

                cardThumbnail.setOnClickListener { listener?.onThumbnailClicked(inputUrl) }
            }
        }

        companion object {
            val LAYOUT = R.layout.item_product_add_video
        }
    }

    interface VideoLinkListener {
        fun onDeleteClicked(videoLinkModel: VideoLinkModel, position: Int)
        fun onTextChanged(url: String, position: Int)
        fun onThumbnailClicked(url: String)
    }
}