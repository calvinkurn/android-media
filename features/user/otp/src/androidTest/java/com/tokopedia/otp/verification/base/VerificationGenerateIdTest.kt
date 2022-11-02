package com.tokopedia.otp.verification.base

import androidx.test.runner.AndroidJUnit4
import com.tokopedia.otp.common.ViewIdGenerator
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Test
import org.junit.runner.RunWith


@UiTest
@RunWith(AndroidJUnit4::class)
class VerificationGenerateIdTest : VerificationTest() {
    @Test
    fun generate_view_id_file() {
        runTest {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "verification.csv")
        }
    }
}
