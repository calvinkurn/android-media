package com.tokopedia.play_common.viewcomponent

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by jegul on 31/07/20
 */
class ViewComponentDelegate<VC: IViewComponent>(
        private val viewComponentCreator: (container: ViewGroup) -> VC
) : ReadOnlyProperty<Fragment, VC> {

    private var viewComponent: VC? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VC {
        viewComponent?.let { return it }

        thisRef.viewLifecycleOwnerLiveData.observe(thisRef, Observer { viewLifecycleOwner ->
            viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {

                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy(owner: LifecycleOwner) {
                    viewComponent = null
                }
            })
        })

        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("View Component has not been initialized")
        }

        return viewComponentCreator(thisRef.requireView() as ViewGroup).also {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(it)
            viewComponent = it
        }
    }
}