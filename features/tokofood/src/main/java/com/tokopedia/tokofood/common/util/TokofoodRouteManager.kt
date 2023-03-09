package com.tokopedia.tokofood.common.util

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodCategoryFragment
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodCategoryFragmentOld
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragmentOld
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.MerchantPageFragment
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.MerchantPageFragmentOld
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragmentOld
import com.tokopedia.tokofood.feature.search.container.presentation.fragment.SearchContainerFragment

object TokofoodRouteManager {

    private const val HOST_TOKOFOOD = "food"
    private const val PATH_HOME = "/home"
    private const val PATH_MERCHANT = "/merchant"
    private const val PATH_PURCHASE = "/purchase"
    private const val PATH_CATEGORY = "/category"
    private const val PATH_SEARCH = "/search"
    private const val PATH_OLD_HOME = "/old-home"
    private const val PATH_OLD_MERCHANT = "/old-merchant"
    private const val PATH_OLD_PURCHASE = "/old-purchase"
    private const val PATH_OLD_CATEGORY = "/old-category"
    private const val PATH_OLD_SEARCH = "/old-search"

    fun mapUriToFragment(context: Context, uri: Uri): Fragment? {
        // tokopedia://food
        if (uri.host == HOST_TOKOFOOD) {
            val f: Fragment? =
                uri.path?.let { uriPath ->
                    val isGtpMigration = getIsGtpMigration(context)
                    when {
                        uriPath.startsWith(PATH_HOME) || uriPath.startsWith(PATH_OLD_HOME) ->
                            if (isGtpMigration) TokoFoodHomeFragment.createInstance() else TokoFoodHomeFragmentOld.createInstance() // tokopedia://food/home
                        uriPath.startsWith(PATH_MERCHANT) || uriPath.startsWith(PATH_OLD_MERCHANT) ->
                            if (isGtpMigration) MerchantPageFragment.createInstance() else MerchantPageFragmentOld.createInstance()// tokopedia://food/merchant
                        uriPath.startsWith(PATH_PURCHASE) || uriPath.startsWith(PATH_OLD_PURCHASE) ->
                            if (isGtpMigration) TokoFoodPurchaseFragment.createInstance() else TokoFoodPurchaseFragmentOld.createInstance() // tokopedia://food/purchase
                        uriPath.startsWith(PATH_CATEGORY) || uriPath.startsWith(PATH_OLD_CATEGORY) ->
                            if (isGtpMigration) TokoFoodCategoryFragment.createInstance() else TokoFoodCategoryFragmentOld.createInstance() // tokopedia://food/category
                        uriPath.startsWith(PATH_SEARCH) || uriPath.startsWith(PATH_OLD_SEARCH) -> SearchContainerFragment.createInstance() // tokopedia://food/search
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
            val mappedUriString = DeeplinkMapperTokoFood.mapperInternalApplinkTokoFood(activity, initialUri)
            val uri = Uri.parse(mappedUriString)
            val f = mapUriToFragment(activity, uri)
            if (f == null) {
                RouteManager.route(activity, mappedUriString)
            } else {
                activity.navigateToNewFragment(f, isFinishCurrent)
            }
        }
    }

    private fun getIsGtpMigration(context: Context): Boolean {
        return FirebaseRemoteConfigImpl(context).getBoolean(RemoteConfigKey.IS_TOKOFOOD_NEW_GTP_FLOW).orFalse()
    }
}
