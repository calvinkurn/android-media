package com.tokopedia.analyticsdebugger.validator.execution

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.validator.detail.ValidatorDetailFragment

class MainValidatorActivity : AppCompatActivity(), MainValidatorFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_validator)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia Client Analytics Validator"

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainValidatorFragment.newInstance())
                .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is MainValidatorFragment) {
            fragment.setCallback(this)
        }
    }

    override fun goDetail(expected: String, actual: String) {
        supportFragmentManager.beginTransaction()
                .addToBackStack("detail")
                .setCustomAnimations(R.anim.anim_slide_up_in, R.anim.anim_slide_out_up)
                .replace(R.id.container, ValidatorDetailFragment.newIntent(expected, actual))
                .commit()
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MainValidatorActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}