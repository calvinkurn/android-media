package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.topads.auto.R

/**
 * Author errysuprayogi on 09,May,2019
 */
class EmptyProductFragment : BaseDaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_new_ads_empty_product, container, false)
        view.findViewById<View>(R.id.add_product).setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM)
            activity!!.finish()
        }
        return view
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        fun newInstance(): EmptyProductFragment {

            val args = Bundle()

            val fragment = EmptyProductFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
