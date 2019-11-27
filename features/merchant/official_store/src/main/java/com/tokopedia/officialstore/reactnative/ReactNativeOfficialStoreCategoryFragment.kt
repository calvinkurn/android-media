package com.tokopedia.officialstore.reactnative

import android.os.Bundle

import com.tokopedia.tkpdreactnative.react.ReactConst
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment

class ReactNativeOfficialStoreCategoryFragment : ReactNativeFragment() {

    override fun getModuleName(): String {
        return ReactConst.Screen.OFFICIAL_STORE_CATEGORY
    }

    override fun getInitialBundle(): Bundle {
        return arguments ?: Bundle()
    }

    companion object {
        private val CATEGORY = "Category"
        private val KEY_CATEGORY = "key_category"


        fun createInstance(bundle: Bundle?): ReactNativeOfficialStoreCategoryFragment {
            var bundle = bundle
            if (bundle == null)
                bundle = Bundle()

            val osFragment = ReactNativeOfficialStoreCategoryFragment()
            bundle.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.OFFICIAL_STORE)
            val category = bundle.getString(KEY_CATEGORY)
            if (category != null && !category.isEmpty()) {
                bundle.putString(CATEGORY, bundle.getString(KEY_CATEGORY))
            }
            osFragment.arguments = bundle
            return osFragment
        }
    }

}