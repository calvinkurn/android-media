package com.tokopedia.updateinactivephone.stub.features.accountlist

import android.content.Context
import android.content.Intent
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import com.tokopedia.updateinactivephone.features.accountlist.InactivePhoneAccountListActivity

class InactivePhoneAccountListActivityStub : InactivePhoneAccountListActivity() {

    override fun getTagFragment(): String = TAG
    override fun inflateFragment() {}
    override fun getComponent(): InactivePhoneComponent = BaseInactivePhoneTest.inactivePhoneComponent!!

    companion object {
        val TAG = InactivePhoneAccountListActivityStub::class.java.name

        fun createIntent(context: Context): Intent {
            return Intent(context, InactivePhoneAccountListActivityStub::class.java)
        }
    }
}