package com.tokopedia.tkpd.flashsale.presentation.manageproductlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant

class FlashSaleManageProductListActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, reservationId: String, flashSaleId: String, tabName: String) {
            val intent = Intent(context, FlashSaleManageProductListActivity::class.java)
            val bundle = Bundle()
            bundle.putString(BundleConstant.BUNDLE_KEY_RESERVATION_ID, reservationId)
            bundle.putString(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            bundle.putString(BundleConstant.BUNDLE_KEY_TAB_NAME, tabName)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private val reservationId by lazy {
        intent.extras?.getString(BundleConstant.BUNDLE_KEY_RESERVATION_ID).orEmpty()
    }

    private val campaignId by lazy {
        intent.extras?.getString(BundleConstant.BUNDLE_FLASH_SALE_ID).orEmpty()
    }

    private val tabName by lazy {
        intent.extras?.getString(BundleConstant.BUNDLE_KEY_TAB_NAME).orEmpty()
    }

    override fun getLayoutRes() = R.layout.stfs_activity_campaign_detail

    override fun getNewFragment() = FlashSaleManageProductListFragment.newInstance(
        reservationId,
        campaignId,
        tabName
    )

    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_manage_product_list)
    }
}