package com.tokopedia.manageaddress

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.EXTRA_SELECTED_ADDRESS_DATA
import com.tokopedia.manageaddress.di.*
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_IS_LOCALIZATION
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.UnknownHostException
import javax.inject.Inject

@LargeTest
@RunWith(AndroidJUnit4::class)
class ManageAddressTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(ManageAddressActivity::class.java, false, false)

    @Inject
    lateinit var fakeGql: FakeGraphqlUseCase

    @Inject
    lateinit var fakeRepo: FakeGraphqlRepository

    @Before
    fun setup() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
        stub.activityComponent.inject(this)
    }

    @Test
    fun fromLocalizationWhenSelectingAddress_ClickSearch_ThenHavingExpectedResultIntent() {
        val i = Intent().apply { putExtra(EXTRA_IS_LOCALIZATION, true) }
        manageAddress {
            launchWithParam(mActivityTestRule, i)
            onClickSearch(KEYWORD)
            selectItemAt(0)
            selectAddress()
        } submit {
            hasResultIntent(mActivityTestRule, RESULT_OK, EXTRA_SELECTED_ADDRESS_DATA)
        }
    }

    @Test
    fun fromCheckoutWhenSelectingAddress_ClickSearch_ThenHavingExpectedResultIntent() {
        val i = Intent().apply { putExtra(EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true) }
        manageAddress {
            launchWithParam(mActivityTestRule, i)
            onClickSearch(KEYWORD)
            selectItemAt(0)
            selectAddress()
        } submit {
            hasResultIntent(mActivityTestRule, RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS, EXTRA_SELECTED_ADDRESS_DATA)
        }
    }

    @Test
    fun shareAddress_positiveFlow() {
        manageAddress {
            val i = Intent()
            launchWithParam(mActivityTestRule, i)
            clickShareIconOnPosition(0)
            typeEmailThenSubmit("0812345678")
            clickAgreeButton()
        } submit {
            hasDisplayedText(R.string.success_share_address)
        }
    }

    @Test
    fun shareAddress_negativeFlow() {
        manageAddress {
            val i = Intent()
            launchWithParam(mActivityTestRule, i)
            clickShareIconOnPosition(0)
            typeEmailThenSubmit("0812345678")
            fakeRepo.shareAddressNegative = true
            clickAgreeButton()
        } submit {
            // Error toast is displayed and bottomsheet is dismissed
            hasDisplayedText("something was wrong")
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
                onView(
                    RecyclerViewMatcher(R.id.address_list)
                        .atPositionOnView(pos, R.id.btn_secondary)
                )
                    .check(matches(not(isDisplayed())))
            } else {
                onView(
                    RecyclerViewMatcher(R.id.address_list)
                        .atPositionOnView(pos, R.id.btn_secondary)
                )
                    .check(matches(isDisplayed()))
            }
        }
    }

    companion object {
        const val KEYWORD = "Hefdy"
    }
}
