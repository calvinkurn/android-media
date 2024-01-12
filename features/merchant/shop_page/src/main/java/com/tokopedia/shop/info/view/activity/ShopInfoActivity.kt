package com.tokopedia.shop.info.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment
import com.tokopedia.shop.info.view.fragment.ShopInfoReimagineFragment

/**
 * Navigate to ShopInfoActivity
 * use [com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_INFO]
 */
class ShopInfoActivity : BaseActivity() {

    private val shopId by lazy {
        intent.getStringExtra(SHOP_ID) ?: intent.data?.lastPathSegment.orEmpty()
    }
    private val shopData by lazy {
        intent.getParcelableExtra<ShopInfoData?>(EXTRA_SHOP_INFO)
    }
    companion object {
        fun createIntent(
            context: Context,
            shopId: String? = null,
            shopInfo: ShopInfoData? = null
        ): Intent {
            return Intent(context, ShopInfoActivity::class.java).apply {
                putExtra(SHOP_ID, shopId)
                putExtra(EXTRA_SHOP_INFO, shopInfo)
            }
        }

        const val EXTRA_SHOP_INFO = "extra_shop_info"
        const val SHOP_ID = "EXTRA_SHOP_ID"
    }

    override fun getScreenName(): String = ShopInfoActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_info)
        loadFragment()
    }

    private fun loadFragment() {
        if (useReimagineVersion()) {
            showFragment(ShopInfoReimagineFragment.newInstance(shopId))
        } else {
            showFragment(ShopInfoFragment.createInstance(shopId, shopData))
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    private fun useReimagineVersion(): Boolean {
        return try {
            val abTestPlatform = RemoteConfigInstance.getInstance().abTestPlatform
            val abTestValue = abTestPlatform.getString(
                key = RollenceKey.AB_TEST_GRADUAL_ROLLOUT_KEY_SHOP_INFO_REIMAGINED,
                defaultValue = ""
            )

            val redirectToReimagineVersion = abTestValue.isNotEmpty()
            redirectToReimagineVersion
        } catch (throwable: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
            false
        }
    }
}
