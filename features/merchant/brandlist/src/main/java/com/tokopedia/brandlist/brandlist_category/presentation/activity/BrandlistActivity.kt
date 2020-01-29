package com.tokopedia.brandlist.brandlist_category.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.brandlist.brandlist_category.presentation.fragment.BrandlistContainerFragment

class BrandlistActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        val categoryId: String
        val bundle = intent.extras
        val uri = intent.data

        if (uri != null && uri.pathSegments.size > 1)  {
            val uriSegment = uri.pathSegments
            categoryId = uriSegment[uriSegment.size - 1]
        } else {
            categoryId = bundle.getString(CATEGORY_EXTRA_APPLINK)
        }

        return BrandlistContainerFragment.createInstance(categoryId)
    }

    companion object {
        const val CATEGORY = "CATEGORY"
        const val CATEGORY_EXTRA_APPLINK = "category"
    }

    object DeeplinkIntents {
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, BrandlistActivity::class.java)
                    .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                    .putExtra(CATEGORY, extras.getString(CATEGORY_EXTRA_APPLINK))
                    .putExtras(extras)
        }

    }
}
