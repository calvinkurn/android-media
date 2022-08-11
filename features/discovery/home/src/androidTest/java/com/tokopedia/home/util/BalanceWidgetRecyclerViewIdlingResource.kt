package com.tokopedia.home.util

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceAdapter
import com.tokopedia.home.util.HomeInstrumentationTestHelper.disableHomeAnimation

private const val NAME = "Recycler view has item idling resource"

internal class BalanceWidgetRecyclerViewIdlingResource(
    private val recyclerView: RecyclerView?,
    private val name: String? = NAME,
    private val limitCountToIdle: Int = 0
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val recyclerView = recyclerView ?: return true
        recyclerView.disableHomeAnimation()

        val isIdle = (recyclerView.adapter as? BalanceAdapter).isStillLoading()
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    private fun BalanceAdapter?.isStillLoading(): Boolean {
        return this != null && itemCount >= limitCountToIdle && (
                this.currentList.find { it is BalanceDrawerItemModel && it.state == BalanceDrawerItemModel.STATE_LOADING } != null)
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}