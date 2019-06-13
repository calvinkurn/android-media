package com.tokopedia.shop.open.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.view.fragment.ShopOpenCreateReadyFragment

class ShopOpenCreateReadyActivity : BaseSimpleActivity() {

    companion object {
        val ARGUMENT_DATA_SHOP_ID = "shop_id"
        fun newInstance(activity: Activity, shopId: String): Intent {
            val intent = Intent(activity, ShopOpenCreateReadyActivity::class.java)
            intent.putExtra(ARGUMENT_DATA_SHOP_ID, shopId)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        return ShopOpenCreateReadyFragment.newInstance(intent.getStringExtra(ARGUMENT_DATA_SHOP_ID))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        RouteManager.route(this, ApplinkConst.SHOP,intent.getStringExtra(ARGUMENT_DATA_SHOP_ID) ?: "" )
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.also {
            it.elevation = 0f
            toolbar.setNavigationIcon(R.drawable.ic_close)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.drawable.ic_close){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}