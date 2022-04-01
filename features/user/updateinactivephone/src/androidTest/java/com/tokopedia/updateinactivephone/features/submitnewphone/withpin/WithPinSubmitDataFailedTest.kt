package com.tokopedia.updateinactivephone.features.submitnewphone.withpin

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseSubmitDataTest
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkErrorMessageOnInputPhone
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.clickOnButtonSubmit
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.setPhoneNumberText
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class WithPinSubmitDataFailedTest : BaseSubmitDataTest() {

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            submitExpeditedInactivePhoneUseCaseStub.response = submitExpeditedInactivePhoneDataModel
            phoneValidationUseCaseStub.response = phoneValidationDataModel
            verifyNewPhoneUseCaseStub.response = verifyNewPhoneDataModel
        }
    }

    @Test
    fun input_empty_phone_number() {
        val phone = ""

        runTest(source = InactivePhoneConstant.EXPEDITED) {
            setPhoneNumberText(phone)
            clickOnButtonSubmit()
            checkErrorMessageOnInputPhone("Wajib diisi.")
        }
    }

    @Test
    fun input_phone_number_less_then_9() {
        val phone = "084444"

        runTest(source = InactivePhoneConstant.EXPEDITED) {
            setPhoneNumberText(phone)
            clickOnButtonSubmit()
            checkErrorMessageOnInputPhone("Min. 9 digit.")
        }
    }
}