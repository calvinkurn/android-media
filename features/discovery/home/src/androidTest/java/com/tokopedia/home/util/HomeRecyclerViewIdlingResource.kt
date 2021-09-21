package com.tokopedia.home.util

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel

private const val NAME = "Recycler view has item idling resource"

internal class HomeRecyclerViewIdlingResource(
    private val recyclerView: RecyclerView?,
    private val name: String? = NAME,
    private val limitCountToIdle: Int = 0
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val recyclerView = recyclerView ?: return true

        val isIdle = (recyclerView.adapter as? HomeRecycleAdapter).isNonCacheData()
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    private fun HomeRecycleAdapter?.isNonCacheData(): Boolean {
        return this != null && itemCount >= limitCountToIdle && (
                this.currentList.find { it is HomeRecommendationFeedDataModel } != null ||
                        this.currentList.find { it is HomeRetryModel } != null
                )
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}