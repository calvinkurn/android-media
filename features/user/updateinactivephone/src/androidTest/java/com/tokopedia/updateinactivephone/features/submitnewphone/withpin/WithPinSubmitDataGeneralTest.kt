package com.tokopedia.updateinactivephone.features.submitnewphone.withpin

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.viewaction.simulateOnBackPressed
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseSubmitDataTest
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkPopupIsDisplayed
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkSubmitDataPageDisplayed
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.clickOnButtonExitPopup
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.clickOnButtonLanjutVerifikasi
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.clickOnButtonSubmit
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.setPhoneNumberText
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class WithPinSubmitDataGeneralTest: BaseSubmitDataTest() {

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            submitExpeditedInactivePhoneUseCaseStub.response = submitExpeditedInactivePhoneDataModel
            phoneValidationUseCaseStub.response = phoneValidationDataModel
            verifyNewPhoneUseCaseStub.response = verifyNewPhoneDataModel
        }
    }

    @Test
    fun show_submit_data_with_pin_page() {
        runTest(source = InactivePhoneConstant.EXPEDITED) {
            checkSubmitDataPageDisplayed()
        }
    }

    @Test
    fun input_valid_phone_number() {
        val phone = "084444123123"

        runTest(source = InactivePhoneConstant.EXPEDITED) {
            setPhoneNumberText(phone)
        }
    }

    @Test
    fun submit_new_phone() {
        val phone = "084444123123"

        runTest(source = InactivePhoneConstant.EXPEDITED) {
            setPhoneNumberText(phone)
            clickOnButtonSubmit()
        }
    }

    @Test
    fun open_popup() {
        runTest {
            simulateOnBackPressed()
            checkPopupIsDisplayed()
        }
    }

    @Test
    fun on_click_exit_popup() {
        runTest {
            simulateOnBackPressed()
            clickOnButtonExitPopup()
        }
    }

    @Test
    fun on_click_lanjut_verifikasi() {
        runTest {
            simulateOnBackPressed()
            clickOnButtonLanjutVerifikasi()
        }
    }
}