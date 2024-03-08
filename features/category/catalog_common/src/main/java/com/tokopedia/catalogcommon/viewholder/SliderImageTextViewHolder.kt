package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.ImageSliderAdapter
import com.tokopedia.catalogcommon.databinding.WidgetItemSliderImageTextBinding
import com.tokopedia.catalogcommon.listener.SliderImageTextListener
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SliderImageTextViewHolder(itemView: View, val listener: SliderImageTextListener? = null) :
    AbstractViewHolder<SliderImageTextUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_slider_image_text
    }

    private val binding by viewBinding<WidgetItemSliderImageTextBinding>()

    override fun bind(element: SliderImageTextUiModel) {
        val imageSlideAdapter = ImageSliderAdapter(element.items)
        binding?.rvSlider?.adapter = imageSlideAdapter
        binding?.rvSlider?.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        listener?.onSliderImageTextImpression(element.widgetName)
    }
}
