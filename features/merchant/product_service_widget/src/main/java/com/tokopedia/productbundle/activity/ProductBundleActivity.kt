package com.tokopedia.productbundle.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.oldproductbundle.activity.ProductBundleActivity
import com.tokopedia.productbundle.fragment.EntrypointFragment
import com.tokopedia.product_service_widget.R
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class ProductBundleActivity : BaseSimpleActivity() {

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getLayoutRes() = R.layout.activity_product_bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getRemoteConfigEnableOldBundleSelectionPage()) {
            directToOldActivity()
        }
    }

    override fun getNewFragment(): Fragment {
        return EntrypointFragment()
    }

    private fun directToOldActivity() {
        val newIntent = Intent(this, ProductBundleActivity::class.java)
        newIntent.data = intent.data
        startActivity(newIntent)
        finish()
    }

    private fun getRemoteConfigEnableOldBundleSelectionPage(): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(this)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_OLD_BUNDLE_SELECTION_PAGE, false)
    }

}