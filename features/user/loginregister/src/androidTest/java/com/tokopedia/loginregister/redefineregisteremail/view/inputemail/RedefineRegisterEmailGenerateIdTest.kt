package com.tokopedia.loginregister.redefineregisteremail.view.inputemail

import android.os.Bundle
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.ViewIdGenerator
import com.tokopedia.loginregister.redefineregisteremail.stub.common.launchFragment
import com.tokopedia.loginregister.redefineregisteremail.view.RedefineRegisterEmailActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class RedefineRegisterEmailGenerateIdTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java,
        false,
        false
    )

    @Test
    fun generate_view_id_file() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        val rootView = findRootView(activityTestRule.activity)
        ViewIdGenerator.createViewIdFile(rootView, "redefine_input_email.csv")
    }

}
