@file:JvmName("ActivityViewBinding")
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.tokopedia.utils.view.binding.noreflection

import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.findRootView
import com.tokopedia.utils.view.binding.internal.requireViewByIdCompat

private class ActivityViewBindingProperty<in A : ComponentActivity, T : ViewBinding?>(
        viewBinder: (A) -> T?,
        onClear: T?.() -> Unit? = {}
) : LifecycleViewBindingProperty<A, T>(viewBinder, onClear) {

    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }

}

@JvmName("viewBindingActivity")
fun <A : ComponentActivity, T : ViewBinding> viewBinding(
        viewBinder: (A) -> T,
        onClear: T?.() -> Unit? = {}
): ViewBindingProperty<A, T> {
    return ActivityViewBindingProperty(viewBinder, onClear)
}

@JvmName("viewBindingActivity")
inline fun <A : ComponentActivity, T : ViewBinding> viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        crossinline viewProvider: (A) -> View = ::findRootView,
        noinline onClear: T?.() -> Unit? = {}
): ViewBindingProperty<A, T> {
    return viewBinding(
        { activity -> viewBindingFactory(viewProvider(activity)) },
        onClear
    )
}

@Suppress("unused")
@JvmName("viewBindingActivity")
inline fun <T : ViewBinding> ComponentActivity.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        @IdRes viewBindingRootId: Int,
        noinline onClear: T?.() -> Unit? = {}
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding(
        { activity -> viewBindingFactory(activity.requireViewByIdCompat(viewBindingRootId)) },
        onClear
    )
}