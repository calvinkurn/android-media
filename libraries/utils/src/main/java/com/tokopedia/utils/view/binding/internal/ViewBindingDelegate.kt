package com.tokopedia.utils.view.binding.internal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method

object ViewBindingCache {

    private val inflateCache = mutableMapOf<Class<out ViewBinding>, InflateViewBinding<ViewBinding>>()
    private val bindCache = mutableMapOf<Class<out ViewBinding>, BindViewBinding<ViewBinding>>()

    @Suppress("UNCHECKED_CAST")
    internal fun <T : ViewBinding> getInflateWithLayoutInflater(viewBindingClass: Class<T>): InflateViewBinding<T> {
        return inflateCache.getOrPut(viewBindingClass) { InflateViewBinding(viewBindingClass) } as InflateViewBinding<T>
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : ViewBinding> getBind(viewBindingClass: Class<T>): BindViewBinding<T> {
        return bindCache.getOrPut(viewBindingClass) { BindViewBinding(viewBindingClass) } as BindViewBinding<T>
    }

    fun clear() {
        inflateCache.clear()
        bindCache.clear()
    }
}

internal abstract class InflateViewBinding<out T : ViewBinding>(
        private val inflateViewBinding: Method
) {

    @Suppress("UNCHECKED_CAST")
    abstract fun inflate(
            layoutInflater: LayoutInflater,
            parent: ViewGroup?,
            attachToParent: Boolean
    ): T

}


@Suppress("FunctionName")
internal fun <T : ViewBinding> InflateViewBinding(viewBindingClass: Class<T>): InflateViewBinding<T> {
    val methodName = "inflate"

    return try {
        val method = viewBindingClass.getMethod(
                methodName,
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
        )

        InflationOfViewBinding(method)
    } catch (e: NoSuchMethodException) {
        val method = viewBindingClass.getMethod(
                methodName,
                LayoutInflater::class.java,
                ViewGroup::class.java
        )

        CombineInflateViewBinding(method)
    }
}

internal class InflationOfViewBinding<out T : ViewBinding>(
        private val inflateViewBinding: Method
) : InflateViewBinding<T>(inflateViewBinding) {

    @Suppress("UNCHECKED_CAST")
    override fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): T {
        return inflateViewBinding(null, layoutInflater, parent, attachToParent) as T
    }
}

internal class CombineInflateViewBinding<out T : ViewBinding>(
        private val inflateViewBinding: Method
) : InflateViewBinding<T>(inflateViewBinding) {

    @Suppress("UNCHECKED_CAST")
    override fun inflate(
            layoutInflater:
            LayoutInflater,
            parent: ViewGroup?,
            attachToParent: Boolean
    ): T {
        require(attachToParent) {
            "TkpdViewBinding: ${InflateViewBinding::class.java.simpleName} supports inflate only the attachToParent is true"
        }

        return inflateViewBinding(null, layoutInflater, parent) as T
    }
}

internal class BindViewBinding<out T : ViewBinding>(viewBindingClass: Class<T>) {

    private val bindViewBinding = viewBindingClass.getMethod("bind", View::class.java)

    @Suppress("UNCHECKED_CAST")
    fun bind(view: View): T {
        return bindViewBinding(null, view) as T
    }
}