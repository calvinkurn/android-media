package com.tokopedia.universal_sharing

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common.di.AppStubModule
import com.tokopedia.common.di.DaggerAppStubComponent
import com.tokopedia.common.stub.UniversalShareBottomSheetStub
import com.tokopedia.common.view.UniversalShareTestActivity
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UniversalShareBottomSheetTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UniversalShareTestActivity::class.java, false, false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    fun before() {
        val fakeBaseAppComponent = DaggerAppStubComponent.builder().appStubModule(AppStubModule(applicationContext)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(fakeBaseAppComponent)
    }

    @After
    fun after() {
        Log.d("UniversalBottomSheetTest", "test after")
    }

    @Test
    fun checkPDPBottomSheet() {
        runTest(UniversalShareBottomSheetStub().apply {
            enableAffiliateCommission(AffiliateInput())
        }) {
            Thread.sleep(5000)
        }
    }

    private fun runTest(bottomSheet: UniversalShareBottomSheet, block: () -> Unit) {
        activityTestRule.launchActivity(Intent())
        activityTestRule.activity.getShareFragment().showUniversalBottomSheet(bottomSheet)
        block.invoke()
    }
}
