package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.router.TopAdsAutoRouter
import java.net.URL

/**
 * Author errysuprayogi on 09,May,2019
 */
class InsufficientBalanceFragment : BaseDaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_autoads_insufficient_budget, container, false)
        view.findViewById<View>(R.id.add_fund).setOnClickListener {
            var intent = (activity!!.application as TopAdsAutoRouter).getSellerWebViewIntent(activity!!, arguments!!.getString(KEY_URL))
            startActivity(intent)
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

        private const val KEY_URL = "URL"

        fun newInstance(url: String): InsufficientBalanceFragment {

            val args = Bundle()

            val fragment = InsufficientBalanceFragment()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }
    }
}
