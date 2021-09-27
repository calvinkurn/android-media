package com.tokopedia.updateinactivephone.features.submitnewphone.withpin

import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseSubmitDataTest
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkSubmitDataPageDisplayed
import org.junit.Test

class WithPinSubmitDataGeneralTest: BaseSubmitDataTest() {

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            submitDataUseCaseStub.response = inactivePhoneSubmitDataModel
            submitExpeditedInactivePhoneUseCaseStub.response = submitExpeditedInactivePhoneDataModel
        }
    }

    @Test
    fun show_submit_data_with_pin_page() {
        runTest(source = InactivePhoneConstant.EXPEDITED) {
            checkSubmitDataPageDisplayed()
        }
    }
}