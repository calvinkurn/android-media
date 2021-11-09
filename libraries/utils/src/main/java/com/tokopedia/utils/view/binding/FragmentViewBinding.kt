@file:JvmName("ReflectionFragmentViewBinding")
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.tokopedia.utils.view.binding

import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.MethodType
import com.tokopedia.utils.view.binding.internal.ViewBindingCache
import com.tokopedia.utils.view.binding.internal.getRootView
import com.tokopedia.utils.view.binding.internal.requireViewByIdCompat
import com.tokopedia.utils.view.binding.noreflection.ViewBindingProperty
import com.tokopedia.utils.view.binding.noreflection.viewBinding

@JvmName("viewBindingFragment")
inline fun <reified T : ViewBinding> Fragment.viewBinding(
        @IdRes viewBindingRootId: Int
): ViewBindingProperty<Fragment, T> {
    return viewBinding(T::class.java, viewBindingRootId)
}

@JvmName("viewBindingFragment")
fun <T : ViewBinding> Fragment.viewBinding(
        viewBindingClass: Class<T>,
        @IdRes viewBindingRootId: Int
): ViewBindingProperty<Fragment, T> {
    return when (this) {
        is DialogFragment -> {
            viewBinding { dialogFragment ->
                require(dialogFragment is DialogFragment)
                ViewBindingCache.getBind(viewBindingClass).bind(dialogFragment.getRootView(viewBindingRootId))
            }
        }
        else -> {
            viewBinding {
                ViewBindingCache.getBind(viewBindingClass).bind(requireView().requireViewByIdCompat(viewBindingRootId))
            }
        }
    }
}

@JvmName("viewBindingFragment")
inline fun <reified T : ViewBinding> Fragment.viewBinding(
        methodType: MethodType = MethodType.Bind
): ViewBindingProperty<Fragment, T> {
    return viewBinding(T::class.java, methodType)
}

@JvmName("viewBindingFragment")
fun <T : ViewBinding> Fragment.viewBinding(
        viewBindingClass: Class<T>,
        methodType: MethodType = MethodType.Bind
): ViewBindingProperty<Fragment, T> {
    return when (methodType) {
        MethodType.Bind -> viewBinding {
            ViewBindingCache.getBind(viewBindingClass).bind(requireView())
        }
        MethodType.Inflate -> viewBinding {
            ViewBindingCache.getInflateWithLayoutInflater(viewBindingClass).inflate(layoutInflater, null, false)
        }
    }
}