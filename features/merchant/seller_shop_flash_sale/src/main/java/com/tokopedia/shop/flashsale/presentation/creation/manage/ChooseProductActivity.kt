package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.BundleConstant

class ChooseProductActivity : BaseSimpleActivity() {

    companion object {

        @JvmStatic
        fun start(context: Context, campaignId: String) {
            val starter = Intent(context, ChooseProductActivity::class.java).apply {
                putExtra(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            }
            context.startActivity(starter)
        }
    }

    private val campaignId by lazy {
        intent?.getStringExtra(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID).orEmpty()
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_common

    override fun getParentViewResourceID(): Int = R.id.container

    override fun getNewFragment() = ChooseProductFragment.newInstance(campaignId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupHeader()
    }

    private fun setupHeader() {
        val header: HeaderUnify = findViewById(R.id.header)
        header.setNavigationOnClickListener { onBackPressed() }
        header.headerTitle = getString(R.string.chooseproduct_page_title)
    }
}