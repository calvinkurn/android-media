package com.tokopedia.home_component.customview.header

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.getLink
import com.tokopedia.home_component_header.R as home_component_headerR
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HeaderChevronLayoutStrategy : HeaderLayoutStrategy {
    companion object {
        private const val ROTATE_TO_DEGREES = 360f
        private const val PIVOT_X_VALUE = 0.5f
        private const val PIVOT_Y_VALUE = 0.5f
        private const val ROTATE_FROM_DEGREES = 0f
        private const val ROTATE_DURATION = 500L
    }

    var ctaButtonRevamp: IconUnify? = null
    var ctaBorder: ImageView? = null
    var ctaButtonRevampContainer: ConstraintLayout? = null

    private val rotateAnimation by lazy {
        RotateAnimation(ROTATE_FROM_DEGREES, ROTATE_TO_DEGREES, Animation.RELATIVE_TO_SELF, PIVOT_X_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_Y_VALUE)
            .apply {
                duration = ROTATE_DURATION
                interpolator = LinearInterpolator()
            }
    }

    override fun getLayout(): Int = home_component_headerR.layout.home_component_header_chevron_layout

    override fun renderCta(
        itemView: View,
        channel: ChannelModel,
        listener: HeaderListener?,
        ctaMode: Int?,
        colorMode: Int?
    ) {
        val header = channel.channelHeader
        ctaButtonRevamp = itemView.findViewById(home_component_headerR.id.cta_chevron_icon)
        ctaButtonRevampContainer = itemView.findViewById(home_component_headerR.id.cta_chevron_container)
        ctaBorder = itemView.findViewById(home_component_headerR.id.cta_chevron_border)

        if (header.hasSeeMoreApplink() || ctaMode != DynamicChannelHeaderView.CTA_MODE_SEE_ALL) {
            setCtaIcon(
                itemView.context,
                ctaBorder,
                ctaButtonRevamp,
                ctaMode,
                colorMode
            )

            ctaButtonRevampContainer?.show()
            ctaButtonRevamp?.show()
            ctaBorder?.show()

            ctaButtonRevamp?.setOnClickListener {
                when (ctaMode) {
                    DynamicChannelHeaderView.CTA_MODE_SEE_ALL -> listener?.onSeeAllClick(channel.channelHeader.getLink())
                    DynamicChannelHeaderView.CTA_MODE_RELOAD -> {
                        it.startAnimation(rotateAnimation)
                        listener?.onReloadClick(channel)
                    }
                    DynamicChannelHeaderView.CTA_MODE_CLOSE -> listener?.onDismissClick(channel)
                }
            }
        } else {
            ctaButtonRevampContainer?.hide()
            ctaButtonRevamp?.hide()
            ctaBorder?.hide()
        }
    }

    private fun setCtaIcon(
        context: Context,
        ctaBorder: ImageView?,
        ctaButtonRevamp: IconUnify?,
        ctaMode: Int?,
        colorMode: Int?
    ) {
        val iconId = when (ctaMode) {
            DynamicChannelHeaderView.CTA_MODE_SEE_ALL -> IconUnify.CHEVRON_RIGHT
            DynamicChannelHeaderView.CTA_MODE_RELOAD -> IconUnify.RELOAD
            DynamicChannelHeaderView.CTA_MODE_CLOSE -> IconUnify.CLOSE
            else -> IconUnify.CHEVRON_RIGHT
        }
        when (colorMode) {
            DynamicChannelHeaderView.COLOR_MODE_NORMAL -> {
                ctaBorder?.loadImage(ContextCompat.getDrawable(context, home_component_headerR.drawable.home_component_header_bg_header_cta))
                ctaButtonRevamp?.setImage(
                    newIconId = iconId,
                    newLightEnable = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN900),
                    newDarkEnable = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950)
                )
            }
            DynamicChannelHeaderView.COLOR_MODE_INVERTED -> {
                ctaBorder?.loadImage(ContextCompat.getDrawable(context, home_component_headerR.drawable.home_component_header_bg_channel_header_cta_inverted))
                ctaButtonRevamp?.setImage(
                    newIconId = iconId,
                    newLightEnable = ContextCompat.getColor(context, home_component_headerR.color.dms_header_cta_inverted_icon),
                    newDarkEnable = ContextCompat.getColor(context, home_component_headerR.color.dms_header_cta_inverted_icon)
                )
            }
        }
    }

    override fun renderTitle(
        context: Context,
        channelHeader: ChannelHeader,
        txtTitle: Typography?,
        headerColorMode: Int?
    ) {
        txtTitle?.setTextColor(
            if (headerColorMode == DynamicChannelHeaderView.COLOR_MODE_INVERTED) {
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White)
            } else if (channelHeader.textColor.isNotEmpty()) {
                Color.parseColor(channelHeader.textColor).staticIfDarkMode(context)
            } else {
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950)
            }
        )
    }

    override fun renderSubtitle(
        context: Context,
        channelHeader: ChannelHeader,
        txtSubtitle: Typography?,
        headerColorMode: Int?
    ) {
        txtSubtitle?.setTextColor(
            if (headerColorMode == DynamicChannelHeaderView.COLOR_MODE_INVERTED) {
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White)
            } else if (channelHeader.textColor.isNotEmpty()) {
                Color.parseColor(channelHeader.textColor).staticIfDarkMode(context)
            } else {
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN600)
            }
        )
    }

    override fun renderIconSubtitle(
        itemView: View,
        channelHeader: ChannelHeader
    ) {
        val subtitleIcon: ImageView = itemView.findViewById(home_component_headerR.id.header_icon_subtitle)
        subtitleIcon.gone()
    }

    override fun setConstraints(headerContainer: ConstraintLayout?, channelHeader: ChannelHeader) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(headerContainer)
        if(channelHeader.hasTitleOnly()) {
            constraintSet.connect(home_component_headerR.id.header_title, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        } else {
            constraintSet.clear(home_component_headerR.id.header_title, ConstraintSet.BOTTOM)
        }
        constraintSet.applyTo(headerContainer)
    }
}
