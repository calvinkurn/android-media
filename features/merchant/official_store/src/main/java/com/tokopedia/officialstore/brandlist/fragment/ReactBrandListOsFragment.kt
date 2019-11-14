package com.tokopedia.officialstore.brandlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.tkpdreactnative.react.ReactConst
import com.tokopedia.tkpdreactnative.react.ReactUtils
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment

class ReactBrandListOsFragment : ReactNativeFragment() {

    override fun getModuleName(): String {
        return ReactConst.Screen.BRANDLIST_CATEGORY
    }

    override fun getInitialBundle(): Bundle {
        return arguments ?: Bundle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ReactUtils.startTracing(MP_BRAND_LIST)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        private val MP_BRAND_LIST = "mp_brand_list"

        fun createInstance(bundle: Bundle): ReactBrandListOsFragment {
            val fragment = ReactBrandListOsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}