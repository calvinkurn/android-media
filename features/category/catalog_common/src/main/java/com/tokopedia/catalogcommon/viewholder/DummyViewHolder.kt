package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerHeroBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemDummyBinding
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.utils.view.binding.viewBinding

class DummyViewHolder(itemView: View) : AbstractViewHolder<DummyUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_dummy
    }

    private val binding by viewBinding<WidgetItemDummyBinding>()


    override fun bind(element: DummyUiModel) {
        binding?.content?.text = element.content
        binding?.content?.setTextColor(element.widgetTextColor ?: return)
        binding?.content?.setBackgroundColor(element.widgetBackgroundColor ?: return)
    }
}
