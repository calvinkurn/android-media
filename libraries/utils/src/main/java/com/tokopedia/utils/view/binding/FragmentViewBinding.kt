@file:JvmName("ReflectionFragmentViewBinding")
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.tokopedia.utils.view.binding

import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.MethodType
import com.tokopedia.utils.view.binding.internal.ViewBindingMethodBinder
import com.tokopedia.utils.view.binding.internal.getRootView
import com.tokopedia.utils.view.binding.internal.requireViewByIdCompat
import com.tokopedia.utils.view.binding.noreflection.ViewBindingProperty
import com.tokopedia.utils.view.binding.noreflection.viewBinding

@JvmName("viewBindingFragment")
inline fun <reified T : ViewBinding> Fragment.viewBinding(
        @IdRes viewBindingRootId: Int,
        noinline onClear: T?.() -> Unit? = {}
): ViewBindingProperty<Fragment, T> {
    return viewBinding(T::class.java, viewBindingRootId, onClear)
}

@JvmName("viewBindingFragment")
fun <T : ViewBinding> Fragment.viewBinding(
        viewBindingClass: Class<T>,
        @IdRes viewBindingRootId: Int,
        onClear: T?.() -> Unit? = {}
): ViewBindingProperty<Fragment, T> {
    return when (this) {
        is DialogFragment -> {
            viewBinding(
                { dialogFragment ->
                    require(dialogFragment is DialogFragment)
                    ViewBindingMethodBinder.getBind(viewBindingClass).bind(dialogFragment.getRootView(viewBindingRootId))
                },
                onClear
            )
        }
        else -> {
            viewBinding(
                { ViewBindingMethodBinder.getBind(viewBindingClass).bind(requireView().requireViewByIdCompat(viewBindingRootId)) },
                onClear
            )
        }
    }
}

@JvmName("viewBindingFragment")
inline fun <reified T : ViewBinding> Fragment.viewBinding(
        methodType: MethodType = MethodType.Bind,
        noinline onClear: T?.() -> Unit? = {}
): ViewBindingProperty<Fragment, T> {
    return viewBinding(T::class.java, methodType, onClear)
}

@JvmName("viewBindingFragment")
fun <T : ViewBinding> Fragment.viewBinding(
        viewBindingClass: Class<T>,
        methodType: MethodType = MethodType.Bind,
        onClear: T?.() -> Unit? = {}
): ViewBindingProperty<Fragment, T> {
    return when (methodType) {
        MethodType.Bind -> viewBinding(
            { ViewBindingMethodBinder.getBind(viewBindingClass).bind(requireView()) },
            onClear
        )
        MethodType.Inflate -> viewBinding(
            { ViewBindingMethodBinder.getInflateWithLayoutInflater(viewBindingClass).inflate(layoutInflater, null, false) },
            onClear
        )
    }
}