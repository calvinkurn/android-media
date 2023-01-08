package com.tokopedia.profilecompletion.profilecompletion.view.util

import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import rx.Observable
import rx.functions.Action1

/**
 * Created by stevenfredian on 7/13/17.
 * see Properties on abstraction
 */
object ProfileCompletionProperties {
    /**
     * Updates the *enabled* property of a view for each event received.
     */
    fun enabledFrom(view: View): EnabledProperty {
	return EnabledProperty(view)
    }

    /**
     * Updates the *text* property of a view for each event received.
     */
    fun textFrom(view: TextView): TextProperty {
	return TextProperty(view)
    }

    /**
     * Clear, update and notify changes of dataset for `ArrayAdapter`.
     */
    fun <T> dataSetFrom(adapter: ArrayAdapter<T>): ArrayAdapterProperty<T> {
	return ArrayAdapterProperty(adapter)
    }

    class EnabledProperty constructor(private val view: View) : Action1<Boolean?> {
	fun set(observable: Observable<Boolean?>) {
	    observable.subscribe(this)
	}

	override fun call(enabled: Boolean?) {
	    view.isEnabled = enabled ?: false
	}

    }

    class TextProperty constructor(private val view: TextView) : Action1<String?> {
	fun set(observable: Observable<String?>) {
	    observable.subscribe(this)
	}

	override fun call(text: String?) {
	    view.text = text
	}

    }

    class ArrayAdapterProperty<T> constructor(private val adapter: ArrayAdapter<T>) :
	Action1<List<T>> {
	fun set(observable: Observable<List<T>>) {
	    observable.subscribe(this)
	}

	override fun call(items: List<T>) {
	    adapter.clear()
	    for (item in items) {
		adapter.add(item)
	    }
	    adapter.notifyDataSetChanged()
	}

    }
}