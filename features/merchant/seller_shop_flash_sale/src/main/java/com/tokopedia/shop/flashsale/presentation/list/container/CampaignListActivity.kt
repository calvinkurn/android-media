package com.tokopedia.shop.flashsale.presentation.list.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.presentation.creation.manage.dialog.ShopClosedDialog

class CampaignListActivity: BaseSimpleActivity() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, CampaignListActivity::class.java)
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
}