package com.tokopedia.universal_sharing

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common.di.AppStubModule
import com.tokopedia.common.di.DaggerAppStubComponent
import com.tokopedia.common.stub.GraphqlRepositoryStub
import com.tokopedia.common.stub.UniversalShareBottomSheetStub
import com.tokopedia.common.view.UniversalShareTestActivity
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UniversalShareBottomSheetTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UniversalShareTestActivity::class.java,
        false,
        false
    )

    lateinit var repositoryStub: GraphqlRepositoryStub

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    fun before() {
        val fakeBaseAppComponent = DaggerAppStubComponent.builder().appStubModule(AppStubModule(applicationContext)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(fakeBaseAppComponent)
        repositoryStub = fakeBaseAppComponent.graphqlRepository() as GraphqlRepositoryStub
    }

    @After
    fun after() {
        setMockParam(GraphqlRepositoryStub.MockParam.NO_PARAM)
    }

    @Test
    fun `bottom-sheet_show_affiliate_commission`() {
        setMockParam(GraphqlRepositoryStub.MockParam.ELIGIBLE_COMMISSION)

        runTest(
            UniversalShareBottomSheetStub().apply {
                enableAffiliateCommission(anyInput())
            }
        ) {
            Espresso.onView(withId(R.id.affilate_commision)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun `bottom-sheet_hide_affiliate_commission_when_not_eligible`() {
        setMockParam(GraphqlRepositoryStub.MockParam.NOT_ELIGIBLE_COMMISSION)

        runTest(
            UniversalShareBottomSheetStub().apply {
                enableAffiliateCommission(anyInput())
            }
        ) {
            Espresso.onView(withId(R.id.affilate_commision)).check(matches(not(isDisplayed())))
        }
    }

    private fun runTest(bottomSheet: UniversalShareBottomSheetStub, block: () -> Unit) {
        activityTestRule.launchActivity(Intent())
        activityTestRule.activity.getShareFragment().showUniversalBottomSheet(bottomSheet)
        block.invoke()
        Thread.sleep(3000)
        bottomSheet.dismiss()
    }

    private fun anyInput(): AffiliateInput = AffiliateInput()

    private fun setMockParam(param: GraphqlRepositoryStub.MockParam) {
        repositoryStub.mockParam = param
    }
}
