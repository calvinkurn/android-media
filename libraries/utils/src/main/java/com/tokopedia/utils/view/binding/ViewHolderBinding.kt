@file:JvmName("ReflectionViewHolderBinding")
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.tokopedia.utils.view.binding

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.ViewBindingMethodBinder
import com.tokopedia.utils.view.binding.noreflection.ViewBindingProperty
import com.tokopedia.utils.view.binding.noreflection.viewBinding

@JvmName("viewBindingFragment")
inline fun <reified T : ViewBinding> ViewHolder.viewBinding(
    noinline onClear: T?.() -> Unit? = {}
) = viewBinding(T::class.java, onClear)

@JvmName("viewBindingFragment")
fun <T : ViewBinding> ViewHolder.viewBinding(
        viewBindingClass: Class<T>,
        onClear: T?.() -> Unit? = {}
): ViewBindingProperty<ViewHolder, T> {
    return viewBinding(
        { ViewBindingMethodBinder.getBind(viewBindingClass).bind(itemView) },
        onClear
    )
}