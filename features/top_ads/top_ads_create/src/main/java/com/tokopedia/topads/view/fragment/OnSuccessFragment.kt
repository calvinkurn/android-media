package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.create.R
import kotlinx.android.synthetic.main.topads_create_activity_success.*

class OnSuccessFragment : TkpdBaseV4Fragment() {

    companion object{
        fun newInstance():OnSuccessFragment{
            val args = Bundle()
            val fragment = OnSuccessFragment()
            fragment.arguments = args
            return fragment

        }
    }
    override fun getScreenName(): String {
        return OnSuccessFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(resources.getLayout(R.layout.topads_create_activity_success),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_go_to_dashboard.setOnClickListener {
            val intent =RouteManager.getIntent(context,ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}