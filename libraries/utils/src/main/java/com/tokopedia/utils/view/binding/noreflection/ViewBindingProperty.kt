@file:Suppress("RedundantVisibilityModifier", "UNCHECKED_CAST")

package com.tokopedia.utils.view.binding.noreflection

import android.annotation.SuppressLint
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface ViewBindingProperty<in R : Any, T : ViewBinding?> : ReadWriteProperty<R, T?> {
    @MainThread fun clear()
}

open class LazyViewBindingProperty<in R : Any, T : ViewBinding>(
        protected val viewBinder: (R) -> T,
        private val onClear: T?.() -> Unit?
) : ViewBindingProperty<R, T> {

    protected var viewBinding: Any? = null

    @Suppress("UNCHECKED_CAST")
    @MainThread override fun getValue(thisRef: R, property: KProperty<*>): T {
        return viewBinding as? T ?: viewBinder(thisRef).also { viewBinding ->
            this.viewBinding = viewBinding
        }
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T?) {
        viewBinding = value
    }

    @MainThread override fun clear() {
        onClear.invoke(viewBinding as? T)
        viewBinding = null
    }
}

abstract class LifecycleViewBindingProperty<in R : Any, T : ViewBinding?>(
        private val viewBinder: (R) -> T?,
        private val onClear: T?.() -> Unit?
) : ViewBindingProperty<R, T> {

    private var viewBinding: T? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @SuppressLint("LogNotTimber")
    @MainThread override fun getValue(thisRef: R, property: KProperty<*>): T? {
        viewBinding?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val viewBinding = viewBinder(thisRef)

        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)) {
            this.viewBinding = viewBinding
            lifecycle.addObserver(ClearViewBindingLifecycle(onClear))
        }

        return viewBinding
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T?) {
        viewBinding = value
    }

    @MainThread override fun clear() {
        viewBinding = null
    }

    private inner class ClearViewBindingLifecycle constructor(
        val onClear: T?.() -> Unit?
    ) : DefaultLifecycleObserver {
        @MainThread override fun onDestroy(owner: LifecycleOwner) {
            onClear.invoke(viewBinding)
            clear()
        }
    }
}