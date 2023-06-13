package com.tokopedia.shop.flashsale.presentation.list.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class CampaignListActivity : BaseSimpleActivity() {

    companion object {
        private const val KEY_BUNDLE_IS_SAVE_DRAFT = "is_save_draft"

        @JvmStatic
        fun start(
            context: Context,
            isSaveDraft: Boolean,
            previousPageMode: PageMode
        ) {
            val starter = Intent(context, CampaignListActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                if (isSaveDraft) addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtras(buildBundle(isSaveDraft, previousPageMode))
            }
            context.startActivity(starter)
        }

        private fun buildBundle(isSaveDraft: Boolean, previousPageMode: PageMode) : Bundle {
            return Bundle().apply {
                putBoolean(KEY_BUNDLE_IS_SAVE_DRAFT, isSaveDraft)
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, previousPageMode)
            }
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
        val isSaveDraft = intent?.getBooleanExtra(KEY_BUNDLE_IS_SAVE_DRAFT, false).orFalse()
        if (isSaveDraft) {
            handleDisplayToaster(intent)
        }
    }

    private fun handleDisplayToaster(intent: Intent?) {
        val container = fragment as? CampaignListContainerFragment ?: return
        val pageMode = intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
        if (pageMode == PageMode.UPDATE || pageMode == PageMode.DRAFT) {
            container.showEditCampaignSuccessInActiveTab()
        } else {
            container.showSaveDraftSuccessInActiveTab()
        }
    }
}