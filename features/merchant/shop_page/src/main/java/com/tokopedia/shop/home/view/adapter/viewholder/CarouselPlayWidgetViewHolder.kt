package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomePlayWidgetBinding
import com.tokopedia.shop.databinding.ViewPlayWidgetCustomHeaderShopHomeTabBinding
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.listener.ShopHomePlayWidgetListener
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by mzennis on 13/10/20.
 */
class CarouselPlayWidgetViewHolder(
    private val playWidgetViewHolder: PlayWidgetViewHolder,
    private val shopHomePlayWidgetListener: ShopHomePlayWidgetListener,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(playWidgetViewHolder.itemView) {

    private val viewBinding: ItemShopHomePlayWidgetBinding? by viewBinding()
    private var playWidgetView: PlayWidgetView? = viewBinding?.playWidgetView
    private var headerTitle: Typography? = null
    private var headerCta: Typography? = null


    override fun bind(element: CarouselPlayWidgetUiModel) {
        playWidgetViewHolder.bind(element.playWidgetState, this)
        setupHeaderSection(element)
        setWidgetImpressionListener(element)
    }

    private fun setupHeaderSection(element: CarouselPlayWidgetUiModel) {
        playWidgetView?.let {
            val customHeaderBinding = ViewPlayWidgetCustomHeaderShopHomeTabBinding.inflate(
                LayoutInflater.from(itemView.context),
                it,
                false
            )
            headerTitle = customHeaderBinding.tvPlayWidgetTitle
            headerCta = customHeaderBinding.tvPlayWidgetAction
            it.setCustomHeader(customHeaderBinding.root)
            playWidgetViewHolder.coordinator.controlWidget(it)
            if (shopHomeListener.isShopHomeTabHasFestivity()) {
                setDefaultHeaderSectionConfig()
            } else {
                if (element.header.isOverrideTheme) {
                    setReimaginedHeaderSectionConfig(element.header.colorSchema)
                } else {
                    setDefaultHeaderSectionConfig()
                }
            }
        }
    }

    private fun setReimaginedHeaderSectionConfig(colorSchema: ShopPageColorSchema) {
        val highEmphasisColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
        )
        val ctaLinkColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.CTA_TEXT_LINK_COLOR
        )
        headerTitle?.setTextColor(highEmphasisColor)
        headerCta?.setTextColor(ctaLinkColor)
    }

    private fun setDefaultHeaderSectionConfig() {
        headerTitle?.setTextColor(MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
        ))
        headerCta?.setTextColor(MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        ))
    }

    private fun setWidgetImpressionListener(model: CarouselPlayWidgetUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            shopHomePlayWidgetListener.onPlayWidgetImpression(model, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_shop_home_play_widget
    }
}
