package com.tokopedia.updateinactivephone.stub.features.successpage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.successpage.InactivePhoneSuccessPageActivity
import com.tokopedia.updateinactivephone.stub.features.successpage.regular.InactivePhoneRegularSuccessFragmentStub
import com.tokopedia.updateinactivephone.stub.features.successpage.withpin.InactivePhoneWithPinSuccessFragmentStub

class InactivePhoneSuccessPageActivityStub : InactivePhoneSuccessPageActivity() {

    override fun getTagFragment(): String = TAG
    override fun inflateFragment() {}

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent?.extras)
        }

        val isExpeditedFlow = intent?.getStringExtra(InactivePhoneConstant.KEY_SOURCE).toString() == InactivePhoneConstant.EXPEDITED
        return if (isExpeditedFlow) {
            InactivePhoneWithPinSuccessFragmentStub.instance(bundle)
        } else {
            InactivePhoneRegularSuccessFragmentStub.instance(bundle)
        }
    }

    open fun setUpFragment() {
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, newFragment, TAG)
            .commit()
    }

    companion object {
        val TAG = InactivePhoneSuccessPageActivityStub::class.java.name

        fun createIntent(context: Context, source: String, inactivePhoneUserDataModel: InactivePhoneUserDataModel?): Intent {
            return Intent(context, InactivePhoneSuccessPageActivityStub::class.java).apply {
                putExtras(Bundle().apply {
                    putString(InactivePhoneConstant.KEY_SOURCE, source)
                    putParcelable(InactivePhoneConstant.PARAM_USER_DATA, inactivePhoneUserDataModel)
                })
            }
        }
    }
}