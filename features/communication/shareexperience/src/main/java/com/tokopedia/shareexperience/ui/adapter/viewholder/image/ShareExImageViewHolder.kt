package com.tokopedia.shareexperience.ui.adapter.viewholder.image

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shareexperience.databinding.ShareexperienceImageItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExCarouselImageListener
import com.tokopedia.shareexperience.ui.model.image.ShareExImageUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExImageViewHolder(
    itemView: View,
    listener: ShareExCarouselImageListener
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ShareexperienceImageItemBinding? by viewBinding()
    private var element: ShareExImageUiModel? = null

    init {
        binding?.shareexIvImageItem?.setOnClickListener {
            listener.onClickImage(bindingAdapterPosition, element?.imageUrl ?: "")
        }
    }

    fun bind(element: ShareExImageUiModel) {
        this.element = element
        binding?.shareexIvImageItem?.loadImage(element.imageUrl)
        binding?.shareexIconCheckmark?.showWithCondition(element.isSelected)
        binding?.shareexBorderSelectedImage?.showWithCondition(element.isSelected)
    }
}
