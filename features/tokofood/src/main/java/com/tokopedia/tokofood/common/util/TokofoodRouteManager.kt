package com.tokopedia.tokofood.common.util

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.MerchantPageFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodCategoryFragment
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.OrderCustomizationFragment
import com.tokopedia.tokofood.feature.search.container.presentation.fragment.SearchContainerFragment

object TokofoodRouteManager {

    private const val HOST_TOKOFOOD = "food"
    private const val PATH_HOME = "/home"
    private const val PATH_MERCHANT = "/merchant"
    private const val PATH_PURCHASE = "/purchase"
    private const val PATH_CATEGORY = "/category"
    private const val PATH_SEARCH = "/search"

    fun mapUriToFragment(uri: Uri): Fragment? {
        // tokopedia://food
        if (uri.host == HOST_TOKOFOOD) {
            val f: Fragment? =
                uri.path?.let { uriPath ->
                    when {
                        uriPath.startsWith(PATH_HOME) -> TokoFoodHomeFragment.createInstance() // tokopedia://food/home
                        uriPath.startsWith(PATH_MERCHANT) -> MerchantPageFragment.createInstance() // tokopedia://food/merchant
                        uriPath.startsWith(PATH_PURCHASE) -> TokoFoodPurchaseFragment.createInstance() // tokopedia://food/purchase
                        uriPath.startsWith(PATH_CATEGORY) -> TokoFoodCategoryFragment.createInstance() // tokopedia://food/category
                        uriPath.startsWith(PATH_SEARCH) -> SearchContainerFragment.createInstance() // tokopedia://food/search
                        else -> null
                    }
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
    fun routePrioritizeInternal(context: Context?, uriString: String, isFinishCurrent: Boolean = false) {
        val activity: BaseMultiFragActivity? = if (context is Fragment) {
            (context.requireActivity() as? BaseMultiFragActivity)
        } else {
            (context as? BaseMultiFragActivity)
        }
        if (activity == null) {
            RouteManager.route(context, uriString)
        } else {
            val initialUri = Uri.parse(uriString)
            val mappedUriString = DeeplinkMapperTokoFood.mapperInternalApplinkTokoFood(initialUri)
            val uri = Uri.parse(mappedUriString)
            val f = mapUriToFragment(uri)
            if (f == null) {
                RouteManager.route(activity, mappedUriString)
            } else {
                // If the fragment could take new params, we should replace the existed same class fragment with the new one
                if (f is MerchantPageFragment || f is OrderCustomizationFragment || f is TokoFoodCategoryFragment || f is SearchContainerFragment) {
                    (activity as? BaseTokofoodActivity)?.navigateToNewFragment(f, true, isFinishCurrent)
                } else {
                    activity.navigateToNewFragment(f)
                }
            }
        }
    }
}