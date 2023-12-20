package com.tokopedia.shareexperience.ui.adapter.viewholder.image

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceImageCarouselItemBinding
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExImageCarouselViewHolder(
    itemView: View
): AbstractViewHolder<ShareExImageCarouselUiModel>(itemView) {

    private val binding: ShareexperienceImageCarouselItemBinding? by viewBinding()

    override fun bind(element: ShareExImageCarouselUiModel) {
        binding?.shareexRvImageCarousel?.updateData(element.imageUrlList)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_image_carousel_item
    }
}
