package com.tokopedia.product.addedit.description.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.description.presentation.model.youtube.YoutubeVideoModel
import kotlinx.android.synthetic.main.item_product_add_video.view.*

class VideoLinkTypeFactory: BaseAdapterTypeFactory(){
    private var listener: VideoLinkListener? = null

    fun setVideoLinkListener(listener: VideoLinkListener){
        this.listener = listener
    }

    fun type(videoLinkModel: VideoLinkModel): Int = VideoLinkViewHolder.LAYOUT
    fun type(youtubeVideoModel: YoutubeVideoModel): Int = VideoLinkViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            VideoLinkViewHolder.LAYOUT -> VideoLinkViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    class VideoLinkViewHolder(val view: View?, private val listener: VideoLinkListener?)
        : AbstractViewHolder<Visitable<*>>(view) {
        override fun bind(element: Visitable<*>) {
            when(element) {
                is VideoLinkModel -> {
                    itemView.textFieldUrl.textFieldInput.setText(element.inputUrl)
                    itemView.cardThumbnail.visibility =
                            if (element.inputUrl.isEmpty()) View.GONE else View.VISIBLE
                    itemView.textFieldUrl.textFieldInput.afterTextChanged {
                        if (adapterPosition >= 0) {
                            val inputUrl = itemView.textFieldUrl.textFieldInput.text.toString()
                            listener?.onTextChanged(inputUrl, adapterPosition)
                        }
                    }
                    itemView.textFieldUrl.getSecondIcon().setOnClickListener {
                        itemView.textFieldUrl.clearFocus()
                        listener?.onDeleteClicked(element, adapterPosition)
                    }
                }
                is YoutubeVideoModel -> {
                    loadLayout(element.thumbnailUrl.toString(), element.title.toString(), element.description.toString())
                }
            }
        }

        private fun loadLayout (imageUrl: String, title: String, description: String) {
            itemView.imgThumbnail.urlSrc = imageUrl
            itemView.tvVideoTitle.text = title
            itemView.tvVideoSubtitle.text = description
        }

        companion object {
            val LAYOUT = R.layout.item_product_add_video
        }
    }

    interface VideoLinkListener {
        fun onDeleteClicked(videoLinkModel: VideoLinkModel, position: Int)
        fun onTextChanged(url: String, position: Int)
    }
}