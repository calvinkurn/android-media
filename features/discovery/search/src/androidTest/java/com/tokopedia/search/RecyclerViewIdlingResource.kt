package com.tokopedia.search

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource

private const val NAME = "Recycler view idling resource"

internal class RecyclerViewIdlingResource(
        private val recyclerView: RecyclerView,
        private val name: String? = NAME
): IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val isIdle = recyclerView.adapter != null && recyclerView.adapter?.itemCount ?: 0 > 0
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback;
    }
}