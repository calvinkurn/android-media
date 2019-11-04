package com.tokopedia.product.manage.item.video.view.activity

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.dialog.BaseTextPickerDialogFragment
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment

class ProductAddVideoActivity : BaseSimpleActivity(), BaseTextPickerDialogFragment.Listener {

    private lateinit var productAddVideoFragment: Fragment

    override fun getNewFragment(): Fragment{
        productAddVideoFragment = ProductAddVideoFragment.createInstance()
        return productAddVideoFragment
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putStringArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEOS_LINKS, (fragment as ProductAddVideoFragment).videoIDs)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

    override fun onTextPickerSubmitted(text: String) {
        (fragment as ProductAddVideoFragment).addVideoIDfromURL(text)
    }
}