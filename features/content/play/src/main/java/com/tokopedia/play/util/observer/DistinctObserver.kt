package com.tokopedia.play.util.observer

import androidx.lifecycle.Observer

/**
 * Created by jegul on 11/05/20
 */
open class DistinctObserver<T>(private val onDistinctValue: (T) -> Unit) : Observer<T> {

    private var value : T? = null

    override fun onChanged(t: T) {
        if (value != t) {
            onDistinctValue(t)
            value = t
        }
    }
}