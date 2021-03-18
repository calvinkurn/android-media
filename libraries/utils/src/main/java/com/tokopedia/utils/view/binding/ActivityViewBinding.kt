package com.tokopedia.utils.view.binding

import android.app.Activity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.ViewBindingCache
import com.tokopedia.utils.view.binding.internal.findRootView
import com.tokopedia.utils.view.binding.noreflection.ViewBindingProperty
import com.tokopedia.utils.view.binding.noreflection.viewBinding

@JvmName("viewBindingActivity")
public inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(@IdRes viewBindingRootId: Int) =
        viewBinding(T::class.java, viewBindingRootId)

@JvmName("viewBindingActivity")
public fun <T : ViewBinding> ComponentActivity.viewBinding(
        viewBindingClass: Class<T>,
        @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity ->
        val rootView = ActivityCompat.requireViewById<View>(activity, viewBindingRootId)
        ViewBindingCache.getBind(viewBindingClass).bind(rootView)
    }
}

@JvmName("viewBindingActivity")
public fun <T : ViewBinding> ComponentActivity.viewBinding(
        viewBindingClass: Class<T>,
        rootViewProvider: (ComponentActivity) -> View
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity -> ViewBindingCache.getBind(viewBindingClass).bind(rootViewProvider(activity)) }
}

@JvmName("inflateViewBindingActivity")
public inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(
        createMethod: CreateMethod = CreateMethod.BIND
) = viewBinding(T::class.java, createMethod)

@JvmName("inflateViewBindingActivity")
public fun <T : ViewBinding> ComponentActivity.viewBinding(
        viewBindingClass: Class<T>,
        createMethod: CreateMethod = CreateMethod.BIND
): ViewBindingProperty<ComponentActivity, T> = when (createMethod) {
    CreateMethod.BIND -> viewBinding(viewBindingClass, ::findRootView)
    CreateMethod.INFLATE -> viewBinding {
        ViewBindingCache.getInflateWithLayoutInflater(viewBindingClass).inflate(layoutInflater, null, false)
    }
}