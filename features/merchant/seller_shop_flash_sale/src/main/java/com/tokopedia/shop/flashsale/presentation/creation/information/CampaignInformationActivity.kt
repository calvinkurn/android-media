package com.tokopedia.shop.flashsale.presentation.creation.information

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.constant.Constant.CAMPAIGN_NOT_CREATED_ID
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class CampaignInformationActivity : BaseSimpleActivity() {

    companion object {
        const val REQUEST_CODE_CREATE_CAMPAIGN_INFO = 100

        @JvmStatic
        fun startUpdateMode(context: Context, campaignId: Long, isClearTop: Boolean = false) {
            val starter = Intent(context, CampaignInformationActivity::class.java)
            if (isClearTop) {
                starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            val bundle = Bundle()
            bundle.putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.UPDATE)
            bundle.putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            starter.putExtras(bundle)

            context.startActivity(starter)
        }
    }

    private val pageMode by lazy {
        intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }

    private val campaignId by lazy {
        intent?.extras?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, CAMPAIGN_NOT_CREATED_ID).orZero()
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_campaign_information
    override fun getNewFragment() = CampaignInformationFragment.newInstance(pageMode, campaignId)
    override fun getParentViewResourceID() = R.id.container

    private fun setupDependencyInjection() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.ssfs_activity_campaign_information)
    }
}