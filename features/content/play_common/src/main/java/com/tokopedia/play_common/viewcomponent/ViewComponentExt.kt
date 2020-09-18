package com.tokopedia.play_common.viewcomponent

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

/**
 * Created by jegul on 31/07/20
 */
fun <VC: IViewComponent> LifecycleOwner.viewComponent(
        isEagerInit: Boolean = false,
        creator: (ViewGroup) -> VC
) : ViewComponentDelegate<VC> {
    return viewComponent(isEagerInit, creator, {})
}

fun <VC: IViewComponent> LifecycleOwner.viewComponent(
        isEagerInit: Boolean = false,
        creator: (ViewGroup) -> VC,
        onDestroy: (VC) -> Unit = {}
) : ViewComponentDelegate<VC> {
    return ViewComponentDelegate(owner = this, isEagerInit = isEagerInit, viewComponentCreator = creator, onDestroy = onDestroy)
}

fun <VC: IViewComponent> LifecycleOwner.viewComponentOrNull(
        isEagerInit: Boolean = false,
        creator: (ViewGroup) -> VC
) : ViewComponentNullableDelegate<VC> {
    return viewComponentOrNull(isEagerInit, creator, {})
}

fun <VC: IViewComponent> LifecycleOwner.viewComponentOrNull(
        isEagerInit: Boolean = false,
        creator: (ViewGroup) -> VC,
        onDestroy: (VC) -> Unit = {}
) : ViewComponentNullableDelegate<VC> {
    return ViewComponentNullableDelegate(owner = this, isEagerInit = isEagerInit, viewComponentCreator = creator, onDestroy = onDestroy)
}