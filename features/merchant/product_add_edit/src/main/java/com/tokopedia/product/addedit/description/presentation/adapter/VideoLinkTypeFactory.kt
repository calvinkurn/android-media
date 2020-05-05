package com.tokopedia.product.addedit.description.presentation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.replaceTextAndRestoreCursorPosition
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

        var isFirstLoaded = true

        var textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                listener?.onTextChanged(editable.toString(), adapterPosition)
            }
        }

        override fun bind(element: VideoLinkModel) {
            itemView.textFieldUrl.textAreaInput.apply {
                maxLines = 1
                setSingleLine(true)
                setMargin(0, 0, context.resources.getDimension(R.dimen.layout_lvl2).toInt(), 0)
            }
            itemView.textFieldUrl.apply {
                isLabelStatic = false
                textAreaLabel = getString(R.string.label_video_url_placeholder)
                textAreaPlaceholder = getString(R.string.label_video_url_placeholder)
                textAreaIconClose.layoutParams.apply {
                    width = context.resources.getDimension(R.dimen.video_clear_icon_size).toInt()
                    height = context.resources.getDimension(R.dimen.video_clear_icon_size).toInt()
                }
            }
            // Remove listener and set the text so it will not trigger textWatcher
            if (isFirstLoaded) {
                itemView.textFieldUrl.apply {
                    textAreaInput.addTextChangedListener(textWatcher)
                    if (element.inputUrl.isNotEmpty()) setText(element.inputUrl)
                }
                isFirstLoaded = false
            } else {
                itemView.textFieldUrl.apply {
                    textAreaInput.removeTextChangedListener(textWatcher)
                    replaceTextAndRestoreCursorPosition(element.inputUrl)
                    textAreaInput.addTextChangedListener(textWatcher)
                    requestFocus()
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
                imgThumbnail.urlSrc = imageUrl
                tvVideoTitle.text = inputTitle
                tvVideoSubtitle.text = inputDescription
                if (errorMessage.isNotEmpty() && !textFieldUrl.textAreaInput.text.isBlank()) {
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