package com.tokopedia.topads.dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsAutopsTypeSelectionBinding
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

private const val click_iklan_toko = "click - iklan toko"

class TopAdsAutoPsTypeSelectionFragment: BaseDaggerFragment() {

    private var binding: FragmentTopadsAutopsTypeSelectionBinding? = null
    private lateinit var userSession: UserSessionInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopadsAutopsTypeSelectionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews(){
        userSession = UserSession(context)
        binding?.topadsAdCard1?.autopsAdTypeTitle?.text = getString(R.string.topads_dash_iklan_produck)
        binding?.topadsAdCard1?.subtitle?.text = getString(R.string.topads_ad_type_selection_product_subtitle)
        binding?.topadsAdCard1?.accordianText?.text = getString(R.string.topads_ad_type_selection_product_accordian_title)
        binding?.topadsAdCard1?.accordianText?.setOnClickListener { expandAccordian(binding?.topadsAdCard1?.accordianGroup) }
        binding?.topadsAdCard1?.accordianIcon?.setOnClickListener { expandAccordian(binding?.topadsAdCard1?.accordianGroup) }
        binding?.topadsAdCard1?.item1?.text = getString(R.string.topads_ad_type_selection_product_accordian_item1)
        binding?.topadsAdCard1?.item2?.text = getString(R.string.topads_ad_type_selection_product_accordian_item2)
        binding?.topadsAdCard1?.item3?.text = getString(R.string.topads_ad_type_selection_product_accordian_item3)
        binding?.topadsAdCard1?.image?.urlSrc = TopAdsDashboardConstant.IKLAN_PRODUCT_AD_TYPE_IMG_URL
        binding?.topadsAdCard1?.submit?.run {
            text = getString(R.string.topads_dashboard_create_product_advertisement)
            setOnClickListener {
                val intent = RouteManager.getIntent(
                    context,
                    ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE_MANUAL_ADS
                )
                startActivityForResult(intent, TopAdsDashboardConstant.AUTO_ADS_DISABLED)
            }
        }

        binding?.topadsAdCard2?.autopsAdTypeTitle?.text = getString(R.string.topads_dashboard_ad_headline_type_selection_title)
        binding?.topadsAdCard2?.subtitle?.text = getString(R.string.topads_ad_type_selection_shop_subtitle)
        binding?.topadsAdCard2?.accordianText?.text = getString(R.string.topads_ad_type_selection_shop_accordian_title)
        binding?.topadsAdCard2?.accordianText?.setOnClickListener { expandAccordian(binding?.topadsAdCard2?.accordianGroup) }
        binding?.topadsAdCard2?.accordianIcon?.setOnClickListener { expandAccordian(binding?.topadsAdCard2?.accordianGroup) }
        binding?.topadsAdCard2?.item1?.text = getString(R.string.topads_ad_type_selection_shop_accordian_item1)
        binding?.topadsAdCard2?.item2?.text = getString(R.string.topads_ad_type_selection_shop_accordian_item2)
        binding?.topadsAdCard2?.item3?.text = getString(R.string.topads_ad_type_selection_shop_accordian_item3)
        binding?.topadsAdCard2?.image?.urlSrc = TopAdsDashboardConstant.IKLAN_TOKO_AD_TYPE_IMG_URL
        binding?.topadsAdCard2?.submit?.run {
            text = getString(R.string.topads_dashboard_create_shop_advertisement)
            setOnClickListener {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormClickEvent(
                    click_iklan_toko,
                    "{${userSession.shopId}}",
                    userSession.userId
                )
                RouteManager.route(
                    context,
                    ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION
                )
            }
        }
    }

    private fun expandAccordian(accordianData: Group?){
        accordianData?.let {
            if (it.isVisible)
                it.gone()
            else
                it.show()
        }
    }

    companion object {
        fun newInstance(): TopAdsAutoPsTypeSelectionFragment {
            return TopAdsAutoPsTypeSelectionFragment()
        }
    }

    override fun getScreenName(): String {
        return TopAdsAutoPsTypeSelectionFragment::class.java.name
    }

    override fun initInjector() {}
}
