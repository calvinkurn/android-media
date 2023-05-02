package com.tokopedia.play.broadcaster.util.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RetainedComponentDelegate<T>(
    private val clazz: Class<out ViewModel>,
    private val owner: () -> ViewModelStoreOwner,
    private val componentCreator: () -> T,
) : ReadOnlyProperty<ViewModelStoreOwner, T> {

    private var value: T? = null

    override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
        val value = this.value
        if (value != null) return value

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return componentCreator() as T
            }
        }

        val component = ViewModelProvider(owner(), factory).get(clazz) as T
        this.value = component

        return component
    }
}
