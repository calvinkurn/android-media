package com.tokopedia.shop.flashsale.presentation.list.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent

class CampaignListActivity : BaseSimpleActivity() {

    companion object {
        private const val KEY_BUNDLE_IS_SAVE_DRAFT = "is_save_draft"

        @JvmStatic
        fun start(
            context: Context,
            isSaveDraft: Boolean = false,
            isClearTop : Boolean = isSaveDraft,
        ) {
            val starter = Intent(context, CampaignListActivity::class.java).apply {
                if (isClearTop) addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                if (isSaveDraft) {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra(KEY_BUNDLE_IS_SAVE_DRAFT, isSaveDraft)
                }
            }
            context.startActivity(starter)
        }
    }


    override fun getLayoutRes() = R.layout.ssfs_activity_campaign_list
    override fun getNewFragment() = CampaignListContainerFragment.newInstance()
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
        setContentView(R.layout.ssfs_activity_campaign_list)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val isSaveDraft = intent?.getBooleanExtra(KEY_BUNDLE_IS_SAVE_DRAFT, false)
        if (isSaveDraft == true) {
            val container = fragment as? CampaignListContainerFragment
            container?.showSaveDraftSuccessInActiveTab()
        }
    }
}