package com.tokopedia.product.edit.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.fragment.ProductAddVideoFragment

class ProductAddVideoActivity : BaseSimpleActivity() {

    private lateinit var productAddVideoFragment: Fragment

    override fun getNewFragment(): Fragment? {
        productAddVideoFragment = ProductAddVideoFragment.createInstance()
        return productAddVideoFragment;
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putStringArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEOS_LINKS, (fragment as ProductAddVideoFragment).videoIDs)
        if (intent != null) {
            setResult(Activity.RESULT_OK, intent)
        }
        super.onBackPressed()
    }
}
