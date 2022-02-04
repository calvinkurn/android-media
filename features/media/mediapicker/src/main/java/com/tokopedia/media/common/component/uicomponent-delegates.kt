package com.tokopedia.media.common.component

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

fun <Ui: BaseUiComponent> LifecycleOwner.uiComponent(
    needImmediateComponent: Boolean = false,
    componentCreation: (ViewGroup) -> Ui
): UiComponentDelegate<Ui> {
    return UiComponentDelegate(
        lifecycleOwner = this,
        componentCreation = componentCreation,
        needImmediateComponent = needImmediateComponent,
    )
}