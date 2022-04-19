package com.tokopedia.utils.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedNullableValue<T : Any?>(val fragment: Fragment, val onClear: ((T) -> Unit)?) : ReadWriteProperty<Fragment, T?> {
    private var _value: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment, { viewLifecycleOwner ->
                    viewLifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            _value?.let { onClear?.invoke(it) }
                            _value = null
                        }
                    })
                })
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        return _value
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        _value = value
    }
}

/**
 * Creates an [AutoClearedNullableValue] associated with this fragment.
 */
fun <T : Any?> Fragment.autoClearedNullable(onClear: ((T) -> Unit)? = null) = AutoClearedNullableValue<T>(this, onClear)