package com.tokopedia.power_merchant.subscribe.view_old.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantTermsFragment

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PowerMerchantTermsFragment.createInstance(intent?.extras ?: Bundle())
    }

    companion object {
        const val EXTRA_SHOP_SCORE = "extra_shop_score"

        fun createIntent(context: Context, shopScore: Int): Intent {
            return Intent(context, PowerMerchantTermsActivity::class.java).apply {
                putExtra(EXTRA_SHOP_SCORE, shopScore)
            }
        }
    }
}