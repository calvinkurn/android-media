package com.tokopedia.home_component.widget.card

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.WidgetSmallProductCardBinding
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil
import com.tokopedia.productcard.R as productcardR

class SmallProductCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = WidgetSmallProductCardBinding
        .inflate(LayoutInflater.from(context))

    private var stockBar: ProductStockBar?

    init {
        addView(binding.root)
        stockBar = ProductStockBar(this)
    }

    fun setData(model: SmallProductModel) {
        // mandatory
        useCompatPadding(model.ribbon())

        setupRibbon(model.ribbon())
        renderStockBar(model.stockBar)
        renderCardContainer(model.ribbon())
        setupProductBannerImage(model.bannerImageUrl)

        shouldHandleTitleStyle(model)
        shouldHandleSubtitleStyle(model)
    }

    private fun shouldHandleTitleStyle(model: SmallProductModel) {
        val (text, style) = model.title()
        basicTextStyle(binding.txtTitle, text, style)

        if (style.url.isEmpty()) {
            hideCampaignIcon()
        }

        binding.icCampaign.shouldShowWithAction(style.url.isNotEmpty()) {
            binding.icCampaign.loadImage(style.url)
            binding.txtTitle.setCustomMargin(MarginArea.Start(2.toPx()))
        }
    }

    private fun shouldHandleSubtitleStyle(model: SmallProductModel) {
        val (text, style) = model.subtitle()
        basicTextStyle(binding.txtSubtitle, text, style)
    }

    private fun renderStockBar(data: SmallProductModel.StockBar) {
        val value = if (data.isEnabled && data.percentageOnFireRange()) 4.toPx() else 2.toPx()
        binding.txtTitle.setCustomMargin(MarginArea.Top(value))

        stockBar?.shouldShowStockBar(data.isEnabled, data)
    }

    private fun setupProductBannerImage(url: String) {
        if (url.isEmpty()) return

        val radius = context.resources.getDimensionPixelSize(
            R.dimen.home_mission_widget_clear_image_corner_radius
        ).toFloat()

        binding.imgBanner.loadImage(url) {
            setRoundedRadius(radius)
            centerCrop()
        }
    }

    private fun useCompatPadding(ribbon: SmallProductModel.Ribbon?) {
        val padding = context.resources?.getDimensionPixelSize(
            productcardR.dimen.product_card_reimagine_use_compat_padding_size
        ) ?: 0

        val startPadding = if (ribbon != null) 0 else padding
        binding.root.setPadding(startPadding, padding, padding, padding)
    }

    private fun renderCardContainer(ribbon: SmallProductModel.Ribbon?) {
        val value = if (ribbon != null) 4.toPx() else 0
        binding.cardContainer.setCustomMargin(MarginArea.Start(value))
    }

    private fun setupRibbon(ribbon: SmallProductModel.Ribbon?) {
        if (ribbon == null) {
            binding.ribbon.gone()
            return
        }

        val ribbonMargin = RibbonView.Margin(start = 0, top = 4.toPx())

        binding.ribbon.render(ribbon)
        binding.ribbon.setMargin(
            left = ribbonMargin.start,
            top = ribbonMargin.top,
            right = 0,
            bottom = 0
        )
    }

    private fun basicTextStyle(typography: Typography?, text: String, style: SmallProductModel.TextStyle) {
        typography?.text = if (style.shouldRenderHtmlFormat.not()) text else HtmlUtil.fromHtml(text)
        if (style.textColor.isNotEmpty()) typography?.setTextColor(Color.parseColor(style.textColor))

        val weight = if (style.isBold) Typography.BOLD else Typography.REGULAR
        typography?.setWeight(weight)

        if (style.isTextStrikethrough) {
            typography?.strikethrough()
            typography?.requestLayout()
        }
    }

    private fun hideCampaignIcon() {
        binding.icCampaign.hide()
        binding.txtTitle.setCustomMargin(MarginArea.Start(0))
    }
}
