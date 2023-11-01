package com.tokopedia.home_component.util

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.home_component.R
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
class DynamicIconSmallUtil: DynamicIconUtil() {

    override fun findMaxHeight(
        icons: List<DynamicIconComponent.DynamicIcon>,
        context: Context
    ): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }
}
