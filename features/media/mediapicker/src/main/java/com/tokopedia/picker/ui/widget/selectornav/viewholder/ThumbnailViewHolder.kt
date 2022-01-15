package com.tokopedia.picker.ui.widget.selectornav.viewholder

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.databinding.ViewItemSelectionThumbnailBinding
import com.tokopedia.picker.utils.dimensionOf
import com.tokopedia.picker.utils.dimensionPixelOffsetOf
import com.tokopedia.picker.utils.getVideoDurationLabel
import com.tokopedia.picker.utils.isVideoFormat
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File

class ThumbnailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionThumbnailBinding? by viewBinding()
    private val context by lazy { itemView.context }

    fun bind(media: Media, deletedOnClick: () -> Unit = {}) {
        val thumbnailSize = context.dimensionPixelOffsetOf(com.tokopedia.abstraction.R.dimen.dp_72)
        val roundedSize = context.dimensionOf(com.tokopedia.abstraction.R.dimen.dp_6)

        var loadFitCenter = false
        val file = File(media.path)

        if (file.exists()) {
            loadFitCenter = ImageProcessingUtil.shouldLoadFitCenter(file)
        }

        binding?.imageView?.loadImage(media.path) {
            overrideSize(Resize(thumbnailSize, thumbnailSize))
            setRoundedRadius(roundedSize)

            if (loadFitCenter) {
                fitCenter()
            } else {
                centerCrop()
            }
        }

        binding?.ivDelete?.setOnClickListener {
            deletedOnClick()
        }

        if (isVideoFormat(media.path)) {
            val durationVideo = getVideoDurationLabel(context, Uri.parse(media.path))
            binding?.tvDuration?.text = durationVideo
            binding?.tvDuration?.show()
        } else {
            binding?.tvDuration?.hide()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_item_selection_thumbnail

        fun create(viewGroup: ViewGroup): ThumbnailViewHolder {
            val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(LAYOUT, viewGroup, false)

            return ThumbnailViewHolder(view)
        }
    }

}