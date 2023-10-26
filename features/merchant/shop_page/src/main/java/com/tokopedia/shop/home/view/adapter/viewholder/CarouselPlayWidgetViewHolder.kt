package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomePlayWidgetBinding
import com.tokopedia.shop.home.view.listener.ShopHomePlayWidgetListener
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by mzennis on 13/10/20.
 */
class CarouselPlayWidgetViewHolder(
    itemView: View,
    private val playWidgetViewHolder: PlayWidgetViewHolder,
    private val shopHomePlayWidgetListener: ShopHomePlayWidgetListener
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(itemView) {

    private val viewBinding: ItemShopHomePlayWidgetBinding? by viewBinding()
    private var headerContainer: ViewGroup? = viewBinding?.headerContainer
    private var textTitle: Typography? = viewBinding?.textTitle
    private var iconCtaChevron: IconUnify? = viewBinding?.iconCtaChevron

    override fun bind(element: CarouselPlayWidgetUiModel) {
        setContentSection(element)
        setupHeaderSection(element.playWidgetState.model)
        setWidgetImpressionListener(element)
        configColorTheme(element)
    }

    private fun setContentSection(element: CarouselPlayWidgetUiModel) {
        playWidgetViewHolder.bind(element.playWidgetState.copy(
            model = element.playWidgetState.model.copy(
                title = "",
                actionTitle = "",
                isActionVisible = false
            )
        ), this)
    }

    private fun configColorTheme(element: CarouselPlayWidgetUiModel) {
        if (element.header.isOverrideTheme) {
            setReimaginedColorConfig(element.header.colorSchema)
        } else {
            setDefaultColorConfig()
        }
    }

    private fun setupHeaderSection(model: PlayWidgetUiModel) {
        headerContainer?.shouldShowWithAction(isShowHeaderSection(model)){
            textTitle?.shouldShowWithAction(model.title.isNotEmpty()){
                textTitle?.text = model.title
            }
            iconCtaChevron?.shouldShowWithAction(hasAction(model)){
                iconCtaChevron?.setOnClickListener {
                    shopHomePlayWidgetListener.onPlayWidgetCtaClicked(model)
                }
            }
        }
    }

    private fun isShowHeaderSection(model: PlayWidgetUiModel): Boolean {
        return  model.title.isNotEmpty() || hasAction(model)
    }

    private fun hasAction(model: PlayWidgetUiModel): Boolean{
        return model.isActionVisible && model.actionAppLink.isNotEmpty()
    }

    private fun setReimaginedColorConfig(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
        )
        val ctaColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR
        )
        setHeaderColor(titleColor, ctaColor)
    }

    private fun setDefaultColorConfig() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            unifyprinciplesR.color.Unify_NN950_96
        )
        val ctaColor = MethodChecker.getColor(
            itemView.context,
            unifyprinciplesR.color.Unify_NN900
        )
        setHeaderColor(titleColor, ctaColor)
    }

    private fun setHeaderColor(titleColor: Int, ctaColor: Int) {
        textTitle?.setTextColor(titleColor)
        iconCtaChevron?.setColorFilter(ctaColor, PorterDuff.Mode.SRC_ATOP)
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
