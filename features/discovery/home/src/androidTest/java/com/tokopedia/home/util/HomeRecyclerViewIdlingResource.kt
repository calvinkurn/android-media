package com.tokopedia.home.util

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.util.HomeInstrumentationTestHelper.disableHomeAnimation
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel

private const val NAME = "Recycler view has item idling resource"

internal class HomeRecyclerViewIdlingResource(
    private val recyclerView: RecyclerView?,
    private val name: String? = NAME,
    private val limitCountToIdle: Int = 0,
    private val fullLayout: Boolean = false
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val recyclerView = recyclerView ?: return true
        recyclerView.disableHomeAnimation()

        val isIdle = (recyclerView.adapter as? HomeRecycleAdapter).isNonCacheData()
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    private fun HomeRecycleAdapter?.isNonCacheData(): Boolean {
        if (!fullLayout) {
            return this != null && itemCount >= limitCountToIdle && (
                    this.currentList.find { it is HomeRecommendationFeedDataModel } != null ||
                            this.currentList.find { it is HomeRetryModel } != null
                    )
        } else {
            return this != null && itemCount >= limitCountToIdle && (
                    listContainsExternalModel(this.currentList) ||
                            this.currentList.find { it is HomeRetryModel } != null
                    )
        }
    }

    private fun listContainsExternalModel(currentList: List<Any>): Boolean {
        return currentList.find { it is HomeRecommendationFeedDataModel } != null &&
                currentList.find { it is ReminderWidgetModel } != null &&
                currentList.find { it is RechargeBUWidgetDataModel } != null
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}