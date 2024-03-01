package com.tokopedia.shareexperience.ui.adapter.viewholder.image

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceImageCarouselItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExImageGeneratorListener
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExImageCarouselViewHolder(
    itemView: View,
    imageGeneratorListener: ShareExImageGeneratorListener
) : AbstractViewHolder<ShareExImageCarouselUiModel>(itemView) {

    private val binding: ShareexperienceImageCarouselItemBinding? by viewBinding()

    init {
        binding?.shareexRvImageCarousel?.setImageGeneratorListener(imageGeneratorListener)
    }

    override fun bind(element: ShareExImageCarouselUiModel) {
        binding?.shareexRvImageCarousel?.initData(element.imageUrlList)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_image_carousel_item
    }
}
