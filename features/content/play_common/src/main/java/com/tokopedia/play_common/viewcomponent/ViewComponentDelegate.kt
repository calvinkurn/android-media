package com.tokopedia.play_common.viewcomponent

import android.app.Activity
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
) : ReadOnlyProperty<LifecycleOwner, VC> {

    private var viewComponent: VC? = null

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): VC {
        viewComponent?.let { return it }

        if (thisRef is Fragment) {
            thisRef.viewLifecycleOwnerLiveData.observe(thisRef, Observer { viewLifecycleOwner ->
                viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {

                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroy(owner: LifecycleOwner) {
                        viewComponent = null
                    }
                })
            })
        }

        val lifecycleOwner = getValidLifecycleOwner(thisRef)

        val lifecycle = lifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("View Component has not been initialized")
        }

        return viewComponentCreator(getRootView(thisRef)).also {
            lifecycleOwner.lifecycle.addObserver(it)
            viewComponent = it
        }
    }

    private fun getValidLifecycleOwner(owner: LifecycleOwner): LifecycleOwner {
        return if (owner is Fragment) owner.viewLifecycleOwner else owner
    }

    private fun getRootView(owner: LifecycleOwner): ViewGroup {
        val rootView = when (owner) {
            is Fragment -> owner.requireView()
            is Activity -> (owner.findViewById(android.R.id.content) as ViewGroup).getChildAt( 0)
            else -> throw IllegalStateException("Lifecycle owner with type ${owner.javaClass.simpleName} is not supported")
        }

        return rootView as ViewGroup
    }
}