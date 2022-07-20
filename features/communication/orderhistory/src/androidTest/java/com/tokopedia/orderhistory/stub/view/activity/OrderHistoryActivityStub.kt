package com.tokopedia.orderhistory.stub.view.activity

import com.tokopedia.orderhistory.di.OrderHistoryComponent
import com.tokopedia.orderhistory.stub.di.OrderHistoryComponentStub
import com.tokopedia.orderhistory.view.activity.OrderHistoryActivity

class OrderHistoryActivityStub : OrderHistoryActivity() {

    lateinit var orderHistoryComponentStub: OrderHistoryComponentStub

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    override fun getTagFragment(): String {
        return TAG
    }

    override fun initializeOrderHistoryComponent(): OrderHistoryComponent {
        return orderHistoryComponentStub
    }

    fun setupTestFragment(orderHistoryComponentStub: OrderHistoryComponentStub) {
        this.orderHistoryComponentStub = orderHistoryComponentStub
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment!!, TAG)
                .commit()
    }

    companion object {
        const val TAG = "order_history-tag"
    }
}