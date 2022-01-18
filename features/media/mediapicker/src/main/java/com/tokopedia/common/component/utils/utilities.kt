package com.tokopedia.common.component.utils

import android.app.Activity
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.common.component.BaseUiComponent

fun LifecycleOwner.getSafeLifecycleOwner(): LifecycleOwner {
    if (this is Fragment) return this.viewLifecycleOwner
    return this
}

fun <Ui: BaseUiComponent> LifecycleOwner.createComponentView(
    creation: (ViewGroup) -> Ui,
    viewGroup: ViewGroup
): Ui {
    return creation(viewGroup).also {
        lifecycle.addObserver(it)
    }
}

fun LifecycleOwner.addSafeObserver(observer: LifecycleObserver) {
    if (this is Fragment) {
        viewLifecycleOwnerLiveData.observe(this, {
            viewLifecycleOwner.lifecycle.addObserver(observer)
        })
    } else {
        lifecycle.addObserver(observer)
    }
}

fun LifecycleOwner.getRootCurrentView(): ViewGroup {
    val rootView = when (this) {
        is Activity -> (this.findViewById(android.R.id.content) as ViewGroup)
            .children
            .first()
        is Fragment -> this.requireView()
        else -> error("The type of this owner is not supported yet.")
    }

    return rootView as ViewGroup
}