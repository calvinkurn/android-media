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
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
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
                if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                }
            }
            startActivity(intent)
            activity?.finish()
        }
    }
}