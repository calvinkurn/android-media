package com.tokopedia.shop.flashsale.presentation.creation.information

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class CampaignInformationActivity: BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, CampaignInformationActivity::class.java)

            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
            starter.putExtras(bundle)

            context.startActivity(starter)
        }

        @JvmStatic
        fun startUpdateMode(context: Context, campaignId: Long) {
            val starter = Intent(context, CampaignInformationActivity::class.java)

            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PAGE_MODE, PageMode.UPDATE)
            bundle.putLong(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            starter.putExtras(bundle)

            context.startActivity(starter)
        }
    }

    private val pageMode by lazy {
        intent?.extras?.getParcelable(BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }

    private val campaignId by lazy {
        intent?.extras?.getLong(BUNDLE_KEY_CAMPAIGN_ID).orZero()
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