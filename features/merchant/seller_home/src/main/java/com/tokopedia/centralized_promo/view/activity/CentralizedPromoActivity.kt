package com.tokopedia.centralized_promo.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.centralized_promo.view.fragment.CentralizedPromoFragment
import com.tokopedia.sellerhome.R

class CentralizedPromoActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, CentralizedPromoActivity::class.java)
    }

    override fun getNewFragment(): Fragment = CentralizedPromoFragment.createInstance()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.apply {
            removeAllViews()
            title = getString(R.string.sh_centralized_promo_title)
        }
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            setHomeButtonEnabled(false)
        }
    }
}