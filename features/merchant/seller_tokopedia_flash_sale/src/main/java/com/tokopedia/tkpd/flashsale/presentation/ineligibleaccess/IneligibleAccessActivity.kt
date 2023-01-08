package com.tokopedia.tkpd.flashsale.presentation.ineligibleaccess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R

class IneligibleAccessActivity : BaseSimpleActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, IneligibleAccessActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutRes() = R.layout.stfs_activity_ineligible_access
    override fun getNewFragment() = IneligibleAccessFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_ineligible_access)
    }
}
