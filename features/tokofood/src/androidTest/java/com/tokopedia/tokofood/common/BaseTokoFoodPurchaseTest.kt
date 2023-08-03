package com.tokopedia.tokofood.common

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.UserSessionStub
import org.junit.Rule

class BaseTokoFoodPurchaseTest {

    @get:Rule
    var activityRule = IntentsTestRule(BaseTokofoodActivity::class.java)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var userSessionStub: UserSessionStub
    protected lateinit var graphqlRepositoryStub: GraphqlRepositoryStub

    protected val applicationContext: Context = ApplicationProvider.getApplicationContext()
    protected val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

}
