package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.constant.BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_COUNT

class ChooseProductActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, campaignId: String, selectedProductCount: Int) =
            Intent(context, ChooseProductActivity::class.java).apply {
                putExtra(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                putExtra(BUNDLE_KEY_SELECTED_PRODUCT_COUNT, selectedProductCount)
            }
    }

    private val campaignId by lazy {
        intent?.getStringExtra(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID).orEmpty()
    }

    private val selectedProductCount by lazy {
        intent?.getIntExtra(BUNDLE_KEY_SELECTED_PRODUCT_COUNT, Int.ZERO).orZero()
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_common

    override fun getParentViewResourceID(): Int = R.id.container

    override fun getNewFragment() = ChooseProductFragment.newInstance(campaignId, selectedProductCount)

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