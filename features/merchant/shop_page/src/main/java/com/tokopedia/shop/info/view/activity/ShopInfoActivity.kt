package com.tokopedia.shop.info.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.shop.R
import com.tokopedia.shop.info.view.fragment.ShopInfoReimagineFragment

/**
 * Navigate to ShopInfoActivity
 * use [com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_INFO]
 */
class ShopInfoActivity : BaseActivity() {
    companion object {
        const val EXTRA_SHOP_INFO = "extra_shop_info"
        private const val SHOP_ID = "EXTRA_SHOP_ID"
    }

    private val shopId by lazy {
        intent.getStringExtra(SHOP_ID) ?: intent.data?.lastPathSegment.orEmpty()
    }

    override fun getScreenName(): String = ShopInfoActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_info)
        showFragment(ShopInfoReimagineFragment.newInstance(shopId))
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}
