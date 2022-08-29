package com.tokopedia.updateinactivephone.stub.features.inputoldphonenumber

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.activity.InputOldPhoneNumberActivity

class InputOldPhoneNumberActivityStub : InputOldPhoneNumberActivity() {

    override fun getTagFragment(): String = TAG
    override fun getComponent(): InactivePhoneComponent =
        BaseInactivePhoneTest.inactivePhoneComponent!!

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        return InputOldPhoneNumberFragmentStub.newInstance(bundle)
    }

    companion object {
        val TAG: String = InputOldPhoneNumberActivityStub::class.java.simpleName

        fun createIntent(context: Context): Intent {
            return Intent(context, InputOldPhoneNumberActivityStub::class.java)
        }
    }

}