package com.tokopedia.product.addedit.description.presentation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.WEB_PREFIX_HTTPS
import com.tokopedia.product.addedit.common.util.replaceTextAndRestoreCursorPosition
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

class VideoLinkTypeFactory : BaseAdapterTypeFactory() {
    private var listener: VideoLinkListener? = null

    fun setVideoLinkListener(listener: VideoLinkListener) {
        this.listener = listener
    }

    fun type(videoLinkModel: VideoLinkModel): Int = VideoLinkViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VideoLinkViewHolder.LAYOUT -> VideoLinkViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    class VideoLinkViewHolder(val view: View?, private val listener: VideoLinkListener?) :
        AbstractViewHolder<VideoLinkModel>(view) {

        private var imgThumbnail: ImageUnify? = null
        private var textFieldUrl: TextAreaUnify? = null
        private var cardThumbnail: CardUnify? = null
        private var tvVideoTitle: Typography? = null
        private var tvVideoSubtitle: Typography? = null
        private var isFirstLoaded = true

        private var textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                listener?.onTextChanged(editable.toString(), adapterPosition)
            }
        }

        override fun bind(element: VideoLinkModel) {
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail)
            textFieldUrl = itemView.findViewById(R.id.textFieldUrl)
            cardThumbnail = itemView.findViewById(R.id.cardThumbnail)
            tvVideoTitle = itemView.findViewById(R.id.tvVideoTitle)
            tvVideoSubtitle = itemView.findViewById(R.id.tvVideoSubtitle)

            textFieldUrl?.textAreaInput?.apply {
                maxLines = 1
                isSingleLine = true
            }

            textFieldUrl?.apply {
                isLabelStatic = false
                textAreaLabel = getString(R.string.label_video_url_placeholder)
                textAreaPlaceholder = getString(R.string.label_video_url_placeholder)
            }

            if (isFirstLoaded) {
                textFieldUrl?.apply {
                    textAreaInput.addTextChangedListener(textWatcher)
                    if (element.inputUrl.isNotEmpty()) {
                        setText(element.inputUrl)
                    }
                    if (!element.inputUrl.startsWith(WEB_PREFIX_HTTPS) && element.inputUrl.isNotBlank()) {
                        setText("$WEB_PREFIX_HTTPS${element.inputUrl}")
                    }
                }
                isFirstLoaded = false
            } else {
                textFieldUrl?.apply {
                    textAreaInput.removeTextChangedListener(textWatcher)
                    replaceTextAndRestoreCursorPosition(element.inputUrl)
                    textAreaInput.addTextChangedListener(textWatcher)
                    requestFocus()
                }
            }

            loadLayout(
                inputUrl = element.inputUrl,
                imageUrl = element.inputImage,
                inputTitle = element.inputTitle,
                inputDescription = element.inputDescription,
                errorMessage = element.errorMessage
            )

            textFieldUrl?.textAreaIconClose?.setOnClickListener {
                textFieldUrl?.clearFocus()
                listener?.onDeleteClicked(element, adapterPosition)
            }
        }

        private fun loadLayout(
            inputUrl: String,
            imageUrl: String,
            inputTitle: String,
            inputDescription: String,
            errorMessage: String
        ) {
            itemView.apply {
                cardThumbnail?.visibility = if (inputTitle.isEmpty()) View.GONE else View.VISIBLE
                textFieldUrl?.textAreaIconClose?.visibility = if (inputUrl.isEmpty()) View.GONE else View.VISIBLE

                try {
                    if (imgThumbnail?.context?.isValidGlideContext() == true) {
                        imgThumbnail?.setImageUrl(imageUrl)
                    }
                } catch (e: Throwable) {
                    Timber.e(e)
                }

                tvVideoTitle?.text = inputTitle
                tvVideoSubtitle?.text = inputDescription

                if (errorMessage.isNotEmpty() && !textFieldUrl?.textAreaInput?.text.isNullOrBlank()) {
                    textFieldUrl?.isError = true
                    textFieldUrl?.textAreaMessage = errorMessage
                } else {
                    textFieldUrl?.isError = false
                    textFieldUrl?.textAreaMessage = ""
                }

                cardThumbnail?.setOnClickListener { listener?.onThumbnailClicked(inputUrl) }
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
