package com.tokopedia.manageaddress

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.EXTRA_SELECTED_ADDRESS_DATA
import com.tokopedia.manageaddress.di.DaggerTestAppComponent
import com.tokopedia.manageaddress.di.FakeAppModule
import com.tokopedia.manageaddress.di.FakeGraphqlUseCase
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_IS_LOCALIZATION
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.UnknownHostException

@LargeTest
@RunWith(AndroidJUnit4::class)
class ManageAddressTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(ManageAddressActivity::class.java, false, false)

    lateinit var fakeGql: FakeGraphqlUseCase

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val component = DaggerTestAppComponent.builder().fakeAppModule(FakeAppModule(ctx)).build()
        fakeGql = component.fakeGraphql() as FakeGraphqlUseCase
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
    }

    @Test
    fun fromLocalizationWhenSelectingAddressThenHavingExpectedResultIntent() {
        val i = Intent().apply { putExtra(EXTRA_IS_LOCALIZATION, true) }
        manageAddress {
            launchWithParam(mActivityTestRule, i)
            selectItemAt(2)
            selectAddress()
        } submit {
            hasResultIntent(mActivityTestRule, RESULT_OK, EXTRA_SELECTED_ADDRESS_DATA)
        }
    }

    @Test
    fun fromCheckoutWhenSelectingAddressThenHavingExpectedResultIntent() {
        val i = Intent().apply { putExtra(EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true) }
        manageAddress {
            launchWithParam(mActivityTestRule, i)
            selectItemAt(2)
            selectAddress()
        } submit {
            hasResultIntent(mActivityTestRule, RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS, EXTRA_SELECTED_ADDRESS_DATA)
        }
    }

    @Test
    fun givenUnknownHostExceptionResponseShowGlobalError() {
        fakeGql.returnException(UnknownHostException("gql.tokopedia.com"))
        val i = Intent()
        manageAddress {
            launchWithParam(mActivityTestRule, i)
        } submit {
            assertGlobalErrorNoInternetConnectionShown()
        }
    }

    @Test
    fun interactionTest() {
        val i = Intent().apply { putExtra(EXTRA_IS_LOCALIZATION, true) }
        manageAddress {
            launchWithParam(mActivityTestRule, i)
        }
        for (pos in fakeGql.data.keroAddressCorner.data.indices) {
            manageAddress {
                selectItemAt(pos)
            } submit {
                assertAddressCheckedAtPosition(pos)
            }
            if (pos == 0) {
                onView(RecyclerViewMatcher(R.id.address_list)
                        .atPositionOnView(pos, R.id.btn_secondary))
                        .check(matches(not(isDisplayed())))
            } else {
                onView(RecyclerViewMatcher(R.id.address_list)
                        .atPositionOnView(pos, R.id.btn_secondary))
                        .check(matches(isDisplayed())).perform(click())
                pressBack()
            }
        }
    }
}