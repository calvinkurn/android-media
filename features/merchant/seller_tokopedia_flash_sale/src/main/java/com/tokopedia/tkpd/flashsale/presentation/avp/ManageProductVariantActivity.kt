package com.tokopedia.tkpd.flashsale.presentation.avp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R

class ManageProductVariantActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, ManageProductVariantActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun getLayoutRes() = R.layout.stfs_activity_manage_product_variant
    override fun getNewFragment() = ManageProductVariantFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}