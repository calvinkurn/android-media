@file:JvmName("ViewHolderBinding")
@file:Suppress("unused")

package com.tokopedia.utils.view.binding.noreflection

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.tokopedia.utils.view.binding.internal.requireViewByIdCompat

fun <V : ViewHolder, T : ViewBinding> ViewHolder.viewBinding(
    viewBinder: (V) -> T,
    onClear: T?.() -> Unit? = {}
): ViewBindingProperty<V, T> {
    return LazyViewBindingProperty(viewBinder, onClear)
}

inline fun <V : ViewHolder, T : ViewBinding> ViewHolder.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        crossinline viewProvider: (V) -> View = ViewHolder::itemView,
        noinline onClear: T?.() -> Unit? = {}
): ViewBindingProperty<V, T> {
    return LazyViewBindingProperty(
        { viewHolder: V -> viewProvider(viewHolder).let(viewBindingFactory) },
        onClear
    )
}

inline fun <V : ViewHolder, T : ViewBinding> ViewHolder.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        @IdRes viewBindingRootId: Int,
        noinline onClear: T?.() -> Unit? = {}
): ViewBindingProperty<V, T> {
    return LazyViewBindingProperty(
        { viewHolder: V ->
            viewBindingFactory(viewHolder.itemView.requireViewByIdCompat(viewBindingRootId))
        },
        onClear
    )
}