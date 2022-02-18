package com.tokopedia.product.manage.cassava.testcase

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.cassava.robot.ProductManageRobot
import com.tokopedia.product.manage.cassava.robot.actionTest
import com.tokopedia.product.manage.feature.cashback.presentation.activity.ProductManageSetCashbackActivity
import com.tokopedia.product.manage.feature.list.view.activity.ProductManageActivity
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.mock.ProductManageMockResponseConfig
import com.tokopedia.product.manage.stub.common.GraphqlRepositoryStub
import com.tokopedia.product.manage.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.product.manage.stub.feature.ProductManageStubInstance
import com.tokopedia.product.manage.stub.feature.list.domain.usecase.GetProductManageAccessUseCaseStub
import com.tokopedia.product.manage.stub.feature.list.view.activity.ProductManageActivityStub
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class ProductManageTrackerValidationTestFixture {

    @get:Rule
    var activityRule: ActivityTestRule<ProductManageActivityStub> = IntentsTestRule(
        ProductManageActivityStub::class.java,
        true,
        false
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        setupMockResponse()
        startActivity()
    }

    protected fun startActivity() {
        activityRule.launchActivity(getIntent())
    }

    protected fun validate(fileName: String) {
        waitForTrackerSent()
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    private fun getIntent(): Intent {
        return ProductManageActivityStub.createIntent(context)
    }

    private fun setupMockResponse() {
        setupGraphqlMockResponse(ProductManageMockResponseConfig())
    }

    private fun waitForTrackerSent() {
        Thread.sleep(2000)
    }

}