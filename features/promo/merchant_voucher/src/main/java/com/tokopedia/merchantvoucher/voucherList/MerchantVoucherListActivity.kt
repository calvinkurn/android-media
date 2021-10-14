package com.tokopedia.merchantvoucher.voucherList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.share.DefaultShare
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.analytic.MerchantVoucherTracking
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

/**
 * Created by hendry on 21/09/18.
 */
class MerchantVoucherListActivity : BaseSimpleActivity(),
        MerchantVoucherListFragment.OnMerchantVoucherListFragmentListener {

    lateinit var shopId: String
    var shopInfo: ShopInfo? = null
    var shopName: String? = null
    lateinit var merchantVoucherTracking: MerchantVoucherTracking

    override fun getNewFragment(): Fragment = MerchantVoucherListFragment.createInstance(shopId)

    companion object {
        const val SHOP_ID = "shop_id"       // to get voucher list by shop, to get shop detail
        const val SHOP_NAME = "shop_name"   // placeholder for shop name first time (optional)

        @JvmStatic
        fun createIntent(context: Context, shopId: String, shopName: String? = null): Intent {
            return Intent(context, MerchantVoucherListActivity::class.java).apply {
                putExtra(SHOP_ID, shopId)
                putExtra(SHOP_NAME, shopName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(SHOP_ID) ?: ""
        shopName = intent.getStringExtra(SHOP_NAME)
        super.onCreate(savedInstanceState)
        merchantVoucherTracking = MerchantVoucherTracking()
        if (!shopName.isNullOrEmpty()) {
            supportActionBar?.title = getString(R.string.merchant_voucher_x, shopName)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (shopInfo == null) {
            menu?.clear()
        } else {
            menuInflater.inflate(R.menu.menu_share, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            onShareShop()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun enableShare(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.shopName = shopInfo.shopCore.name
        title = MethodChecker.fromHtml( getString(R.string.merchant_voucher_x, shopName))
        supportActionBar?.title = title
        invalidateOptionsMenu()
    }

    private fun onShareShop() {
        if (fragment != null && fragment is MerchantVoucherListFragment) {
            val shopInfo: ShopInfo? = (fragment as MerchantVoucherListFragment).shopInfo
            shopInfo?.let {
                merchantVoucherTracking.clickShare()
                val shareMsg = getString(R.string.shop_label_share_formatted,
                        MethodChecker.fromHtml(shopInfo.shopCore.name).toString(), shopInfo.location)
                val shareData = LinkerData.Builder.getLinkerBuilder()
                        .setType(LinkerData.SHOP_TYPE)
                        .setName(getString(R.string.message_share_shop))
                        .setTextContent(shareMsg)
                        .setCustMsg(shareMsg)
                        .setUri(shopInfo.shopCore.url)
                        .setId(shopId)
                        .build()
                DefaultShare(this@MerchantVoucherListActivity, shareData).show()
            }
        }
    }
}
