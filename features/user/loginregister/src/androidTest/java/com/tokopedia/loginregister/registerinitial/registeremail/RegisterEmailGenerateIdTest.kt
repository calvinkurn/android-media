package com.tokopedia.loginregister.registerinitial.registeremail

import com.tokopedia.loginregister.common.ViewIdGenerator
import com.tokopedia.loginregister.registerinitial.RegisterEmailBase
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Test

@UiTest
class RegisterEmailGenerateIdTest : RegisterEmailBase() {

    @Test
    fun generate_view_id_file() {
        runTest {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "register_email.csv")
        }
    }

}
