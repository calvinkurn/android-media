package com.tokopedia.home_component_header.view

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
import com.tokopedia.home_component_header.R
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.util.getLink
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.home_component_header.R as home_component_headerR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class HeaderChevronLayoutStrategy : HeaderLayoutStrategy {
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
        channelHeader: ChannelHeader,
        listener: HomeComponentHeaderListener?,
        ctaMode: Int?,
        colorMode: Int?
    ) {
        if (channelHeader.hasSeeMoreApplink() || ctaMode != HomeComponentHeaderView.CTA_MODE_SEE_ALL) {
            ctaButtonRevamp = itemView.findViewById(home_component_headerR.id.cta_chevron_icon)
            ctaButtonRevampContainer = itemView.findViewById(home_component_headerR.id.cta_chevron_container)
            ctaBorder = itemView.findViewById(home_component_headerR.id.cta_chevron_border)

            setCtaIcon(
                itemView.context,
                ctaBorder,
                ctaButtonRevamp,
                ctaMode,
                colorMode
            )

            ctaButtonRevampContainer?.show()
            ctaButtonRevamp?.setOnClickListener {
                when (ctaMode) {
                    HomeComponentHeaderView.CTA_MODE_SEE_ALL -> listener?.onSeeAllClick(channelHeader.getLink())
                    HomeComponentHeaderView.CTA_MODE_RELOAD -> {
                        it.startAnimation(rotateAnimation)
                        listener?.onReloadClick(channelHeader.channelId)
                    }
                    HomeComponentHeaderView.CTA_MODE_CLOSE -> listener?.onDismissClick(channelHeader.channelId)
                }
            }
        } else {
            ctaButtonRevampContainer?.hide()
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
            HomeComponentHeaderView.CTA_MODE_SEE_ALL -> IconUnify.CHEVRON_RIGHT
            HomeComponentHeaderView.CTA_MODE_RELOAD -> IconUnify.RELOAD
            HomeComponentHeaderView.CTA_MODE_CLOSE -> IconUnify.CLOSE
            else -> IconUnify.CHEVRON_RIGHT
        }
        when (colorMode) {
            HomeComponentHeaderView.COLOR_MODE_NORMAL -> {
                ctaBorder?.loadImage(ContextCompat.getDrawable(context, home_component_headerR.drawable.home_component_header_bg_header_cta))
                ctaButtonRevamp?.setImage(
                    newIconId = iconId,
                    newLightEnable = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN900),
                    newDarkEnable = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950)
                )
            }
            HomeComponentHeaderView.COLOR_MODE_INVERTED -> {
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
            if (headerColorMode == HomeComponentHeaderView.COLOR_MODE_INVERTED) {
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
            if (headerColorMode == HomeComponentHeaderView.COLOR_MODE_INVERTED) {
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
        val subtitleIcon: ImageView = itemView.findViewById(R.id.header_icon_subtitle)
        if (channelHeader.subtitle.isNotEmpty() && channelHeader.iconSubtitleUrl.isNotEmpty()) {
            subtitleIcon.loadImage(channelHeader.iconSubtitleUrl)
            subtitleIcon.visible()
        } else {
            subtitleIcon.gone()
        }
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
