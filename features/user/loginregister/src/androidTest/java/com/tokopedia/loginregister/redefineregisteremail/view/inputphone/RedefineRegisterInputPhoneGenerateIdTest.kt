package com.tokopedia.loginregister.redefineregisteremail.view.inputphone

import android.os.Bundle
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.ViewIdGenerator
import com.tokopedia.loginregister.redefineregisteremail.stub.common.launchFragment
import com.tokopedia.loginregister.redefineregisteremail.view.RedefineRegisterEmailActivity
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.param.RedefineParamUiModel
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class RedefineRegisterInputPhoneGenerateIdTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java,
        false,
        false
    )

    private lateinit var bundleMandatory: Bundle

    @Before
    fun setUp() {
        val paramMandatory = RedefineParamUiModel(isRequiredInputPhone = true)
        bundleMandatory = Bundle()
        bundleMandatory.putParcelable("parameter", paramMandatory)
    }

    @Test
    fun generate_view_id_file() {
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        val rootView = findRootView(activityTestRule.activity)
        ViewIdGenerator.createViewIdFile(rootView, "redefine_input_phone.csv")
    }

}
