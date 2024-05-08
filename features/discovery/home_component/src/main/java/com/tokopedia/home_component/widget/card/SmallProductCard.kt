package com.tokopedia.home_component.widget.card

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.marginStart
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.WidgetSmallProductCardBinding
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.widget.card.ProductStockBar.Companion.MIN_THRESHOLD_FIRE_VISIBLE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
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

        binding.txtTitle.shouldTypographyStyleApplied(model.title())
        binding.txtSubtitle.shouldTypographyStyleApplied(model.subtitle())
    }

    private fun renderStockBar(data: SmallProductModel.StockBar) {
        stockBar?.shouldShowStockBar(data.isEnabled, data.percentage)

        val value = if (data.isEnabled && data.percentage > MIN_THRESHOLD_FIRE_VISIBLE) {
            4.toPx()
        } else {
            2.toPx()
        }

        binding.txtTitle.setCustomMargin(MarginArea.Top(value))
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

    private fun Typography?.shouldTypographyStyleApplied(
        content: Pair<String, SmallProductModel.TextStyle>
    ) {
        val (title, style) = content

        this?.text = if (style.shouldRenderHtmlFormat) HtmlUtil.fromHtml(title) else title
        this?.setWeight(if (style.isBold) Typography.BOLD else Typography.REGULAR)

        if (style.isTextStrikethrough) this?.strikethrough()
        if (style.textColor.isNotEmpty()) this?.setTextColor(Color.parseColor(style.textColor))

        shouldHandleCampaignIconVisibility(style)
    }

    private fun shouldHandleCampaignIconVisibility(style: SmallProductModel.TextStyle) {
        if (style.url.isNotEmpty()) {
            binding.icCampaign.show()
            binding.icCampaign.loadImage(style.url)
            binding.txtTitle.setCustomMargin(MarginArea.Start(2.toPx()))
        } else {
            hideCampaignIcon()
        }
    }

    private fun hideCampaignIcon() {
        binding.icCampaign.hide()
        binding.txtTitle.setCustomMargin(MarginArea.Start(0))
    }
}
