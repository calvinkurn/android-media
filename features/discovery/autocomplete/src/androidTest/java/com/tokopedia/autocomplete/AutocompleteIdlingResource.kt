package com.tokopedia.autocomplete

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource

private const val NAME = "Recycler view has item idling resource"

internal class AutocompleteIdlingResource(
        private val recyclerView: RecyclerView?,
        private val name: String? = NAME
): IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val recyclerView = recyclerView ?: return true

        val isIdle = recyclerView.adapter.hasItem()
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    private fun RecyclerView.Adapter<*>?.hasItem(): Boolean {
        return this != null && this.itemCount > 0
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}