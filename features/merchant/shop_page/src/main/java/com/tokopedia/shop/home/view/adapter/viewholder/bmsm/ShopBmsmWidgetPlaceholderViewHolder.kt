package com.tokopedia.shop.home.view.adapter.viewholder.bmsm

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemBmsmWidgetPlaceholderBinding
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.view.model.ShopBmsmWidgetGwpUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopBmsmWidgetPlaceholderViewHolder(
    itemView: View
): AbstractViewHolder<ShopBmsmWidgetGwpUiModel>(itemView)   {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_bmsm_widget_placeholder
        private const val PD_WIDGET_TITLE_LOADER_LEFT_MARGIN = 64
    }

    private val binding: ItemBmsmWidgetPlaceholderBinding? by viewBinding()

    override fun bind(element: ShopBmsmWidgetGwpUiModel) {
        binding?.apply {
            val widgetGwpName = WidgetNameEnum.BMSM_GWP_OFFERING_GROUP.value
            val actualWidgetName = element.name
            val isGwpWidget = actualWidgetName == widgetGwpName
            clGiftImageLoaderWrapper.showWithCondition(isGwpWidget)

            if (!isGwpWidget) {
                val params = loaderTitle.layoutParams as ViewGroup.MarginLayoutParams
                params.marginStart = PD_WIDGET_TITLE_LOADER_LEFT_MARGIN
                loaderTitle.layoutParams = params
            }
        }
    }
}
