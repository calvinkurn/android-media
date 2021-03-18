package com.tokopedia.utils.view.binding.noreflection

import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.findRootView
import com.tokopedia.utils.view.binding.internal.requireViewByIdCompat

@RestrictTo(LIBRARY)
private class ActivityViewBindingProperty<in A : ComponentActivity, out T : ViewBinding>(
        viewBinder: (A) -> T
) : LifecycleViewBindingProperty<A, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }
}

@JvmName("viewBindingActivity")
public fun <A : ComponentActivity, T : ViewBinding> ComponentActivity.viewBinding(
        viewBinder: (A) -> T
): ViewBindingProperty<A, T> {
    return ActivityViewBindingProperty(viewBinder)
}

@JvmName("viewBindingActivity")
public inline fun <A : ComponentActivity, T : ViewBinding> ComponentActivity.viewBinding(
        crossinline vbFactory: (View) -> T,
        crossinline viewProvider: (A) -> View = ::findRootView
): ViewBindingProperty<A, T> {
    return viewBinding { activity -> vbFactory(viewProvider(activity)) }
}

@Suppress("unused")
@JvmName("viewBindingActivity")
public inline fun <T : ViewBinding> ComponentActivity.viewBinding(
        crossinline vbFactory: (View) -> T,
        @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity -> vbFactory(activity.requireViewByIdCompat(viewBindingRootId)) }
}