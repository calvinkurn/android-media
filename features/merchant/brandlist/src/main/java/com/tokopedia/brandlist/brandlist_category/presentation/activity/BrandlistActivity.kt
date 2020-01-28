package com.tokopedia.brandlist.brandlist_category.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.brandlist.brandlist_category.presentation.fragment.BrandlistContainerFragment

class BrandlistActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return BrandlistContainerFragment.createInstance()
//        return BrandlistContainerFragment.createInstance(
//                intent.getStringExtra(CATEGORY_EXTRA_APPLINK)
//        )
    }

    companion object {
        const val CATEGORY = "CATEGORY"
        const val CATEGORY_EXTRA_APPLINK = "category"
    }

    object DeeplinkIntents {
//        @DeepLink(ApplinkConst.BRAND_LIST, ApplinkConst.BRAND_LIST_WITH_SLASH, ApplinkConst.BRAND_LIST_CATEGORY)
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, BrandlistActivity::class.java)
                    .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                    .putExtra(CATEGORY, extras.getString(CATEGORY_EXTRA_APPLINK))
                    .putExtras(extras)
        }

    }
}
