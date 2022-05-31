package com.tokopedia.topads.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton


private const val CLICK_TAMBAH_KREDIT_TOPADS = "click-tambah kredit topads"

class NoCreditFragment : TkpdBaseV4Fragment() {

    companion object {
        fun newInstance(): NoCreditFragment {
            val args = Bundle()
            val fragment = NoCreditFragment()
            fragment.arguments = args
            return fragment

        }
    }

    override fun getScreenName(): String {
        return NoCreditFragment::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_create_bottom_sheet_insufficient_credit),
            container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageUnify>(R.id.ic_ilustration)
            ?.setImageDrawable(view.context?.getResDrawable(R.drawable.ill_isi_kredit_topads))
        view.findViewById<UnifyButton>(R.id.btn_topup)?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TAMBAH_KREDIT_TOPADS,
                "")
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT)
            startActivity(intent)
        }
    }

}