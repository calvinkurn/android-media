package com.tokopedia.universal_sharing

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common.di.AppStubModule
import com.tokopedia.common.di.DaggerAppStubComponent
import com.tokopedia.common.stub.GraphqlRepositoryStub
import com.tokopedia.common.stub.UniversalShareBottomSheetStub
import com.tokopedia.common.view.UniversalShareTestActivity
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.test.application.matcher.hasViewHolderOf
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ChipViewHolder
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.ChipProperties
import com.tokopedia.universal_sharing.view.model.LinkProperties
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

    @Test
    fun `bottom-sheet_able_click_the_chip_and_get_the_properties_on_listener`() {
        val chips = chipComponentList()
        var onTitleChipPropertiesSelected = ""

        runTest(
            UniversalShareBottomSheetStub().apply {
                setChipList(chips)
                onChipChangedListener {
                    onTitleChipPropertiesSelected = it.title
                }
            }
        ) {
            Espresso.onView(withId(R.id.lst_chip)).check(matches(isDisplayed()))
            Espresso.onView(RecyclerViewMatcher(R.id.lst_chip).atPosition(0)).perform(ViewActions.click())
            assert(onTitleChipPropertiesSelected.isNotEmpty())
        }
    }

    @Test
    fun `bottom-sheet_show_chip_component_with_default_selection_state`() {
        val chips = chipComponentList()
            .toMutableList()
            .also {
                it.last().isSelected = true
            }

        runTest(
            UniversalShareBottomSheetStub().apply {
                setChipList(chips)
            }
        ) {
            Espresso.onView(RecyclerViewMatcher(R.id.lst_chip).atPosition(0))
                .check(matches(hasDescendant(withId(R.id.view_chip))))

            Espresso.onView(withId(R.id.lst_chip)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun `bottom-sheet_show_chip_component_with_first_item_as_default_selection_state`() {
        runTest(
            UniversalShareBottomSheetStub().apply {
                setChipList(chipComponentList())
            }
        ) {
            Espresso.onView(withId(R.id.lst_chip)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.lst_chip)).check(matches(hasViewHolderOf(ChipViewHolder::class.java)))
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

    private fun chipComponentList() = listOf(
        ChipProperties(
            id = 1,
            title = "Foo",
            properties = LinkProperties()
        ),
        ChipProperties(
            id = 2,
            title = "Bar",
            properties = LinkProperties()
        ),
    )
}
