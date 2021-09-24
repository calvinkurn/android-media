package com.tokopedia.updateinactivephone.stub.features.successpage

import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.successpage.InactivePhoneSuccessPageActivity

class InactivePhoneSuccessPageActivityStub : InactivePhoneSuccessPageActivity() {

    var fakeInactivePhoneDataModel = InactivePhoneUserDataModel(
        email = "rivaldy.firmansyah+100@tokopedia.com",
        newPhoneNumber = "084444123123"
    )

    override fun getTagFragment(): String = TAG
    override fun inflateFragment() {}

    override fun initDescription() {
        inactivePhoneUserDataModel = fakeInactivePhoneDataModel
        super.initDescription()
    }

    companion object {
        val TAG = InactivePhoneSuccessPageActivityStub::class.java.name
    }

}