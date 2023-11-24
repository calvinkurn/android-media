package com.tokopedia.catalogcommon.viewholder

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.ImageSlidePagerAdapter
import com.tokopedia.catalogcommon.databinding.WidgetItemSliderImageTextBinding
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.utils.view.binding.viewBinding

class SliderImageTextViewHolder(itemView: View) :
    AbstractViewHolder<SliderImageTextUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_slider_image_text
    }

    private val binding by viewBinding<WidgetItemSliderImageTextBinding>()

    override fun bind(element: SliderImageTextUiModel) {
        val imageSlideAdapter = ImageSlidePagerAdapter(element.items)
        binding?.viewPager?.adapter = imageSlideAdapter
        binding?.viewPager?.currentItem = Int.ZERO
    }
}
