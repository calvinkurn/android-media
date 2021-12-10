package com.tokopedia.updateinactivephone.features.submitnewphone

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
import com.tokopedia.updateinactivephone.features.submitnewphone.regular.InactivePhoneDataUploadFragment
import com.tokopedia.updateinactivephone.features.submitnewphone.withpin.InactivePhoneSubmitNewPhoneFragment

open class InactivePhoneSubmitDataActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent?.extras)
        }

        val isExpeditedFlow = intent?.getStringExtra(KEY_SOURCE).toString() == InactivePhoneConstant.EXPEDITED
        return if (isExpeditedFlow) {
            InactivePhoneSubmitNewPhoneFragment.create(bundle)
        } else {
            InactivePhoneDataUploadFragment.create(bundle)
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        updateTitle(getString(R.string.text_title))
        toolbar.setTitleTextAppearance(this, R.style.BoldToolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            elevation = 0f
        }
    }

    companion object {
        fun getIntent(context: Context, source: String, inactivePhoneUserDataModel: InactivePhoneUserDataModel?): Intent {
            return Intent(context, InactivePhoneSubmitDataActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putString(KEY_SOURCE, source)
                    putParcelable(InactivePhoneConstant.PARAM_USER_DATA, inactivePhoneUserDataModel)
                })
            }
        }
    }
}
