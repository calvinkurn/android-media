package com.tokopedia.topads.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.topads.common.R
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
        super.onViewCreated(view, savedInstanceState)
        btn_go_to_dashboard.setOnClickListener {
            val intent =RouteManager.getIntent(context,ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL).apply {
                if (isFromPdpSellerMigration()) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName())
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks())
                }
            }
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun getSellerMigrationRedirectionApplinks(): ArrayList<String> {
        return ArrayList(activity?.intent?.getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA).orEmpty())
    }

    private fun getSellerMigrationFeatureName(): String {
        return activity?.intent?.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).orEmpty()
    }

    private fun isFromPdpSellerMigration(): Boolean {
        return !activity?.intent?.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).isNullOrEmpty()
    }
}