package com.tokopedia.home_component.widget.card

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.WidgetSmallProductCardBinding
import com.tokopedia.home_component.util.loadImageRounded
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil

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
        setupProductBannerImage(model.bannerImageUrl)
        binding.txtTitle.shouldTypographyStyleApplied(model.title)
        binding.txtSubtitle.shouldTypographyStyleApplied(model.subtitle)
    }

    private fun setupProductBannerImage(url: String) {
        val radius = context.resources.getDimensionPixelSize(
            R.dimen.home_mission_widget_clear_image_corner_radius
        )

        binding.imgBanner.loadImageRounded(url, radius)
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
