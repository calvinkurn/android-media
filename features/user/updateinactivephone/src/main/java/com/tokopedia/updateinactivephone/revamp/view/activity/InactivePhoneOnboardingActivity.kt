package com.tokopedia.updateinactivephone.revamp.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.common.replaceFragment
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneOnboardingFragment

class InactivePhoneOnboardingActivity : BaseSimpleActivity(), FragmentTransactionInterface {

    override fun getNewFragment(): Fragment? {
        return InactivePhoneOnboardingFragment()
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount < 1) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }

    }

    override fun replace(fragment: Fragment) {
        replaceFragment(parentViewResourceID, fragment)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, InactivePhoneOnboardingActivity::class.java)
        }
    }
}
