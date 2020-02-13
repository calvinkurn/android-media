package com.tokopedia.browse.categoryNavigation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.browse.R


class CategoryBrowseActivity : BaseCategoryBrowseActivity() {

    private val launchSource: String = "Belanja/Home"


    override fun getCategoryLaunchSource(): String {
        return launchSource
    }

    override fun getScreenName() = getString(R.string.belanja_screen_name)

    object DeepLinkIntents {
        lateinit var bundle: Bundle

        @DeepLink(ApplinkConst.CATEGORY_BELANJA_DEFAULT)
        @JvmStatic
        fun getCategoryBrowseDefaultIntent(context: Context, extras: Bundle): Intent {
            bundle = extras
            return openBelanjaActivity(context)
        }

        private fun openBelanjaActivity(context: Context): Intent {
            return Intent(context, CategoryBrowseActivity::class.java)
        }
    }
}
