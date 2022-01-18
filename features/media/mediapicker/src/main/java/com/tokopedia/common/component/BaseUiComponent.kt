package com.tokopedia.common.component

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.LifecycleObserver

interface BaseUiComponent : LifecycleObserver {
    fun componentView(): View
    fun resources(): Resources
    fun show()
    fun hide()
    fun isShown(): Boolean
    fun isHidden(): Boolean
}