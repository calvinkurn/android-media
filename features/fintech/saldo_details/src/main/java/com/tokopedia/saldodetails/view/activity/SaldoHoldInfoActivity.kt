package com.tokopedia.saldodetails.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.saldodetails.view.fragment.SaldoHoldInfoFragment

class SaldoHoldInfoActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return SaldoHoldInfoFragment.createInstance()
    }

    fun newInstance(context: Context): Intent {
        return Intent(context, SaldoHoldInfoFragment::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("Informasi Saldo Tertahan")
    }

}