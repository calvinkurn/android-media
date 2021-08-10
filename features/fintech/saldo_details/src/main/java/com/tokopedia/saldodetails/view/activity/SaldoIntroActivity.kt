package com.tokopedia.saldodetails.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.saldodetails.view.fragment.SaldoIntroFragment

class SaldoIntroActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return SaldoIntroFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCloseButton()
    }

    private fun setCloseButton() {
        if (supportActionBar == null) {
            return
        }
        supportActionBar!!.setHomeAsUpIndicator(ContextCompat.getDrawable(this,
                com.tokopedia.abstraction.R.drawable.ic_close_default))
    }

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, SaldoIntroActivity::class.java)
        }
    }
}

