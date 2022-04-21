package com.tokopedia.review.feature.ovoincentive.presentation.adapter.layoutmanager

import android.content.Context
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class IncentiveOvoIllustrationLayoutManager(
    context: Context
) : FlexboxLayoutManager(context, FlexDirection.ROW) {
    init {
        flexWrap = FlexWrap.WRAP
        justifyContent = JustifyContent.CENTER
    }
}