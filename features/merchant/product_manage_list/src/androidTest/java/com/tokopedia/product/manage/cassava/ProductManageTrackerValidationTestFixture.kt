package com.tokopedia.product.manage.cassava

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.product.manage.stub.common.GraphqlRepositoryStub
import com.tokopedia.product.manage.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.product.manage.stub.feature.list.view.activity.ProductManageActivityStub
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class ProductManageTrackerValidationTestFixture {

    @get:Rule
    var activityRule: IntentsTestRule<ProductManageActivityStub> = IntentsTestRule(
        ProductManageActivityStub::class.java,
        false,
        false
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    lateinit var graphqlRepositoryStub: GraphqlRepositoryStub

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        getGraphqlRepositoryStub()
    }

    private fun getGraphqlRepositoryStub() {
        val applicationContext = ApplicationProvider.getApplicationContext<Application>()
        graphqlRepositoryStub = BaseAppComponentStubInstance.getBaseAppComponentStub(
            applicationContext
        ).graphqlRepository() as GraphqlRepositoryStub
        graphqlRepositoryStub.clearMocks()
    }

}