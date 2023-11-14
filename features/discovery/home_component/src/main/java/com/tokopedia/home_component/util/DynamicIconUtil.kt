package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.home_component.model.DynamicIconComponent

/**
 * Created by frenzel
 */
abstract class DynamicIconUtil {
    abstract fun findMaxHeight(
        icons: List<DynamicIconComponent.DynamicIcon>,
        context: Context
    ): Int
}
