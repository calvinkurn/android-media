package com.tokopedia.home_component_header.view

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import android.view.ViewStub
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal interface HeaderLayoutStrategy {
    fun getLayout(): Int

    fun renderCta(
        itemView: View,
        channelHeaderContainer: ConstraintLayout?,
        stubCtaButton: View?,
        channelHeader: ChannelHeader,
        hasSeeMoreApplink: Boolean,
        hasExpiredTime: Boolean,
        listener: HomeChannelHeaderListener?,
        ctaMode: Int?,
        colorMode: Int?
    )

    fun renderTitle(
        context: Context,
        channelHeader: ChannelHeader,
        channelTitle: Typography?,
        headerColorMode: Int?
    )

    /**
     * @param itemView is view
     * @param icon url
     * @param stubChannelIconSubtitle view is image for icon
     * @param channelHeader pojo model
     *
     * Only show channel icon subtitle when it is exist
     */
    fun renderIconSubtitle(
        itemView: View,
        channelHeader: ChannelHeader,
        stubChannelIconSubtitle: View?,
    ){

    }

    fun renderSubtitle(
        context: Context,
        channelHeader: ChannelHeader,
        channelSubtitle: Typography?,
        headerColorMode: Int?
    )

    fun handleCtaConstraints(
        channelHeader: ChannelHeader,
        hasExpiredTime: Boolean,
        channelHeaderContainer: ConstraintLayout?
    )

    fun setSubtitleConstraints(
        hasExpiredTime: Boolean,
        channelHeaderContainer: ConstraintLayout?,
        resources: Resources
    )

    fun setContainerPadding(
        channelHeaderContainer: ConstraintLayout,
        hasExpiredTime: Boolean,
        resources: Resources
    )

    fun Int.staticIfDarkMode(context: Context?): Int {
        return if(context != null && context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950)
        } else this
    }

    fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }
}
