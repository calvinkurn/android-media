package com.tokopedia.browse.categoryNavigation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.categoryNavigation.CategoryNavigationConfig


class CategoryBrowseActivity : BaseCategoryBrowseActivity() {

    val launchSource: String = "Belanja/Home"


    override fun getCategoryLaunchSource(): String {
        return launchSource
    }


    object DeepLinkIntents {
        val KEY_EXTRA_DEPARTMENT_ID = "EXTRA_DEPARTMENT_ID"

        @DeepLink(ApplinkConst.CATEGORY_BELANJA_DEFAULT)
        @JvmStatic
        fun getCategoryBrowseDefaultIntent(context: Context, extras: Bundle): Intent {
            return if (isNewCategoryEnabled(context) && CategoryNavigationConfig.isNewCategoryEnabled) {
                Intent(context, CategoryBrowseActivity::class.java)
            } else {
                RouteManager.getIntent(context, ApplinkConst.CATEGORY + "/" + extras.getString(KEY_EXTRA_DEPARTMENT_ID, ""))
            }
        }
    }
}
