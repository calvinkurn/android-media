package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.seller_shop_flash_sale.R

class ChooseProductActivity : BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.ssfs_activity_common

    override fun getParentViewResourceID(): Int = R.id.container

    override fun getNewFragment() = ChooseProductFragment()

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