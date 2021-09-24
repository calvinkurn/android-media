package com.tokopedia.updateinactivephone.features.onboarding.withpin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.PARAM_USER_DATA
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker

class InactivePhoneWithPinActivity: BaseSimpleActivity() {

    private var tracker = InactivePhoneWithPinTracker()

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent?.extras)
        }

        return InactivePhoneOnboardingPinFragment.instance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        updateTitle(getString(R.string.text_title))
        toolbar.setTitleTextAppearance(this, R.style.BoldToolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            elevation = 0f
        }
    }

    override fun onBackPressed() {
        tracker.clickOnButtonBackOnboarding()
        super.onBackPressed()
    }

    companion object {
        fun createIntent(context: Context, inactivePhoneUserDataModel: InactivePhoneUserDataModel): Intent {
            return Intent(context, InactivePhoneWithPinActivity::class.java).apply {
                putExtra(PARAM_USER_DATA, inactivePhoneUserDataModel)
            }
        }
    }
}