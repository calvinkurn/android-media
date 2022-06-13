package com.tokopedia.updateinactivephone.features.successpage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.KEY_SOURCE
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.successpage.regular.InactivePhoneRegularSuccessFragment
import com.tokopedia.updateinactivephone.features.successpage.withpin.InactivePhoneWithPinSuccessFragment

open class InactivePhoneSuccessPageActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent?.extras)
        }

        val isExpeditedFlow = intent?.getStringExtra(KEY_SOURCE).toString() == InactivePhoneConstant.EXPEDITED
        return if (isExpeditedFlow) {
            InactivePhoneWithPinSuccessFragment.instance(bundle)
        } else {
            InactivePhoneRegularSuccessFragment.instance(bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        updateTitle("")
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            elevation = 0f
        }
    }

    companion object {
        fun createIntent(context: Context, source: String, inactivePhoneUserDataModel: InactivePhoneUserDataModel?): Intent {
            return Intent(context, InactivePhoneSuccessPageActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putString(KEY_SOURCE, source)
                    putParcelable(InactivePhoneConstant.PARAM_USER_DATA, inactivePhoneUserDataModel)
                })
            }
        }
    }
}
