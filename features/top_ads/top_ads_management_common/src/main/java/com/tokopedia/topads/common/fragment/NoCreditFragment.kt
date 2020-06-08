package com.tokopedia.topads.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.common.R
import kotlinx.android.synthetic.main.topads_create_bottom_sheet_insufficient_credit.*

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
        super.onViewCreated(view, savedInstanceState)
        btn_topup.setOnClickListener {
            val intent =RouteManager.getIntent(context,ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT)
            startActivity(intent)
            activity?.finish()
        }
    }

}