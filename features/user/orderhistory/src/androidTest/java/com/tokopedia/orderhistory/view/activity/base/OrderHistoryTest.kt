package com.tokopedia.orderhistory.view.activity.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.orderhistory.AndroidFileUtil
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.di.OrderHistoryContextModule
import com.tokopedia.orderhistory.idling.FragmentTransactionIdle
import com.tokopedia.orderhistory.stub.di.DaggerOrderHistoryComponentStub
import com.tokopedia.orderhistory.stub.di.OrderHistoryComponentStub
import com.tokopedia.orderhistory.stub.usecase.GetProductOrderHistoryUseCaseStub
import com.tokopedia.orderhistory.stub.view.activity.OrderHistoryActivityStub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class OrderHistoryTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            OrderHistoryActivityStub::class.java, false, false
    )

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    protected open lateinit var activity: OrderHistoryActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    protected val exShopId = "1479278"

    @Inject
    protected lateinit var getProductOrderHistoryUseCase: GetProductOrderHistoryUseCaseStub

    protected var chatHistoryProductResponse: ChatHistoryProductResponse = AndroidFileUtil.parse(
            "success_get_product_order_history.json",
            ChatHistoryProductResponse::class.java
    )

    protected lateinit var orderHistoryComponentStub: OrderHistoryComponentStub

    @ExperimentalCoroutinesApi
    @Before
    open fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        val baseComponent = (applicationContext as BaseMainApplication).baseAppComponent
        orderHistoryComponentStub = DaggerOrderHistoryComponentStub.builder()
                .baseAppComponent(baseComponent)
                .orderHistoryContextModule(OrderHistoryContextModule(context))
                .build()
        orderHistoryComponentStub.inject(this)
    }

    protected fun setupOrderHistoryActivity() {
        val intent = Intent().apply {
            data = Uri.parse("tokopedia://product-order-history/$exShopId")
        }
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        fragmentTransactionIdling = FragmentTransactionIdle(
                activity.supportFragmentManager,
                OrderHistoryActivityStub.TAG
        )
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(orderHistoryComponentStub)
        waitForFragmentResumed()
    }

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    protected fun waitForIt(millis: Long) {
        Thread.sleep(millis)
    }
}