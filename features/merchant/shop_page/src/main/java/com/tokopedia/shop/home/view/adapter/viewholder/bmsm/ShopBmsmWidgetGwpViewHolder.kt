package com.tokopedia.shop.home.view.adapter.viewholder.bmsm

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.shop.home.view.model.ShopBmsmWidgetGwpUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeBmsmWidgetGwpBinding
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetDependencyProvider
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetEventListener
import com.tokopedia.shop_widget.buy_more_save_more.util.BmsmWidgetColorThemeConfig
import com.tokopedia.shop_widget.buy_more_save_more.util.ColorType
import com.tokopedia.unifycomponents.R.*
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
                    setTextColor(getTextColor(element))
                }
                tpgSubTitle.apply {
                    showWithCondition(element.data.size == Int.ONE)
                    text = element.data.firstOrNull()?.offerName
                    setTextColor(getTextColor(element))
                }
                widgetBmsm.apply {
                    setupWidget(
                        provider = provider,
                        offerList = element.data,
                        colorSchema = element.header.colorSchema,
                        colorThemeConfiguration = getColorThemeConfiguration(element),
                        patternColorType = ColorType.values().firstOrNull { value ->
                            value.type == patternColorType
                        } ?: ColorType.LIGHT
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

    private fun getColorThemeConfiguration(element: ShopBmsmWidgetGwpUiModel): BmsmWidgetColorThemeConfig {
        val colorThemeConfiguration = if (element.isFestivity) {
            BmsmWidgetColorThemeConfig.FESTIVITY
        } else {
            if (element.header.isOverrideTheme) {
                BmsmWidgetColorThemeConfig.REIMAGINE
            } else {
                BmsmWidgetColorThemeConfig.DEFAULT
            }
        }
        return colorThemeConfiguration
    }

    private fun getTextColor(element: ShopBmsmWidgetGwpUiModel): Int {
        val textColor =  when (getColorThemeConfiguration(element)) {
            BmsmWidgetColorThemeConfig.FESTIVITY -> ContextCompat.getColor(itemView.context, R.color.dms_static_white)
            BmsmWidgetColorThemeConfig.REIMAGINE -> {
                if (element.header.colorSchema.listColorSchema.isNotEmpty()) {
                    element.header.colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
                } else {
                    ContextCompat.getColor(itemView.context, color.Unify_NN950)
                }
            }
            BmsmWidgetColorThemeConfig.DEFAULT -> ContextCompat.getColor(itemView.context, R.color.dms_static_black)
        }

        return textColor
    }
}
