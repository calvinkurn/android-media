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
        fun start(context: Context?, flashSaleId: Long, tabName: String) {
            context ?: return
            val intent = Intent(context, ChooseProductActivity::class.java)
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            bundle.putString(BundleConstant.BUNDLE_KEY_TAB_NAME, tabName)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private val flashSaleId by lazy {
        intent?.extras?.getLong(BundleConstant.BUNDLE_FLASH_SALE_ID).orZero()
    }
    private val tabName by lazy {
        intent?.extras?.getString(BundleConstant.BUNDLE_KEY_TAB_NAME).orEmpty()
    }

    override fun getLayoutRes() = R.layout.stfs_activity_flash_sale_list_container
    override fun getNewFragment() = ChooseProductFragment.newInstance(flashSaleId, tabName)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_flash_sale_list_container)
    }
}