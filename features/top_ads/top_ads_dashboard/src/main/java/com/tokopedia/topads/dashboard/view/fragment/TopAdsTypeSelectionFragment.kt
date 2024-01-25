package com.tokopedia.topads.dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsTypeSelectionBinding
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AUTO_ADS_DISABLED

private const val click_iklan_toko = "click - iklan toko"
class TopAdsTypeSelectionFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsTypeSelectionBinding? = null
    private var userSession: UserSessionInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopadsTypeSelectionBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews(){
        context?.let { context ->
            userSession = UserSession(context)
            binding?.topadsAdCard1?.cardBg?.background = VectorDrawableCompat.create(context.resources, R.drawable.ic_topads_added_ads_bg, null)
            binding?.topadsAdCard1?.cardIcon?.setImageDrawable(context?.getResDrawable(R.drawable.ic_topads_added_ads_produk))
            binding?.topadsAdCard1?.cardTitle?.text = getString(R.string.topads_dashboard_ad_product_type_selection_title)
            binding?.topadsAdCard1?.cardSubtitle?.text = getString(R.string.topads_dashboard_ad_product_type_selection_subtitle)
            binding?.topadsAdCard1?.root?.setOnClickListener {
                val intent = RouteManager.getIntent(context,
                    ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER)
                startActivityForResult(intent, AUTO_ADS_DISABLED)
            }

            binding?.topadsAdCard2?.cardBg?.background = VectorDrawableCompat.create(context.resources, R.drawable.ic_topads_added_ads_bg, null)
            binding?.topadsAdCard2?.cardIcon?.setImageDrawable(context?.getResDrawable(R.drawable.ic_topads_added_ads_headline))
            binding?.topadsAdCard2?.cardTitle?.text = getString(R.string.topads_dashboard_ad_headline_type_selection_title)
            binding?.topadsAdCard2?.cardSubtitle?.text = getString(R.string.topads_dashboard_ad_headline_type_selection_subtitle)
            binding?.topadsAdCard2?.root?.setOnClickListener {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormClickEvent(
                    click_iklan_toko,
                    "{${userSession?.shopId}}",
                    userSession?.userId)
                RouteManager.route(context,
                    ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
            }
        }
    }


    companion object {
        fun newInstance(): TopAdsTypeSelectionFragment {
            return TopAdsTypeSelectionFragment()
        }
    }

    override fun getScreenName(): String {
        return TopAdsTypeSelectionFragment::class.java.name
    }

    override fun initInjector() {}
}
