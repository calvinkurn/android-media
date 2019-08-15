package com.tokopedia.browse.categoryNavigation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.navigation_common.category.CategoryNavigationConfig


class CategoryBrowseActivity : BaseCategoryBrowseActivity() {

    val launchSource: String = "Belanja/Home"


    override fun getCategoryLaunchSource(): String {
        return launchSource
    }


    object DeepLinkIntents {
        val KEY_EXTRA_DEPARTMENT_ID = "EXTRA_DEPARTMENT_ID"
        lateinit var bundle: Bundle

        @DeepLink(ApplinkConst.CATEGORY_BELANJA_DEFAULT)
        @JvmStatic
        fun getCategoryBrowseDefaultIntent(context: Context, extras: Bundle): Intent {
            bundle = extras
            return CategoryNavigationConfig.updateCategoryConfig(context, ::runNewBelanja,::runOldBelanja)
        }

        fun runNewBelanja(context: Context): Intent {
            return Intent(context, CategoryBrowseActivity::class.java)
        }

        fun runOldBelanja(context: Context): Intent {
          return  RouteManager.getIntent(context, ApplinkConst.CATEGORY + "/" + bundle.getString(KEY_EXTRA_DEPARTMENT_ID, ""))
        }
    }
}
