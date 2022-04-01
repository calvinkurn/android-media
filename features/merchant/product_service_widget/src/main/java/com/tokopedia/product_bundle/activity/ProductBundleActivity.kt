package com.tokopedia.product_bundle.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_service_widget.R

class ProductBundleActivity : BaseSimpleActivity() {

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getLayoutRes() = R.layout.activity_product_bundle

    override fun getNewFragment(): Fragment {
        return EntrypointFragment()
    }
}