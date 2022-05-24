package com.tokopedia.officialstore.util

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter

private const val NAME = "Recycler view has item idling resource"

internal class OSRecyclerViewIdlingResource(
    private val activity: Activity? = null,
    private val name: String? = NAME,
    private val limitCountToIdle: Int = 2
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val activity = activity ?: return false
        val recyclerView = activity.findViewById<RecyclerView>(R.id.os_child_recycler_view)?: return false
        recyclerView.disableHomeAnimation()
        val isIdle = (recyclerView.adapter as? OfficialHomeAdapter).isContainsProductRecom()
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    private fun OfficialHomeAdapter?.isContainsProductRecom(): Boolean {
        return this != null && itemCount >= limitCountToIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}