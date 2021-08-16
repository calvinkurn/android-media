package com.tokopedia.officialstore.util

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel

private const val NAME = "Recycler view has item idling resource"

internal class OSRecyclerViewIdlingResource(
    private val recyclerView: RecyclerView?,
    private val name: String? = NAME,
    private val limitCountToIdle: Int = 2
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val recyclerView = recyclerView ?: return true
        val isIdle = (recyclerView.adapter as? OfficialHomeAdapter).isContainsProductRecom()
        if (isIdle) {
            Log.d("DevaraFikry", "Id idle now")
            resourceCallback?.onTransitionToIdle()
        }


        return isIdle
    }

    private fun OfficialHomeAdapter?.isContainsProductRecom(): Boolean {
        return this != null && itemCount >= limitCountToIdle && (
                this.currentList.find { it is ProductRecommendationDataModel } != null
                )
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}