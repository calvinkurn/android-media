package com.tokopedia.updateinactivephone.stub.features.accountlist

import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.features.accountlist.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.stub.di.InactivePhoneComponentStubBuilder

class InactivePhoneAccountListActivityStub : InactivePhoneAccountListActivity() {

    override fun getTagFragment(): String = TAG
    override fun inflateFragment() {}

    override fun getComponent(): InactivePhoneComponent {
        return InactivePhoneComponentStubBuilder.getComponent(applicationContext, this)
    }

    companion object {
        val TAG = InactivePhoneAccountListActivityStub::class.java.name
    }

}