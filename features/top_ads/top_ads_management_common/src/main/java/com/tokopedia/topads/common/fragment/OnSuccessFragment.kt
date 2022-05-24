package com.tokopedia.topads.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.activity.EXTRA_BUTTON
import com.tokopedia.topads.common.activity.EXTRA_SUBTITLE
import com.tokopedia.topads.common.activity.EXTRA_TITLE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.PARAM_TOPADS_HOMEPAGE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.TOPADS_MOVE_TO_DASHBOARD
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class OnSuccessFragment : TkpdBaseV4Fragment() {

    companion object {
        fun newInstance(args: Bundle): OnSuccessFragment {
            val fragment = OnSuccessFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getScreenName(): String {
        return OnSuccessFragment::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_create_activity_success),
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageUnify>(R.id.ic_ilustration)
            ?.setImageDrawable(view.context?.getResDrawable(R.drawable.ill_success))
        view.findViewById<UnifyButton>(R.id.goToDashboard)?.setOnClickListener {
            val intent = RouteManager.getIntent(context,
                ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL).apply {
                if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME,
                        getSellerMigrationFeatureName(activity?.intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA,
                        getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                }
                putExtra(TOPADS_MOVE_TO_DASHBOARD,
                    arguments?.getInt(TOPADS_MOVE_TO_DASHBOARD, PARAM_TOPADS_HOMEPAGE))
            }
            startActivity(intent)
            activity?.finish()
        }
        arguments?.run {
            if (!getString(EXTRA_TITLE).isNullOrEmpty()) {
                view.findViewById<Typography>(R.id.title)?.text = getString(EXTRA_TITLE)
            }
            if (!getString(EXTRA_SUBTITLE).isNullOrEmpty()) {
                view.findViewById<Typography>(R.id.subtitle)?.text = getString(EXTRA_SUBTITLE)
            }
            if (!getString(EXTRA_BUTTON).isNullOrEmpty()) {
                view.findViewById<UnifyButton>(R.id.goToDashboard)?.text = getString(EXTRA_BUTTON)
            }
        }
    }
}