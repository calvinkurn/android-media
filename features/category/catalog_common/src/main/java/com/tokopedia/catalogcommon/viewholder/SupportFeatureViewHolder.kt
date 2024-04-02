package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.ItemSupportFeatureAdapter
import com.tokopedia.catalogcommon.databinding.WidgetItemSupportFeatureBinding
import com.tokopedia.catalogcommon.uimodel.SupportFeaturesUiModel
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.utils.view.binding.viewBinding


class SupportFeatureViewHolder(itemView: View) :
    AbstractViewHolder<SupportFeaturesUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_support_feature
    }

    private val binding by viewBinding<WidgetItemSupportFeatureBinding>()

    override fun bind(element: SupportFeaturesUiModel) {
        binding?.tvTitle?.text = element.titleSection
        binding?.tvTitle?.setTextColor(element.widgetTextColor.orDefaultColor(itemView.context))
        binding?.rvItems?.apply {
            adapter = ItemSupportFeatureAdapter(element.items)
            layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

}
