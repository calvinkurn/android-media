package com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.databinding.ItemWidgetBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.WidgetItem
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetDependencyProvider
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetEventListener

class WidgetViewHolder(
    itemView: View,
    private val provider: BmsmWidgetDependencyProvider,
    private val listener: BmsmWidgetEventListener
) : AbstractViewHolder<WidgetItem>(itemView) {

    private val binding: ItemWidgetBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_widget
    }

    override fun bind(element: WidgetItem) {
        binding?.apply {
            widgetBmsm.apply {
                setupWidget(provider, element.item, false, ShopPageColorSchema())
                setOnSuccessAtcListener { product ->
                    listener.onBmsmWidgetSuccessAtc(product)
                }
                setOnNavigateToOlpListener { applink ->
                    listener.onBmsmWidgetNavigateToOlp(applink)
                }
                setOnProductCardClicked { product ->
                    listener.onBmsmWidgetProductClicked(product)
                }
            }
        }
    }
}
