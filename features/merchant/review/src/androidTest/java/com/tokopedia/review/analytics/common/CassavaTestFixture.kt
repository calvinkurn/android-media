package com.tokopedia.review.analytics.common

import android.app.Application
import android.content.Context
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.config.GlobalConfig
import com.tokopedia.review.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.review.stub.common.graphql.coroutines.domain.repository.GraphqlRepositoryStub
import com.tokopedia.user.session.UserSession
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class CassavaTestFixture {
    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected lateinit var graphqlRepositoryStub: GraphqlRepositoryStub
    protected lateinit var userSession: UserSession

    @Before
    open fun setup() {
        getGraphqlRepositoryStub()
        getUserSessionStub()
    }

    private fun getGraphqlRepositoryStub() {
        graphqlRepositoryStub = BaseAppComponentStubInstance.getBaseAppComponentStub(
            context.applicationContext as Application
        ).graphqlRepository() as GraphqlRepositoryStub
        graphqlRepositoryStub.clearMocks()
    }

    private fun getUserSessionStub() {
        userSession = BaseAppComponentStubInstance.getBaseAppComponentStub(
            context.applicationContext as Application
        ).userSession() as UserSession
    }

    protected fun setAppToSellerApp() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP
        GlobalConfig.DEBUG = true
    }
}