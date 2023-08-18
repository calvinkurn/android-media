package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemSliderImageTextBinding
import com.tokopedia.catalogcommon.util.decoration.MarginItemDecoration
import com.tokopedia.catalogcommon.util.decoration.SpaceItemDecoration
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.utils.view.binding.viewBinding

class SliderImageTextViewHolder(itemView: View) :
    AbstractViewHolder<SliderImageTextUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_slider_image_text
    }

    private val binding by viewBinding<WidgetItemSliderImageTextBinding>()

    private val displayMetrics = itemView.resources.displayMetrics

    override fun bind(element: SliderImageTextUiModel) {
        binding?.root?.setBackgroundColor(element.widgetBackgroundColor.orDefaultColor(itemView.context))
        binding?.rvItems?.apply {
            adapter = ItemSliderImageTextAdapter(element.items)
            layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemDecoration(16.dpToPx(displayMetrics), 16.dpToPx(displayMetrics)))
            addItemDecoration(SpaceItemDecoration(16.dpToPx(displayMetrics)))
        }
    }
}
