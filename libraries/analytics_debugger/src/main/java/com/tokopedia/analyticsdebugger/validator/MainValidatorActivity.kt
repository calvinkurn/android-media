package com.tokopedia.analyticsdebugger.validator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.validator.core.GtmLogUi
import com.tokopedia.analyticsdebugger.validator.detail.ValidatorDetailFragment
import com.tokopedia.analyticsdebugger.validator.list.ValidatorListFragment
import com.tokopedia.analyticsdebugger.validator.main.MainValidatorFragment

class MainValidatorActivity : AppCompatActivity(), MainValidatorFragment.Listener, ValidatorListFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_validator)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia Client Analytics Validator"

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, ValidatorListFragment.newInstance())
                .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is MainValidatorFragment) {
            fragment.setCallback(this)
        } else if (fragment is ValidatorListFragment) {
            fragment.setListener(this)
        }
    }

    override fun goDetail(expected: String, actual: List<GtmLogUi>) {
        supportFragmentManager.beginTransaction()
                .addToBackStack("detail")
                .setCustomAnimations(R.anim.slide_left_in, 0, R.anim.pop_delay_in, R.anim.slide_right_out)
                .replace(R.id.container, ValidatorDetailFragment.newIntent(expected, actual))
                .commit()
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MainValidatorActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    override fun runTest(filepath: String) {
        supportFragmentManager.beginTransaction()
                .addToBackStack("runner")
                .replace(R.id.container, MainValidatorFragment.newInstance(filepath))
                .commit()
    }
}