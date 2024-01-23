package com.tokopedia.shop.home.view.adapter.viewholder.bmsm

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.shop.home.view.model.ShopBmsmWidgetGwpUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeBmsmWidgetGwpBinding
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetDependencyProvider
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetEventListener
import com.tokopedia.utils.view.binding.viewBinding

class ShopBmsmWidgetGwpViewHolder(
    itemView: View,
    private val provider: BmsmWidgetDependencyProvider,
    private val listener: BmsmWidgetEventListener,
    private val patternColorType: String
): AbstractViewHolder<ShopBmsmWidgetGwpUiModel>(itemView)  {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_bmsm_widget_gwp
    }

    private val binding: ItemShopHomeBmsmWidgetGwpBinding? by viewBinding()
    private var isBmsmWidgetAlreadyLoaded = false

    override fun bind(element: ShopBmsmWidgetGwpUiModel) {
        if(!isBmsmWidgetAlreadyLoaded) {
            binding?.apply {
                tpgTitle.apply {
                    text = element.header.title
                    val textColor = if (element.header.isOverrideTheme && element.header.colorSchema.listColorSchema.isNotEmpty()) {
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
                        element.header.isOverrideTheme,
                        element.header.colorSchema,
                        patternColorType
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
                    setOnWidgetVisible {
                        isBmsmWidgetAlreadyLoaded = true
                    }
                }
            }
        }
    }
}
