package com.tokopedia.officialstore.brandlist.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.officialstore.brandlist.fragment.ReactBrandListOsFragment
import com.tokopedia.tkpdreactnative.react.ReactConst
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity

/**
 * Created by yogie putra on 29/03/18.
 */

class ReactNativeBrandListOsActivity : ReactFragmentActivity<ReactBrandListOsFragment>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    override fun getReactNativeFragment(): ReactBrandListOsFragment {
        return ReactBrandListOsFragment.createInstance(reactNativeProps)
    }

    override fun getToolbarTitle(): String? {
        return if (intent != null && intent.extras != null) {
            intent.extras!!.getString(DeepLickIntents.EXTRA_TITLE)
        } else ""
    }

    object DeepLickIntents {

        val EXTRA_TITLE = "EXTRA_TITLE"

        @JvmStatic
        @DeepLink(ApplinkConst.BRAND_LIST, ApplinkConst.BRAND_LIST_WITH_SLASH, ApplinkConst.BRAND_LIST_CATEGORY)
        fun getBrandlistApplinkCallingIntent(context: Context, bundle: Bundle): Intent {
            return createApplinkCallingIntent(context, ReactConst.Screen.BRANDLIST_PAGE, "All Brands", bundle)
        }

        fun createApplinkCallingIntent(context: Context, reactScreenName: String, pageTitle: String, extras: Bundle): Intent {
            val intent = Intent(context, ReactNativeBrandListOsActivity::class.java)
            extras.putString(ReactConst.KEY_SCREEN, reactScreenName)
            extras.putString(EXTRA_TITLE, pageTitle)
            intent.putExtras(extras)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun createCallingIntent(context: Context, reactScreenName: String, pageTitle: String): Intent {
            val intent = Intent(context, ReactNativeBrandListOsActivity::class.java)
            val extras = Bundle()
            extras.putString(ReactConst.KEY_SCREEN, reactScreenName)
            extras.putString(EXTRA_TITLE, pageTitle)
            intent.putExtras(extras)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

    }
}

