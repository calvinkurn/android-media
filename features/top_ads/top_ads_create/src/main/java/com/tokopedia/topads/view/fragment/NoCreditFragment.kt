package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.create.R
import kotlinx.android.synthetic.main.topads_create_activity_success.*

class NoCreditFragment : TkpdBaseV4Fragment() {

    companion object{
        fun newInstance():NoCreditFragment{
            val args = Bundle()
            val fragment = NoCreditFragment()
            fragment.arguments = args
            return fragment

        }
    }
    override fun getScreenName(): String {
        return NoCreditFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_create_bottom_sheet_insufficient_credit),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_go_to_dashboard.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL)

        }
    }

}