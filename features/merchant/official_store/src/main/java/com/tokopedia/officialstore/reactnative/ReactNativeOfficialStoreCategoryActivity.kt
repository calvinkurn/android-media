package com.tokopedia.officialstore.reactnative

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.officialstore.R
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity

class ReactNativeOfficialStoreCategoryActivity : ReactFragmentActivity<ReactNativeOfficialStoreCategoryFragment>() {

    override fun getReactNativeFragment(): ReactNativeOfficialStoreCategoryFragment {
        return ReactNativeOfficialStoreCategoryFragment.createInstance(reactNativeProps)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = toolbar
        if (toolbar != null) {
            toolbar.visibility = View.GONE
        }
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.react_native_banner_official_title)
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        // No super to avoid crash transactionTooLarge
    }

    object DeeplinkIntent {

        @DeepLink(ApplinkConst.OFFICIAL_STORES_CATEGORY)
        @JvmStatic
        fun getOfficialStoreCategoryApplinkCallingIntent(context: Context, bundle: Bundle): Intent {
            return createApplinkCallingIntent(context, bundle)
        }

        fun createApplinkCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, ReactNativeOfficialStoreCategoryActivity::class.java)
            intent.putExtras(extras)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }
}