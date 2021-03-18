package com.tokopedia.utils.view.binding.noreflection

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.requireViewByIdCompat

fun <VH : ViewHolder, T : ViewBinding> VH.viewBinding(viewBinder: (VH) -> T): ViewBindingProperty<VH, T> {
    return LazyViewBindingProperty(viewBinder)
}

inline fun <VH : ViewHolder, T : ViewBinding> VH.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        crossinline viewProvider: (VH) -> View = ViewHolder::itemView,
): ViewBindingProperty<VH, T> {
    return LazyViewBindingProperty { viewHolder: VH -> viewProvider(viewHolder).let(viewBindingFactory) }
}

inline fun <VH : ViewHolder, T : ViewBinding> VH.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        @IdRes viewBindingRootId: Int,
): ViewBindingProperty<VH, T> {
    return LazyViewBindingProperty { viewHolder: VH ->
        viewBindingFactory(viewHolder.itemView.requireViewByIdCompat(viewBindingRootId))
    }
}