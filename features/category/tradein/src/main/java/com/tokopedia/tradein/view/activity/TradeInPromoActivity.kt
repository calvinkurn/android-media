package com.tokopedia.tradein.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.tradein.R
import com.tokopedia.tradein.view.fragment.TradeInPromoDetailPageFragment

class TradeInPromoActivity : BaseSimpleActivity() {

    companion object {
        const val TRADE_IN_PROMO_CODE = "TRADE_IN_PROMO_CODE"

        @JvmStatic
        fun getIntent(context : Context, code : String) : Intent {
            return Intent(context, TradeInPromoActivity::class.java).apply {
                putExtra(TRADE_IN_PROMO_CODE, code)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return intent?.getStringExtra(TRADE_IN_PROMO_CODE)?.let {
            TradeInPromoDetailPageFragment.getFragmentInstance(it)
        }
    }
}