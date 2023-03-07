package com.tokopedia.tkpd.flashsale.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant

class CampaignDetailActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, flashSaleId: Long, totalSubmittedProduct: Long = 0) {
            val intent = Intent(context, CampaignDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            bundle.putLong(BundleConstant.BUNDLE_KEY_TOTAL_SUBMITTED_PRODUCT, totalSubmittedProduct)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    private val flashSaleId by lazy {
        intent?.extras?.getLong(BundleConstant.BUNDLE_FLASH_SALE_ID).orZero()
    }

    private val totalSubmittedProduct by lazy {
        intent?.extras?.getLong(BundleConstant.BUNDLE_KEY_TOTAL_SUBMITTED_PRODUCT).orZero()
    }

    override fun getLayoutRes() = R.layout.stfs_activity_campaign_detail
    override fun getNewFragment() = CampaignDetailFragment.newInstance(flashSaleId, totalSubmittedProduct)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_campaign_detail)
    }
}
