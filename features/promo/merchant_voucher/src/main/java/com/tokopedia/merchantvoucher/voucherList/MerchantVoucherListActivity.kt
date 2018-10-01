package com.tokopedia.merchantvoucher.voucherList

import android.content.Context
import android.content.Intent
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

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherListActivity : BaseSimpleActivity(), HasComponent<MerchantVoucherComponent> {

//    var shopInfo: ShopInfo? = null

    override fun getComponent() = DaggerMerchantVoucherComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun getNewFragment(): Fragment = MerchantVoucherListFragment.createInstance()

    companion object {
        @JvmStatic
        fun createIntent(context: Context, shopId:String) = Intent(context, MerchantVoucherListActivity::class.java)
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
//        (application as MerchantVoucherModuleRouter).goToShareShop(this@MerchantVoucherListActivity,
//                shopId, info.shopUrl, getString(R.string.shop_label_share_formatted,
//                MethodChecker.fromHtml(info.shopName).toString(), info.shopLocation))

    }
}
