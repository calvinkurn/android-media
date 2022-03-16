package com.tokopedia.home_account.explicitprofile

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home_account.explicitprofile.fakes.ExplicitProfileInterceptor
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseExplicitProfileTest {

    @get:Rule
    var activityRule = IntentsTestRule(
        ExplicitProfileActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(ExplicitProfileInterceptor(context)),
            context
        )

        activityRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }
}