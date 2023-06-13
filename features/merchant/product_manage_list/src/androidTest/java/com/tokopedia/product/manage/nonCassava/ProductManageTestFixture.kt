package com.tokopedia.product.manage.nonCassava

import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.product.manage.mock.ProductManageMockResponseConfig
import com.tokopedia.product.manage.stub.feature.list.view.activity.ProductManageActivityStub
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class ProductManageTestFixture {

    @get:Rule
    var activityRule: ActivityTestRule<ProductManageActivityStub> = IntentsTestRule(
        ProductManageActivityStub::class.java,
        true,
        false
    )


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        setupMockResponse()
        startActivity()
    }


    private fun startActivity() {
        activityRule.launchActivity(getIntent())
    }

    private fun getIntent(): Intent {
        return ProductManageActivityStub.createIntent(context)
    }

    private fun setupMockResponse() {
        setupGraphqlMockResponse(ProductManageMockResponseConfig())
    }


}
