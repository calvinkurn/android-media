package com.tokopedia.home_component_header.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

class HeaderRevampLayoutStrategy : HeaderLayoutStrategy {
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

    override fun getLayout(): Int = R.layout.home_common_channel_header_revamp

    override fun renderCta(
        itemView: View,
        channelHeaderContainer: ConstraintLayout?,
        stubCtaButton: View?,
        stubSeeAllButton: View?,
        stubSeeAllButtonUnify: View?,
        channelHeader: ChannelHeader,
        hasSeeMoreApplink: Boolean,
        hasExpiredTime: Boolean,
        listener: HomeChannelHeaderListener?,
        ctaMode: Int?,
        colorMode: Int?
    ) {
        if (hasSeeMoreApplink || ctaMode != HomeChannelHeaderView.CTA_MODE_SEE_ALL) {
            if (stubCtaButton is ViewStub &&
                !isViewStubHasBeenInflated(stubCtaButton)
            ) {
                stubCtaButton.inflate()?.apply {
                    ctaButtonRevamp = findViewById(R.id.cta_button_revamp)
                    ctaButtonRevampContainer = findViewById(R.id.cta_container)
                    ctaBorder = findViewById(R.id.cta_border_revamp)
                }
            } else {
                ctaButtonRevamp = itemView.findViewById(R.id.cta_button_revamp)
                ctaButtonRevampContainer = itemView.findViewById(R.id.cta_container)
                ctaBorder = itemView.findViewById(R.id.cta_border_revamp)
            }

            setCtaIcon(
                itemView.context,
                ctaBorder,
                ctaButtonRevamp,
                ctaMode,
                colorMode
            )

            handleCtaConstraints(
                channelHeader,
                hasExpiredTime,
                channelHeaderContainer
            )

            ctaButtonRevampContainer?.show()
            ctaButtonRevamp?.setOnClickListener {
                when (ctaMode) {
                    HomeChannelHeaderView.CTA_MODE_SEE_ALL -> listener?.onSeeAllClick(channelHeader.getLink())
                    HomeChannelHeaderView.CTA_MODE_RELOAD -> {
                        it.startAnimation(rotateAnimation)
                        listener?.onReloadClick(channelHeader.channelId)
                    }
                    HomeChannelHeaderView.CTA_MODE_CLOSE -> listener?.onDismissClick(channelHeader.channelId)
                }
            }
        } else {
            ctaButtonRevampContainer?.hide()
        }
    }

    override fun renderIconSubtitle(
        itemView: View,
        channelHeader: ChannelHeader,
        stubChannelIconSubtitle: View?,
    ) {
        val subtitleIcon : ImageView
        if(hasIconSubtitle(channelHeader)){
            if (stubChannelIconSubtitle is ViewStub &&
                !isViewStubHasBeenInflated(stubChannelIconSubtitle)
            ){
                stubChannelIconSubtitle.inflate().apply {
                    subtitleIcon = findViewById(R.id.channel_subtitle_icon)
                }
            } else {
                subtitleIcon = itemView.findViewById(R.id.channel_subtitle_icon)
            }
            renderIcon(subtitleIcon, channelHeader)
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
            HomeChannelHeaderView.CTA_MODE_SEE_ALL -> IconUnify.CHEVRON_RIGHT
            HomeChannelHeaderView.CTA_MODE_RELOAD -> IconUnify.RELOAD
            HomeChannelHeaderView.CTA_MODE_CLOSE -> IconUnify.CLOSE
            else -> IconUnify.CHEVRON_RIGHT
        }
        when (colorMode) {
            HomeChannelHeaderView.COLOR_MODE_NORMAL -> {
                ctaBorder?.loadImage(ContextCompat.getDrawable(context, R.drawable.bg_channel_header_cta))
                ctaButtonRevamp?.setImage(
                    newIconId = iconId,
                    newLightEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900),
                    newDarkEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
            }
            HomeChannelHeaderView.COLOR_MODE_INVERTED -> {
                ctaBorder?.loadImage(ContextCompat.getDrawable(context, R.drawable.bg_channel_header_cta_inverted))
                ctaButtonRevamp?.setImage(
                    newIconId = iconId,
                    newLightEnable = ContextCompat.getColor(context, com.tokopedia.home_component_header.R.color.dms_header_cta_inverted_icon),
                    newDarkEnable = ContextCompat.getColor(context, com.tokopedia.home_component_header.R.color.dms_header_cta_inverted_icon)
                )
            }
        }
    }

    override fun renderTitle(
        context: Context,
        channelHeader: ChannelHeader,
        channelTitle: Typography?,
        headerColorMode: Int?
    ) {
        channelTitle?.setTextColor(
            if (headerColorMode == HomeChannelHeaderView.COLOR_MODE_INVERTED) {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
            } else if (channelHeader.textColor.isNotEmpty()) {
                Color.parseColor(channelHeader.textColor).staticIfDarkMode(context)
            } else {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            }
        )
    }

    override fun renderSubtitle(
        context: Context,
        channelHeader: ChannelHeader,
        channelSubtitle: Typography?,
        headerColorMode: Int?
    ) {
        channelSubtitle?.setTextColor(
            if (headerColorMode == HomeChannelHeaderView.COLOR_MODE_INVERTED) {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
            } else if (channelHeader.textColor.isNotEmpty()) {
                Color.parseColor(channelHeader.textColor).staticIfDarkMode(context)
            } else {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
            }
        )
    }

    /**
     * Requirement (revamp):
     * `see all` button align to center between title and subtitle/countdown timer
     */
    override fun handleCtaConstraints(
        channelHeader: ChannelHeader,
        hasExpiredTime: Boolean,
        channelHeaderContainer: ConstraintLayout?
    ) {
        val bottomAnchor = if (channelHeader.subtitle.isEmpty() && !hasExpiredTime) {
            R.id.channel_title
        } else if (hasExpiredTime) {
            R.id.count_down
        } else {
            R.id.channel_subtitle
        }
        val constraintSet = ConstraintSet()
        constraintSet.clone(channelHeaderContainer)
        constraintSet.connect(R.id.cta_button_revamp, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
        constraintSet.connect(R.id.cta_button_revamp, ConstraintSet.BOTTOM, bottomAnchor, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(channelHeaderContainer)
    }

    override fun setSubtitleConstraints(
        hasExpiredTime: Boolean,
        channelHeaderContainer: ConstraintLayout?,
        resources: Resources
    ) {
        if (hasExpiredTime) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelHeaderContainer)
            constraintSet.connect(R.id.channel_subtitle, ConstraintSet.TOP, R.id.count_down, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.channel_subtitle, ConstraintSet.BOTTOM, R.id.count_down, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelHeaderContainer)
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelHeaderContainer)
            constraintSet.connect(R.id.channel_subtitle, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.BOTTOM, resources.getDimensionPixelSize(R.dimen.home_channel_header_subtitle_top_padding))
            constraintSet.connect(R.id.channel_subtitle, ConstraintSet.BOTTOM, R.id.channel_title_container, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelHeaderContainer)
        }
    }

    override fun setContainerPadding(
        channelHeaderContainer: ConstraintLayout,
        hasExpiredTime: Boolean,
        resources: Resources
    ) {
        channelHeaderContainer.setPadding(channelHeaderContainer.paddingLeft, channelHeaderContainer.paddingTop, channelHeaderContainer.paddingRight, resources.getDimensionPixelSize(R.dimen.home_channel_header_bottom_padding))
    }

    private fun hasIconSubtitle(channelHeader: ChannelHeader): Boolean {
        return !TextUtils.isEmpty(channelHeader.iconSubtitleUrl)
    }

    private fun renderIcon(channelIconSubtitle:  ImageView, channelHeader: ChannelHeader){
        channelIconSubtitle.loadImage(channelHeader.iconSubtitleUrl)
        channelIconSubtitle.visibility = View.VISIBLE
    }
}
