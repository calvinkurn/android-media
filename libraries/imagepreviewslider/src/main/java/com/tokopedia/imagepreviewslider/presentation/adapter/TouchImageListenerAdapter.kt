package com.tokopedia.imagepreviewslider.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.design.image.TouchImageView
import com.tokopedia.design.list.adapter.TouchImageAdapter
import java.util.*

class TouchImageListenerAdapter(context: Context, FileLoc: ArrayList<String>) : TouchImageAdapter(context, FileLoc) {
    private var imageClickListener: ImageClickListener? = null

    interface ImageClickListener {
        fun onImageClicked(position: Int)
    }

    fun setOnImageClickListener(Listener: ImageClickListener) {
        imageClickListener = Listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = super.instantiateItem(container, position) as TouchImageView
        if (imageClickListener != null) {
            imageView.setOnClickListener { imageClickListener?.onImageClicked(position) }
        }
        return imageView
    }
}
