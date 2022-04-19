package com.tokopedia.updateinactivephone.stub.features.submitnewphone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.submitnewphone.InactivePhoneSubmitDataActivity
import com.tokopedia.updateinactivephone.stub.features.submitnewphone.regular.InactivePhoneDataUploadFragmentStub
import com.tokopedia.updateinactivephone.stub.features.submitnewphone.withpin.InactivePhoneSubmitNewPhoneFragmentStub
import com.tokopedia.updateinactivephone.stub.features.successpage.InactivePhoneSuccessPageActivityStub

class InactivePhoneSubmitDataActivityStub : InactivePhoneSubmitDataActivity() {

    override fun getTagFragment(): String = TAG

    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent?.extras)
        }

        val isExpeditedFlow = intent?.getStringExtra(InactivePhoneConstant.KEY_SOURCE).toString() == InactivePhoneConstant.EXPEDITED
        return if (isExpeditedFlow) {
            InactivePhoneSubmitNewPhoneFragmentStub.instance(bundle)
        } else {
            InactivePhoneDataUploadFragmentStub.instance(bundle)
        }
    }

    companion object {
        val TAG = InactivePhoneSubmitDataActivityStub::class.java.name

        fun createIntent(context: Context, source: String, inactivePhoneUserDataModel: InactivePhoneUserDataModel?): Intent {
            return Intent(context, InactivePhoneSubmitDataActivityStub::class.java).apply {
                putExtras(Bundle().apply {
                    putString(InactivePhoneConstant.KEY_SOURCE, source)
                    putParcelable(InactivePhoneConstant.PARAM_USER_DATA, inactivePhoneUserDataModel)
                })
            }
        }

    }

}