package com.tokopedia.shop.home.view.adapter.viewholder.bmsm

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeBmsmWidgetPdBinding
import com.tokopedia.shop.home.view.model.ShopBmsmWidgetGwpUiModel
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetDependencyProvider
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetEventListener
import com.tokopedia.utils.view.binding.viewBinding

class ShopBmsmWidgetPdViewHolder(
    itemView: View,
    private val provider: BmsmWidgetDependencyProvider,
    private val listener: BmsmWidgetEventListener,
    private val isOverrideTheme: Boolean
): AbstractViewHolder<ShopBmsmWidgetGwpUiModel>(itemView)  {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_bmsm_widget_pd
    }

    private val binding: ItemShopHomeBmsmWidgetPdBinding? by viewBinding()

    override fun bind(element: ShopBmsmWidgetGwpUiModel) {
        binding?.apply {
            tpgTitle.apply {
                text = element.header.title
                val textColor = if (isOverrideTheme && element.header.colorSchema.listColorSchema.isNotEmpty()){
                    element.header.colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
                } else {
                    ContextCompat.getColor(context, R.color.dms_static_black)
                }
                setTextColor(textColor)
            }
            tpgSubTitle.apply {
                showWithCondition(element.data.size == Int.ONE)
                text = element.data.firstOrNull()?.offerName
            }
            widgetBmsm.apply {
                setupWidget(
                    provider,
                    element.data,
                    isOverrideTheme,
                    element.header.colorSchema
                )
                setOnSuccessAtcListener { product ->
                    listener.onBmsmWidgetSuccessAtc(product)
                }
                setOnErrorAtcListener { errorMsg ->
                    listener.onBmsmWidgetErrorAtc(errorMsg)
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
