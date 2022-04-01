package com.tokopedia.buyerorderdetail.cassava

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.buyerorderdetail.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.buyerorderdetail.stub.common.graphql.coroutines.domain.repository.GraphqlRepositoryStub
import com.tokopedia.buyerorderdetail.stub.detail.presentation.activity.BuyerOrderDetailActivityStub
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailCoachMarkData
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class BuyerOrderDetailTrackerValidationTestFixture {
    @get:Rule
    var activityRule: IntentsTestRule<BuyerOrderDetailActivityStub> = IntentsTestRule(
        BuyerOrderDetailActivityStub::class.java,
        false,
        false
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    lateinit var graphqlRepositoryStub: GraphqlRepositoryStub

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        setVersionName()
        getGraphqlRepositoryStub()
        preventCoachMarkFromShowing()
    }

    private fun setVersionName() {
        GlobalConfig.VERSION_NAME = "3.142-test"
    }

    private fun getGraphqlRepositoryStub() {
        val applicationContext = ApplicationProvider.getApplicationContext<Application>()
        graphqlRepositoryStub = BaseAppComponentStubInstance.getBaseAppComponentStub(
            applicationContext
        ).graphqlRepository() as GraphqlRepositoryStub
        graphqlRepositoryStub.clearMocks()
    }

    private fun preventCoachMarkFromShowing() {
        CoachMarkPreference.setShown(context, BuyerOrderDetailCoachMarkData.DRIVER_TIPPING_INFO_COACHMARK_KEY, true)
    }
}