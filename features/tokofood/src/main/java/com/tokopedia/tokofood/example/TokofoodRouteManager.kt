package com.tokopedia.tokofood.example

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseFragment

object TokofoodRouteManager {

    fun mapUriToFragment(uri: Uri): BaseMultiFragment? {
        // tokopedia://tokofood
        if (uri.host == "tokofood") {
            var f: IBaseMultiFragment? = null
            if (uri.path == "/home") { // tokopedia://tokofood/home
                f = FragmentA()
            } else if (uri.path == "/b") { // tokopedia://tokofood/b
                f = FragmentB()
            } else if (uri.path == "/purchase") {
                f = TokoFoodPurchaseFragment.createInstance()
            }
            if (f != null) {
//                f.arguments = Bundle().apply {
//                    putString(Constant.DATA_KEY, uri.toString())
//                }
//                return f
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