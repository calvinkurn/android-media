package com.tokopedia.updateinactivephone.features.submitnewphone.withpin

import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseSubmitDataTest
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkErrorMessageOnInputPhone
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.clickOnButtonSubmit
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.setPhoneNumberText
import org.junit.Test

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
            checkTracker()
        }
    }

    @Test
    fun input_phone_number_less_then_9() {
        val phone = "084444"

        runTest(source = InactivePhoneConstant.EXPEDITED) {
            setPhoneNumberText(phone)
            clickOnButtonSubmit()
            checkErrorMessageOnInputPhone("Min. 9 digit.")
            checkTracker()
        }
    }
}