package com.tokopedia.pdpsimulation.activteGopay.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.pdpsimulation.activteGopay.presentation.bottomsheet.GopayLinkBenefitBottomSheet

class ActivateGopayLinkActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
         return  null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowCustomEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = ""
        showBottomSheet()
    }

    private fun showBottomSheet() {
        GopayLinkBenefitBottomSheet().showBottomSheet(supportFragmentManager).setOnDismissListener {
            finish()
        }
    }
}