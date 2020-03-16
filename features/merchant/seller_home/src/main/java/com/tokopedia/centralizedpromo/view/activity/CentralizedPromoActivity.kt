package com.tokopedia.centralizedpromo.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment

class CentralizedPromoActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, CentralizedPromoActivity::class.java)
    }

    override fun getNewFragment(): Fragment = CentralizedPromoFragment.createInstance()
}