package com.tokopedia.play_common.delegate

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by jegul on 26/04/21
 */
class FragmentViewDelegate<V: View>(
        @IdRes val idRes: Int
) : ReadOnlyProperty<Fragment, V> {

    private var mView: V? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        mView?.let { return it }

        return createValue(thisRef)
    }

    private fun createValue(fragment: Fragment): V = synchronized(this@FragmentViewDelegate) {
        require(fragment is FragmentWithDetachableView)

        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            viewLifecycleOwner.lifecycle.addObserver(getViewLifecycleObserver(fragment))
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Fragment View Lifecycle Owner is not initialized")
        }

        val view = fragment.requireView().findViewById(idRes) as V

        fragment.getViewContainer().addView(view)

        return@synchronized view
    }

    private fun getViewLifecycleObserver(fragment: FragmentWithDetachableView): LifecycleObserver {
        val container = fragment.getViewContainer()
        val lifecycleObserver = container.getViewLifecycleObserver()
        return if (lifecycleObserver == null) {
            val viewLifecycleObserver = createViewLifecycleObserver(container)
            container.setViewLifecycleObserver(viewLifecycleObserver)
            viewLifecycleObserver
        } else {
            lifecycleObserver
        }
    }

    private fun createViewLifecycleObserver(container: FragmentViewContainer) = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            container.clearViews()
        }
    }
}