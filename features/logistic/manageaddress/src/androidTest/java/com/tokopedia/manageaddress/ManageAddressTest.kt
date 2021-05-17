package com.tokopedia.manageaddress

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.EXTRA_SELECTED_ADDRESS_DATA
import com.tokopedia.manageaddress.di.DaggerTestAppComponent
import com.tokopedia.manageaddress.di.FakeAppModule
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_IS_LOCALIZATION
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ManageAddressTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(ManageAddressActivity::class.java, false, false)

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val component = DaggerTestAppComponent.builder().fakeAppModule(FakeAppModule(ctx)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        mActivityTestRule.launchActivity(Intent().apply { putExtra(EXTRA_IS_LOCALIZATION, true) })
    }

    @Test
    fun fromLocalizationWhenSelectingAddressThenHavingExpectedResultIntent() {
        manageAddress {
            selectItemAt(2)
            selectAddress()
        } submit {
            hasResultIntent(mActivityTestRule, RESULT_OK, EXTRA_SELECTED_ADDRESS_DATA)
        }
    }
}