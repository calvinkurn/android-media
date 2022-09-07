package com.tokopedia.tkpd.flashsale.presentation.chooseproduct

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.fragment.ChooseProductFragment
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant

class ChooseProductActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, campaignId: Long) {
            val intent = Intent(context, ChooseProductActivity::class.java)
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private val campaignId by lazy {
        intent?.extras?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID).orZero()
    }

    override fun getLayoutRes() = R.layout.stfs_activity_flash_sale_list_container
    override fun getNewFragment() = ChooseProductFragment.newInstance(campaignId)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_flash_sale_list_container)
    }
}