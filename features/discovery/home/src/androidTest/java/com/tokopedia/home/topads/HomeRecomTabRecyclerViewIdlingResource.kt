package com.tokopedia.home.topads

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoading
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.util.HomeInstrumentationTestHelper.disableHomeAnimation

private const val NAME = "Recycler view has item idling resource"

internal class HomeRecomTabRecyclerViewIdlingResource(
    private val recyclerView: RecyclerView?,
    private val name: String? = NAME,
    private val limitCountToIdle: Int = 0
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val recyclerView = recyclerView ?: return true
        recyclerView.disableHomeAnimation()

        val isIdle = (recyclerView.adapter as? HomeRecommendationAdapter).isLoading()
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    private fun HomeRecommendationAdapter?.isLoading(): Boolean {
        return this != null && itemCount >= limitCountToIdle && (
                this.currentList.find { it is HomeRecommendationLoading } != null)
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}
