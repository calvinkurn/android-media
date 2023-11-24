package com.tokopedia.home_component_header.view

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal interface HeaderLayoutStrategy {
    fun getLayout(): Int

    fun renderCta(
        itemView: View,
        channelHeader: ChannelHeader,
        listener: HomeComponentHeaderListener?,
        ctaMode: Int?,
        colorMode: Int?
    )

    fun renderTitle(
        context: Context,
        channelHeader: ChannelHeader,
        txtTitle: Typography?,
        headerColorMode: Int?
    )

    fun renderSubtitle(
        context: Context,
        channelHeader: ChannelHeader,
        txtSubtitle: Typography?,
        headerColorMode: Int?
    )

    fun renderIconSubtitle(
        itemView: View,
        channelHeader: ChannelHeader,
    )

    fun setConstraints(
        headerContainer: ConstraintLayout?,
        channelHeader: ChannelHeader,
    )

    fun Int.staticIfDarkMode(context: Context?): Int {
        return if(context != null && context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950)
        } else this
    }
}
