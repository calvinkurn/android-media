package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.data.internal.AutoAdsStatus
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BERANDA_CREATION_SHEET
import com.tokopedia.topads.dashboard.databinding.AutoPsBerandaCreationModalLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.topads.dashboard.R as topadsdashboardR

class TopadsBerandaCreationBottomSheet : BottomSheetUnify() {

    private var binding : AutoPsBerandaCreationModalLayoutBinding? = null
    private var autoAdsData: AutoAdsResponse.TopAdsGetAutoAds.Data? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout(inflater,container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout(inflater: LayoutInflater, container: ViewGroup?) {
        binding = AutoPsBerandaCreationModalLayoutBinding.inflate(inflater,container,false)
        setChild(binding?.root)
        setTitle(getString(topadsdashboardR.string.topads_auto_ps_beranda_modal_title))
        showCloseIcon = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setClicks()
    }

    private fun initView(){
        autoAdsData?.let {
            if(it.status == AutoAdsStatus.STATUS_INACTIVE){
                binding?.autoPsDisabledState?.show()
                binding?.autoPsStateEnabled?.root?.gone()
            } else {
                binding?.autoPsDisabledState?.gone()
                binding?.autoPsStateEnabled?.root?.show()
                setAutoPsView(it)
            }
        }

        binding?.createShopAd?.createProductTitle?.text = getString(topadsdashboardR.string.topads_dash_headline_title)

    }

    private fun setAutoPsView(data: AutoAdsResponse.TopAdsGetAutoAds.Data) {
        binding?.autoPsStateEnabled?.desc?.text = "${getString(topadsdashboardR.string.topads_active)} • ${getString(topadsdashboardR.string.topads_dash_anggaran_harian)} Rp${data.dailyBudget}"
    }

    private fun setClicks(){
        binding?.autoPsStateEnabled?.cta?.setOnClickListener{
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
            )
            startActivity(intent)
        }

        binding?.createProduct?.submitBtn?.setOnClickListener {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER
            )
            startActivity(intent)
        }

        binding?.createShopAd?.submitBtn?.setOnClickListener {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION
            )
            startActivity(intent)
        }

        binding?.enableAutoPs?.autoPsCta?.setOnClickListener {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
            )
            startActivity(intent)
        }
    }

    fun show(fragmentManager: FragmentManager, autoAdsData: AutoAdsResponse.TopAdsGetAutoAds.Data?) {
        this.autoAdsData = autoAdsData
        show(fragmentManager, BERANDA_CREATION_SHEET)
    }
}
