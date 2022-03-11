package com.tokopedia.picker.common.basecomponent

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

fun <Ui: BaseUiComponent> LifecycleOwner.uiComponent(
    componentCreation: (ViewGroup) -> Ui
): UiComponentDelegate<Ui> {
    return UiComponentDelegate(
        lifecycleOwner = this,
        eagerComponent = false,
        componentCreation = componentCreation,
    )
}