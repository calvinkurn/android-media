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
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.ribbon.RibbonView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil
import com.tokopedia.productcard.R as productcardR

class SmallProductCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = WidgetSmallProductCardBinding.inflate(
        LayoutInflater.from(context)
    )

    init {
        addView(binding.root)
    }

    fun setData(model: SmallProductModel) {
        setupRibbon(model.ribbon)
        useCompatPadding(model.ribbon)
        renderCardContainer(model.ribbon)
        setupProductBannerImage(model.bannerImageUrl)

        binding.txtTitle.shouldTypographyStyleApplied(model.title)
        binding.txtSubtitle.shouldTypographyStyleApplied(model.subtitle)
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
        val marginStart = if (ribbon != null) 4.toPx() else 0

        binding.cardContainer.layoutParams = binding.cardContainer.layoutParams.apply {
            val marginLayoutParams = this as? MarginLayoutParams
            marginLayoutParams?.marginStart = marginStart
        }
    }

    private fun setupRibbon(ribbon: SmallProductModel.Ribbon?) {
        if (ribbon == null) {
            binding.ribbon.gone()
            return
        }

        val ribbonMargin = RibbonView.Margin(start = 0, top = 4.toPx())

        // temporary
        val model = ProductCardModel.LabelGroup(
            title = ribbon.text,
            type = when(ribbon.type) {
                is SmallProductModel.Ribbon.Type.Red -> "red"
                is SmallProductModel.Ribbon.Type.Yellow -> "gold"
            }
        )

        binding.ribbon.render(model)

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

        if (style.textColor.isNotEmpty()) {
            this?.setTextColor(Color.parseColor(style.textColor))
        }
    }
}
