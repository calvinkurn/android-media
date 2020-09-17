package com.tokopedia.play_common.viewcomponent

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

/**
 * Created by jegul on 31/07/20
 */
fun <VC: IViewComponent> LifecycleOwner.viewComponent(creator: (ViewGroup) -> VC) : ViewComponentDelegate<VC> {
    return ViewComponentDelegate(creator)
}

fun <VC: IViewComponent> LifecycleOwner.viewComponentOrNull(creator: (ViewGroup) -> VC) : ViewComponentNullableDelegate<VC> {
    return ViewComponentNullableDelegate(creator)
}