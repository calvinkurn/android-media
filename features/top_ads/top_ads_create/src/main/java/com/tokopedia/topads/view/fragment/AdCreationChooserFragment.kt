package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.*
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.topads.common.view.sheet.ManualAdsConfirmationCommonSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.AdChooserViewModel
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.*
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.view.*
import javax.inject.Inject

private const val CLICK_MULAI_IKLAN = "click-mulai iklan otomatis"
private const val CLICK_BUAT_IKLAN_MANUAL = "click-buat iklan manual"

class AdCreationChooserFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: AdChooserViewModel
    private var adStatus = 0
    private var dailyBudget = 0

    companion object {

        private const val TOGGLE_OFF = "toggle_off"
        private const val AUTO_ADS_DISABLED = 111
        private const val MANUAL = 3
        private const val AUTO = 4
        private const val NO_ADS = 2

        fun newInstance(): AdCreationChooserFragment {
            val args = Bundle()
            val fragment = AdCreationChooserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(AdChooserViewModel::class.java)
        return inflater.inflate(resources.getLayout(R.layout.topads_create_fragment_onboarding), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAdsState(this::onSuccess)
        viewModel.getAutoAdsStatus(this::onSuccessAutoAds)
    }

    private fun onSuccess(data: AdCreationOption) {
        adStatus = data.topAdsGetShopInfo.data.category
    }

    private fun onSuccessAutoAds(data: AutoAdsResponse) {
        dailyBudget = data.topAdsGetAutoAds.data.dailyBudget
        when (data.topAdsGetAutoAds.data.status) {
            STATUS_ACTIVE -> autoAdsEnableConfirm(true)
            STATUS_INACTIVE -> autoAdsEnableConfirm(true)
            STATUS_IN_PROGRESS_ACTIVE -> inProgressActivate()
            STATUS_IN_PROGRESS_INACTIVE -> inProgressDeactivate()
            else -> autoAdsEnableConfirm(true)
        }
    }

    private fun inProgressDeactivate() {
        autoAdsEnableConfirm(false)
        ticker_info?.tickerTitle = getString(R.string.topads_create_ticker_title_deactivate)
        ticker_info?.setHtmlDescription(getString(R.string.topads_create_ticker_desc_deactiivate))
    }

    private fun inProgressActivate() {
        autoAdsEnableConfirm(false)
        ticker_info?.tickerTitle = getString(R.string.topads_create_ticker_title_activate)
        ticker_info?.setHtmlDescription(getString(R.string.topads_create_ticker_desc_actiivate))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.autoAdsData.observe(viewLifecycleOwner, Observer {
            viewModel.getAutoAdsStatus(this::onSuccessAutoAds)

        })
        view.imageView7.setImageDrawable(view.context.getResDrawable(R.drawable.ill_header))
        context?.let {
            view.top_corner_bg1.setImageDrawable(AppCompatResources.getDrawable(it, R.drawable.topads_create_bg_top_corner))
            view.top_corner_bg2.setImageDrawable(AppCompatResources.getDrawable(it, R.drawable.topads_create_bg_top_corner))
        }

        view.top_corner_icon1.setImageResource(R.drawable.icon_otomatis_onboarding)
        view.icon2.setImageResource(R.drawable.topads_create_ic_checklist_blue)
        view.icon3.setImageResource(R.drawable.topads_create_ic_checklist_blue)
        view.icon4.setImageResource(R.drawable.topads_create_ic_checklist_blue)

        view.top_corner_icon2.setImageResource(R.drawable.icon_manual_onboarding)
        view.icon6.setImageResource(R.drawable.topads_create_ic_checklist_blue)
        view.icon7.setImageResource(R.drawable.topads_create_ic_checklist_blue)
        view.icon8.setImageResource(R.drawable.topads_create_ic_checklist_blue)


        btn_start_auto_ads?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_MULAI_IKLAN, "")
            if (adStatus == AUTO) {
                RouteManager.getIntent(it.context, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS).apply {
                    if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                        putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                        putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                    }
                    startActivityForResult(this, AUTO_ADS_DISABLED)
                }
            }
            if (adStatus == MANUAL || adStatus == NO_ADS) {
                RouteManager.getIntent(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE).apply {
                    if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                        putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                        putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                    }
                    startActivity(this)
                }
            }
        }

        btn_start_manual_ads?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUAT_IKLAN_MANUAL, "")
            if (adStatus == MANUAL || adStatus == NO_ADS) {
                Intent(activity, StepperActivity::class.java).apply {
                    if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                        putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                        putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                    }
                    startActivity(this)
                }
            } else if (adStatus == AUTO) {
                val sheet = ManualAdsConfirmationCommonSheet.newInstance()
                sheet.show(childFragmentManager, "")
                sheet.manualClick = {
                    viewModel.postAutoAds(TOGGLE_OFF, dailyBudget)
                }
            }
        }
    }

    private fun autoAdsEnableConfirm(enable: Boolean) {
        if (enable)
            ticker_info?.gone()
        else
            ticker_info?.visible()
        btn_start_auto_ads?.isEnabled = enable
        btn_start_manual_ads?.isEnabled = enable
    }

    override fun getScreenName(): String {
        return AdCreationChooserFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTO_ADS_DISABLED) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
        viewModel.getAutoAdsStatus(this::onSuccessAutoAds)
    }
}