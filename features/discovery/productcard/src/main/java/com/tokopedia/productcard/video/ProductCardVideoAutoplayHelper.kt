package com.tokopedia.productcard.video

import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

class ProductCardVideoAutoplayHelper(
    private val remoteConfig: RemoteConfig,
    private val productVideoAutoplayFilter: ProductVideoAutoplayFilter,
    coroutineScope: CoroutineScope,
): LifecycleObserver, CoroutineScope by coroutineScope {
    private val productVideoAutoplay: ProductVideoAutoplay by lazy {
        ProductVideoAutoplay(productVideoAutoplayFilter, this)
    }

    private val isAutoplayProductVideoEnabled: Boolean by lazy {
        remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MPC_VIDEO_AUTOPLAY, true)
    }

    private var recyclerView: RecyclerView? = null

    private val autoPlayScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                startVideoAutoplayWhenRecyclerViewIsIdle()
            }
        }
    }

    private val autoPlayAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            if (positionStart == 0) {
                recyclerView?.viewTreeObserver
                    ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            startVideoAutoplayWhenRecyclerViewIsIdle()
                            recyclerView?.viewTreeObserver
                                ?.removeOnGlobalLayoutListener(this)
                        }
                    })
            }
        }
    }

    fun registerLifecycleObserver(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
        lifecycleOwner.lifecycle.addObserver(productVideoAutoplay)
    }

    fun setUpProductVideoAutoplayListener(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        if (isAutoplayProductVideoEnabled) {
            recyclerView.addOnScrollListener(autoPlayScrollListener)
            recyclerView.adapter?.registerAdapterDataObserver(autoPlayAdapterDataObserver)
        }
    }

    private fun startVideoAutoplayWhenRecyclerViewIsIdle() {
        productVideoAutoplay.startVideoAutoplay()
    }

    fun stopVideoAutoplay() {
        productVideoAutoplay.stopVideoAutoplay()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterVideoAutoplayAdapterObserver() {
        if (isAutoplayProductVideoEnabled) {
            try {
                recyclerView?.adapter?.unregisterAdapterDataObserver(autoPlayAdapterDataObserver)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
        recyclerView = null
    }
}