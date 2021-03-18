package com.tokopedia.utils.view.binding

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.ViewBindingCache
import com.tokopedia.utils.view.binding.noreflection.ViewBindingProperty
import com.tokopedia.utils.view.binding.noreflection.viewBinding

@JvmName("viewBindingFragment")
public inline fun <reified T : ViewBinding> ViewHolder.viewBinding() = viewBinding(T::class.java)

@JvmName("viewBindingFragment")
public fun <T : ViewBinding> ViewHolder.viewBinding(
        viewBindingClass: Class<T>,
): ViewBindingProperty<ViewHolder, T> {
    return viewBinding { ViewBindingCache.getBind(viewBindingClass).bind(itemView) }
}