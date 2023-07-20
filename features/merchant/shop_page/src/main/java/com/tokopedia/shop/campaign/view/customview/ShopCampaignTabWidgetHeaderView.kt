package com.tokopedia.shop.campaign.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ShopCampaignWidgetHeaderViewBinding
import com.tokopedia.unifyprinciples.Typography

class ShopCampaignTabWidgetHeaderView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {
    companion object {
        private val TITLE_BACKGROUND_DARK = R.drawable.bg_campaign_widget_header_title_dark_mode
        private val TITLE_COLOR_DARK = com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        private val TITLE_BACKGROUND_LIGHT = R.drawable.bg_campaign_widget_header_title_light_mode
        private val TITLE_COLOR_LIGHT = R.color.dms_static_Unify_NN950_light

        private val CTA_BACKGROUND_DARK = R.drawable.bg_campaign_widget_header_cta_dark_mode
        private val CTA_COLOR_DARK = com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        private val CTA_BACKGROUND_LIGHT = R.drawable.bg_campaign_widget_header_cta_light_mode
        private val CTA_COLOR_LIGHT = R.color.dms_color_525A67

        private val TEXT_TITLE_ID = R.id.text_title
        private val CTA_VIEW_ID = R.id.cta_view
        private val CTA_TEXT_VIEW_ID = R.id.cta_text_view

    }

    private var binding: ShopCampaignWidgetHeaderViewBinding? = null
    private var textTitleId = TEXT_TITLE_ID
    private var ctaViewId = CTA_VIEW_ID
    private var ctaTextViewId = CTA_TEXT_VIEW_ID

    private val textTitle: Typography?
        get() = binding?.root?.findViewById(textTitleId)
    private val ctaView: IconUnify?
        get() = binding?.root?.findViewById(ctaViewId)

    private val ctaTextView: Typography?
        get() = binding?.root?.findViewById(ctaTextViewId)

    init {
        binding = ShopCampaignWidgetHeaderViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setTitle(title: String) {
        binding?.textTitle?.apply {
            if (title.isNotEmpty()) {
                show()
                text = title
            } else {
                hide()
            }
        }
    }

    fun setCta(ctaText: String, onCtaClicked: () -> Unit) {
        if (ctaText.isNotEmpty()) {
            ctaView?.show()
            ctaView?.setOnClickListener {
                onCtaClicked.invoke()
            }
        } else {
            ctaView?.hide()
        }
    }

    fun configColorMode(colorMode: Boolean) {
        configTextTitleColorMode(colorMode)
        configCtaColorMode(colorMode)
    }

    private fun configCtaColorMode(colorMode: Boolean) {
        ctaView?.apply {
            if (isDarkMode(colorMode)) {
                background = MethodChecker.getDrawable(context, CTA_BACKGROUND_DARK)
                setColorFilter(MethodChecker.getColor(context, CTA_COLOR_DARK))
            } else {
                background = MethodChecker.getDrawable(context, CTA_BACKGROUND_LIGHT)
                setColorFilter(MethodChecker.getColor(context, CTA_COLOR_LIGHT))
            }
        }
    }

    private fun configTextTitleColorMode(colorMode: Boolean) {
        textTitle?.apply {
            if (isDarkMode(colorMode)) {
                background = MethodChecker.getDrawable(context, TITLE_BACKGROUND_DARK)
                setTextColor(MethodChecker.getColor(context, TITLE_COLOR_DARK))
            } else {
                background = MethodChecker.getDrawable(context, TITLE_BACKGROUND_LIGHT)
                setTextColor(MethodChecker.getColor(context, TITLE_COLOR_LIGHT))
            }
        }
    }

    private fun isDarkMode(colorMode: Boolean): Boolean {
        return colorMode
    }

    fun setTitleId(id: Int) {
        textTitle?.id = id
        textTitleId = id
    }

    fun setCtaTextId(id: Int) {
        ctaTextView?.id = id
        ctaTextViewId = id
    }

    fun showTitle() {
        textTitle?.show()
    }

}
