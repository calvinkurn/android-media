package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.power_merchant.subscribe.ACTION_KEY
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantTermsFragment

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PowerMerchantTermsFragment.createInstance(intent?.extras ?: Bundle())
    }

    companion object {
        fun createIntent(context: Context, action: String): Intent {
            return Intent(context, PowerMerchantTermsActivity::class.java).apply {
                putExtra(ACTION_KEY, action)
            }
        }
    }
}