package com.tokopedia.tokofood.common.util

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.MerchantPageFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.home.presentation.fragment.TokoFoodCategoryFragment
import com.tokopedia.tokofood.home.presentation.fragment.TokoFoodHomeFragment

object TokofoodRouteManager {

    fun mapUriToFragment(uri: Uri): Fragment? {
        // tokopedia://food
        if (uri.host == "food") {
            val f: Fragment? =
                when (uri.path) {
                    "/home" -> TokoFoodHomeFragment.createInstance() // tokopedia://tokofood/home
                    "/merchant" -> MerchantPageFragment.createInstance() // tokopedia://tokofood/merchant
                    "/purchase" -> TokoFoodPurchaseFragment.createInstance() // tokopedia://tokofood/purchase
                    "/category" -> TokoFoodCategoryFragment.createInstance() // tokopedia://tokofood/category
                    else -> null
                }
            if (f != null) {
                f.arguments = Bundle().apply {
                    putString(Constant.DATA_KEY, uri.toString())
                }
                return f
            }
        }
        return null
    }

    /**
     * function that will route the uri to given destination, either fragment or activity
     * If the uriString can be handled in Activity, it will go to new fragment.
     * Otherwise, it will go to Activity
     */
    fun routePrioritizeInternal(context: Context, uriString: String) {
        val activity: BaseMultiFragActivity? = if (context is Fragment) {
            (context.requireActivity() as? BaseMultiFragActivity)
        } else {
            (context as? BaseMultiFragActivity)
        }
        if (activity == null) {
            RouteManager.route(context, uriString)
        } else {
            val uri = Uri.parse(uriString)
            val f = mapUriToFragment(uri)
            if (f != null) {
                activity.navigateToNewFragment(f)
            } else {
                RouteManager.route(activity, uriString)
            }
        }
    }
}