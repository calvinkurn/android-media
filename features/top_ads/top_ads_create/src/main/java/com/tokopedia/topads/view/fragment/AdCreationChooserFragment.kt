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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
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
import com.tokopedia.topads.common.utils.TopadsCommonUtil.showErrorAutoAds
import com.tokopedia.topads.common.view.sheet.ManualAdsConfirmationCommonSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.AdChooserViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.DarkModeUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

private const val CLICK_MULAI_IKLAN = "click-mulai iklan otomatis"
private const val CLICK_BUAT_IKLAN_MANUAL = "click-buat iklan manual"
private const val CLICK_BUAT_IKLAN_MANUAL_ONBOARDING = "click - mulai iklan manual di onboarding"

class AdCreationChooserFragment : BaseDaggerFragment() {

    private var tickerInfo: Ticker? = null
    private var topCornerIcon1: ImageUnify? = null
    private var topCornerIcon2: ImageUnify? = null
    private var topCornerBg1: ImageUnify? = null
    private var topCornerBg2: ImageUnify? = null
    private var imageView7: ImageUnify? = null
    private var btnStartAutoAds: UnifyButton? = null
    private var btnStartManualAds: UnifyButton? = null
    private var btnLearnAutoAds: UnifyButton? = null
    private var btnLearnManualAds: UnifyButton? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: AdChooserViewModel
    private var adStatus = 0
    private var dailyBudget = 0

    companion object {

        private const val TOGGLE_OFF = "toggle_off"
        private const val AUTO_ADS_DISABLED = 111
        private const val AUTO = 4


        private const val EDU_AUTO_ADS_LINK = "https://seller.tokopedia.com/edu/tambah-iklan-otomatis-topads/"
        private const val EDU_MANUAL_ADS_LINK = "https://seller.tokopedia.com/edu/iklan-manual-baru/"

        fun newInstance(): AdCreationChooserFragment {
            val args = Bundle()
            val fragment = AdCreationChooserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(AdChooserViewModel::class.java)
        val view = inflater.inflate(context?.resources?.getLayout(R.layout.topads_create_fragment_onboarding),
            container, false)
        setUpView(view)
        return view
    }

    private fun setUpView(view: View) {
        tickerInfo = view.findViewById(R.id.ticker_info)
        topCornerIcon1 = view.findViewById(R.id.top_corner_icon1)
        topCornerIcon2 = view.findViewById(R.id.top_corner_icon2)
        topCornerBg1 = view.findViewById(R.id.top_corner_bg1)
        topCornerBg2 = view.findViewById(R.id.top_corner_bg2)
        imageView7 = view.findViewById(R.id.imageView7)
        btnStartAutoAds = view.findViewById(R.id.btn_start_auto_ads)
        btnStartManualAds = view.findViewById(R.id.btn_start_manual_ads)
        btnLearnAutoAds = view.findViewById(R.id.btn_learn_auto_ads)
        btnLearnManualAds = view.findViewById(R.id.btn_learn_manual_ads)

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
        tickerInfo?.tickerTitle = getString(R.string.topads_create_ticker_title_deactivate)
        tickerInfo?.setHtmlDescription(getString(R.string.topads_create_ticker_desc_deactiivate))
    }

    private fun inProgressActivate() {
        autoAdsEnableConfirm(false)
        tickerInfo?.tickerTitle = getString(R.string.topads_create_ticker_title_activate)
        tickerInfo?.setHtmlDescription(getString(R.string.topads_create_ticker_desc_actiivate))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.autoAdsData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> viewModel.getAutoAdsStatus(this::onSuccessAutoAds)
                is Fail -> it.throwable.message?.let { errorMessage ->
                    view.showErrorAutoAds(errorMessage)
                }
            }
        })

        if(activity?.isDarkMode() == true){
            imageView7?.setImageDrawable(view.context.getResDrawable(R.drawable.ill_header))
        }
        context?.let {
            topCornerBg1?.setImageDrawable(AppCompatResources.getDrawable(it,
                R.drawable.topads_create_bg_top_corner))
            topCornerBg2?.setImageDrawable(AppCompatResources.getDrawable(it,
                R.drawable.topads_create_bg_top_corner))
        }

        topCornerIcon1?.setImageResource(R.drawable.ic_iklan_otomatis)
        topCornerIcon2?.setImageResource(R.drawable.ic_iklan_manual)

        btnStartAutoAds?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_MULAI_IKLAN, "")
            if (adStatus == AUTO) {
                RouteManager.getIntent(it.context, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS)
                    .apply {
                        if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                            putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME,
                                getSellerMigrationFeatureName(activity?.intent?.extras))
                            putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA,
                                getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                        }
                        startActivityForResult(this, AUTO_ADS_DISABLED)
                    }
            } else {
                RouteManager.getIntent(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
                    .apply {
                        if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                            putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME,
                                getSellerMigrationFeatureName(activity?.intent?.extras))
                            putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA,
                                getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                        }
                        startActivity(this)
                    }
            }
        }

        btnStartManualAds?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUAT_IKLAN_MANUAL, "")
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateOnboardingEvent(
                CLICK_BUAT_IKLAN_MANUAL_ONBOARDING, "")
            if (adStatus == AUTO) {
                val sheet = ManualAdsConfirmationCommonSheet.newInstance()
                sheet.show(childFragmentManager, "")
                sheet.manualClick = {
                    viewModel.postAutoAds(TOGGLE_OFF, dailyBudget)
                }
            } else {
                Intent(activity, StepperActivity::class.java).apply {
                    if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                        putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME,
                            getSellerMigrationFeatureName(activity?.intent?.extras))
                        putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA,
                            getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                    }
                    startActivity(this)
                }
            }
        }

        btnLearnAutoAds?.setOnClickListener {
            activity?.let{
                RouteManager.route(it, ApplinkConstInternalGlobal.WEBVIEW, EDU_AUTO_ADS_LINK)
            }
        }

        btnLearnManualAds?.setOnClickListener {
            activity?.let{
                RouteManager.route(it, ApplinkConstInternalGlobal.WEBVIEW, EDU_MANUAL_ADS_LINK)
            }
        }
    }

    private fun autoAdsEnableConfirm(enable: Boolean) {
        if (enable)
            tickerInfo?.gone()
        else
            tickerInfo?.visible()
        btnStartAutoAds?.isEnabled = enable
        btnStartManualAds?.isEnabled = enable
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
