package com.tokopedia.play_common.viewcomponent

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by jegul on 31/07/20
 */
class ViewComponentDelegate<VC: IViewComponent>(
        owner: LifecycleOwner,
        private val isEagerInit: Boolean,
        private val viewComponentCreator: (container: ViewGroup) -> VC
) : ReadOnlyProperty<LifecycleOwner, VC> {

    private var viewComponent: VC? = null

    init {
        if (isEagerInit)
            owner.safeAddObserver(
                    getEagerInitLifecycleObserver(owner)
            )
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): VC {
        viewComponent?.let { return it }

        return if (!isEagerInit) getOrCreateValue(thisRef)
        else throw IllegalStateException("ViewComponent is eager init but has not been initialized")
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

    private fun LifecycleOwner.createView(viewComponentCreator: (container: ViewGroup) -> VC, viewGroup: ViewGroup): VC {
        return viewComponentCreator(viewGroup).also {
            lifecycle.addObserver(it)
            viewComponent = it
        }
    }

    private fun getOrCreateValue(owner: LifecycleOwner): VC = synchronized(this@ViewComponentDelegate) {
        viewComponent?.let { return it }

        owner.safeAddObserver(getOnDestroyLifecycleObserver(owner))

        val lifecycleOwner = getValidLifecycleOwner(owner)

        val lifecycle = lifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("View Component has not been initialized")
        }

        return lifecycleOwner.createView(viewComponentCreator, getRootView(owner))
    }

    private fun LifecycleOwner.safeAddObserver(observer: LifecycleObserver) {
        if (this is Fragment) {
            viewLifecycleOwnerLiveData.observe(this, Observer { viewLifecycleOwner ->
                viewLifecycleOwner.lifecycle.addObserver(observer)
            })
        } else {
            lifecycle.addObserver(observer)
        }
    }

    private fun getEagerInitLifecycleObserver(owner: LifecycleOwner) = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            getOrCreateValue(owner)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            owner.lifecycle.removeObserver(this)
        }
    }

    private fun getOnDestroyLifecycleObserver(owner: LifecycleOwner) = object : LifecycleObserver {

        private val mainHandler = Handler(Looper.getMainLooper())

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            owner.lifecycle.removeObserver(this)

            mainHandler.post {
                viewComponent = null
            }
        }
    }
}