package com.tokopedia.merchantvoucher.voucherList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.merchantvoucher.MerchantVoucherModuleRouter
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.di.MerchantVoucherComponent
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherListActivity : BaseSimpleActivity(), HasComponent<MerchantVoucherComponent> {

    lateinit var shopId: String

    override fun getComponent() = DaggerMerchantVoucherComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun getNewFragment(): Fragment = MerchantVoucherListFragment.createInstance(shopId)

    companion object {
        const val SHOP_ID = "shop_id"

        @JvmStatic
        fun createIntent(context: Context, shopId: String): Intent {
            return Intent(context, MerchantVoucherListActivity::class.java).apply { putExtra(SHOP_ID, shopId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(SHOP_ID)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            onShareShop()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun onShareShop() {
        if (fragment!= null && fragment is MerchantVoucherListFragment){
            val shopInfo: ShopInfo? = (fragment as MerchantVoucherListFragment).shopInfo
            if (shopInfo!= null) {
                (application as MerchantVoucherModuleRouter).goToShareShop(this@MerchantVoucherListActivity,
                shopId, shopInfo.info.shopUrl, getString(R.string.shop_label_share_formatted,
                MethodChecker.fromHtml(shopInfo.info.shopName).toString(), shopInfo.info.shopLocation))
            }
        }
    }
}
