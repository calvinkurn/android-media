package com.tokopedia.affiliate.ui.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.affiliate.ui.fragment.AffiliatePromotionHistoryFragment
import com.tokopedia.affiliate_toko.R

class AffiliatePromotionHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affiliate_promotion_history)
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.frame,AffiliatePromotionHistoryFragment.getFragmentInstance(),"FragmentInstance")
            commitNow()
        }
    }
}