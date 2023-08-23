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
//        binding?.root?.setBackgroundColor(element.widgetBackgroundColor.orDefaultColor(itemView.context))
        binding?.viewPager?.adapter = imageSlideAdapter
        binding?.viewPager?.currentItem = Int.ZERO
        binding?.tvHighlight?.text = element.items[Int.ZERO].textHighlight
        binding?.tvTitle?.text = element.items[Int.ZERO].textTitle
        binding?.tvDescription?.text = element.items[Int.ZERO].textDescription

        binding?.tvHighlight?.setTextColor(element.items[Int.ZERO].textHighlightColor)
        binding?.tvTitle?.setTextColor(element.items[Int.ZERO].textTitleColor)
        binding?.tvDescription?.setTextColor(element.items[Int.ZERO].textDescriptionColor)

        val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.fade_in)

        binding?.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                binding?.tvHighlight?.startAnimation(animation)
                binding?.tvTitle?.startAnimation(animation)
                binding?.tvDescription?.startAnimation(animation)
                binding?.tvHighlight?.text = element.items[position].textHighlight
                binding?.tvTitle?.text = element.items[position].textTitle
                binding?.tvDescription?.text = element.items[position].textDescription

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state != SCROLL_STATE_IDLE){
                    imageSlideAdapter.resetAlphaView()
                }
            }

        })
    }
}
