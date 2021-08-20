package com.tokopedia.officialstore.util

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel

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