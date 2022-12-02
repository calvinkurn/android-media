package com.tokopedia.picker.common.basecomponent

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.LifecycleObserver

interface BaseUiComponent : LifecycleObserver {
    fun container(): View
    fun resources(): Resources
    fun isShown(): Boolean
    fun isHidden(): Boolean
    fun release() {}
}