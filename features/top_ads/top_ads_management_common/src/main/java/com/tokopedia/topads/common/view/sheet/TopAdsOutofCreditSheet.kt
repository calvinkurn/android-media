package com.tokopedia.topads.common.view.sheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_bottom_sheet_insufficient_credit.*

private const val CLICK_TAMBAH_KREDIT_TOPADS = "click-tambah kredit topads"
private const val TOPADS_OUT_CREDIT_BOTTOMSHEET = "out_of_credit_bottomsheet"

class TopAdsOutofCreditSheet : BottomSheetUnify() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.topads_create_bottom_sheet_insufficient_credit, null)
        setChild(contentView)
        showCloseIcon = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        ic_ilustration.setImageDrawable(view?.context?.getResDrawable(R.drawable.ill_isi_kredit_topads))
        subtitle?.text = getString(R.string.topads_common_out_of_credit_subtitle)
        btn_topup?.text = getString(R.string.topads_common_out_of_creadit_btn)
        btn_topup?.setOnClickListener {
            dismiss()
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TAMBAH_KREDIT_TOPADS, "")
            val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT)
            activity?.finish()
            startActivity(intent)
        }
        dismissBS?.setOnClickListener {
            dismiss()
            val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL).apply {
                if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                }
                putExtra(TopAdsCommonConstant.TOPADS_MOVE_TO_DASHBOARD, TopAdsCommonConstant.PARAM_PRODUK_IKLAN)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun show(
            fragmentManager: FragmentManager, ) {
        show(fragmentManager, TOPADS_OUT_CREDIT_BOTTOMSHEET)
    }
}

