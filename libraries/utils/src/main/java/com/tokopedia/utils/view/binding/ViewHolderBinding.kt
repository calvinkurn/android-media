@file:JvmName("ReflectionViewHolderBinding")
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.tokopedia.utils.view.binding

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.ViewBindingCache
import com.tokopedia.utils.view.binding.noreflection.ViewBindingProperty
import com.tokopedia.utils.view.binding.noreflection.viewBinding

@JvmName("viewBindingFragment")
inline fun <reified T : ViewBinding> ViewHolder.viewBinding() = viewBinding(T::class.java)

@JvmName("viewBindingFragment")
fun <T : ViewBinding> ViewHolder.viewBinding(
        viewBindingClass: Class<T>,
): ViewBindingProperty<ViewHolder, T> {
    return viewBinding { ViewBindingCache.getBind(viewBindingClass).bind(itemView) }
}