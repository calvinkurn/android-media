package com.tokopedia.home_component.customview.header

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewStub
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.util.getLink
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class HeaderControlLayoutStrategy : HeaderLayoutStrategy {
    var seeAllButton: TextView? = null
    var seeAllButtonUnify: UnifyButton? = null

    override fun getLayout(): Int = R.layout.home_component_dynamic_channel_header

    override fun renderCta(
        itemView: View,
        channelHeaderContainer: ConstraintLayout?,
        stubCtaButton: View?,
        stubSeeAllButton: View?,
        stubSeeAllButtonUnify: View?,
        channel: ChannelModel,
        hasSeeMoreApplink: Boolean,
        hasExpiredTime: Boolean,
        listener: HeaderListener?,
        ctaMode: Int?,
        colorMode: Int?
    ) {
        if (channel.channelHeader.backImage.isNotBlank()) {
            handleUnifySeeAllButton(
                itemView,
                stubSeeAllButtonUnify,
                channel.channelHeader,
                hasSeeMoreApplink,
                listener
            )
        } else {
            handleSeeAllButton(
                itemView,
                stubSeeAllButton,
                channel,
                hasSeeMoreApplink,
                listener
            )
        }
        handleCtaConstraints(
            channel.channelHeader,
            hasExpiredTime,
            channelHeaderContainer
        )
    }

    private fun handleSeeAllButton(
        itemView: View,
        stubSeeAllButton: View?,
        channel: ChannelModel,
        hasSeeMoreApplink: Boolean,
        listener: HeaderListener?
    ) {
        if (hasSeeMoreApplink) {
            seeAllButton = if (stubSeeAllButton is ViewStub &&
                !isViewStubHasBeenInflated(stubSeeAllButton)
            ) {
                val stubSeeAllView = stubSeeAllButton.inflate()
                stubSeeAllView?.findViewById(R.id.see_all_button)
            } else {
                itemView.findViewById(R.id.see_all_button)
            }

            if (channel.style == ChannelStyle.ChannelHome) {
                seeAllButton?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            } else if (channel.style == ChannelStyle.ChannelOS) {
                seeAllButton?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_PN600))
            }

            seeAllButton?.show()
            seeAllButton?.setOnClickListener {
                listener?.onSeeAllClick(channel.channelHeader.getLink())
            }
        } else {
            stubSeeAllButton?.hide()
            seeAllButtonUnify?.hide()
            seeAllButton?.hide()
        }
    }

    private fun handleUnifySeeAllButton(
        itemView: View,
        stubSeeAllButtonUnify: View?,
        channelHeader: ChannelHeader,
        hasSeeMoreApplink: Boolean,
        listener: HeaderListener?
    ) {
        /**
         * Requirement:
         * Show unify button of see more button for dc sprint if back image is not empty
         */
        if (hasSeeMoreApplink) {
            if (channelHeader.backImage.isNotBlank()) {
                seeAllButtonUnify = if (stubSeeAllButtonUnify is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButtonUnify)
                ) {
                    val stubSeeAllButtonView = stubSeeAllButtonUnify.inflate()
                    stubSeeAllButtonView?.findViewById(R.id.see_all_button_unify)
                } else {
                    itemView.findViewById(R.id.see_all_button_unify)
                }
            }

            seeAllButtonUnify?.setOnClickListener { listener?.onSeeAllClick(channelHeader.getLink()) }

            seeAllButtonUnify?.show()
        } else {
            stubSeeAllButtonUnify?.hide()
            seeAllButton?.hide()
            seeAllButtonUnify?.hide()
        }
    }

    override fun renderTitle(
        context: Context,
        channelHeader: ChannelHeader,
        channelTitle: Typography?,
        headerColorMode: Int?
    ) {
        channelTitle?.setTextColor(
            if (channelHeader.textColor.isNotEmpty()) {
                Color.parseColor(channelHeader.textColor).invertIfDarkMode(context)
            } else {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950).invertIfDarkMode(context)
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
            if (channelHeader.textColor.isNotEmpty()) {
                Color.parseColor(channelHeader.textColor).invertIfDarkMode(context)
            } else {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950).invertIfDarkMode(context)
            }
        )
    }

    /**
     * Requirement (control):
     * `see all` button align to subtitle and countdown timer
     */
    override fun handleCtaConstraints(
        channelHeader: ChannelHeader,
        hasExpiredTime: Boolean,
        channelHeaderContainer: ConstraintLayout?
    ) {
        if (channelHeader.backImage.isNotBlank()) {
            if (channelHeader.subtitle.isEmpty() && hasExpiredTime) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(channelHeaderContainer)
                constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.BOTTOM, R.id.channel_title, ConstraintSet.BOTTOM, 0)
                constraintSet.applyTo(channelHeaderContainer)
            } else {
                val constraintSet = ConstraintSet()
                constraintSet.clone(channelHeaderContainer)
                constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.TOP, R.id.count_down, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.BOTTOM, R.id.count_down, ConstraintSet.BOTTOM, 0)
                constraintSet.applyTo(channelHeaderContainer)
            }
        } else {
            if (channelHeader.subtitle.isEmpty() && !hasExpiredTime) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(channelHeaderContainer)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, R.id.channel_title, ConstraintSet.BOTTOM, 0)
                constraintSet.applyTo(channelHeaderContainer)
            } else {
                val constraintSet = ConstraintSet()
                constraintSet.clone(channelHeaderContainer)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.count_down, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, R.id.count_down, ConstraintSet.BOTTOM, 0)
                constraintSet.applyTo(channelHeaderContainer)
            }
        }
    }

    override fun setSubtitleConstraints(
        hasExpiredTime: Boolean,
        channelHeaderContainer: ConstraintLayout?,
        resources: Resources
    ) {
    }

    override fun setContainerPadding(
        channelHeaderContainer: ConstraintLayout,
        hasExpiredTime: Boolean,
        resources: Resources
    ) {
        if (hasExpiredTime) {
            channelHeaderContainer.setPadding(channelHeaderContainer.paddingLeft, channelHeaderContainer.paddingTop, channelHeaderContainer.paddingRight, resources.getDimensionPixelSize(R.dimen.home_dynamic_header_bottom_padding_with_timer_old))
        } else {
            channelHeaderContainer.setPadding(channelHeaderContainer.paddingLeft, channelHeaderContainer.paddingTop, channelHeaderContainer.paddingRight, resources.getDimensionPixelSize(R.dimen.home_dynamic_header_bottom_padding))
        }
    }
}
