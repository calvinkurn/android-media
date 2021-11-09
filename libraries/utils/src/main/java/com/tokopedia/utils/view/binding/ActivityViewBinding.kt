@file:JvmName("ReflectionActivityViewBinding")
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.tokopedia.utils.view.binding

import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.MethodType
import com.tokopedia.utils.view.binding.internal.ViewBindingCache
import com.tokopedia.utils.view.binding.internal.findRootView
import com.tokopedia.utils.view.binding.noreflection.ViewBindingProperty
import com.tokopedia.utils.view.binding.noreflection.viewBinding

@JvmName("viewBindingActivity")
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(@IdRes viewBindingRootId: Int) =
        viewBinding(T::class.java, viewBindingRootId)

@JvmName("viewBindingActivity")
fun <T : ViewBinding> ComponentActivity.viewBinding(
        viewBindingClass: Class<T>,
        @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity ->
        val rootView = ActivityCompat.requireViewById<View>(activity, viewBindingRootId)
        ViewBindingCache.getBind(viewBindingClass).bind(rootView)
    }
}

@JvmName("viewBindingActivity")
fun <T : ViewBinding> ComponentActivity.viewBinding(
        viewBindingClass: Class<T>,
        rootViewProvider: (ComponentActivity) -> View
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity -> ViewBindingCache.getBind(viewBindingClass).bind(rootViewProvider(activity)) }
}

@JvmName("inflateViewBindingActivity")
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(
        methodType: MethodType = MethodType.Bind
) = viewBinding(T::class.java, methodType)

@JvmName("inflateViewBindingActivity")
fun <T : ViewBinding> ComponentActivity.viewBinding(
        viewBindingClass: Class<T>,
        methodType: MethodType = MethodType.Bind
): ViewBindingProperty<ComponentActivity, T> {
    return when (methodType) {
        MethodType.Bind -> viewBinding(viewBindingClass, ::findRootView)
        MethodType.Inflate -> viewBinding {
            ViewBindingCache.getInflateWithLayoutInflater(viewBindingClass).inflate(layoutInflater, null, false)
        }
    }
}