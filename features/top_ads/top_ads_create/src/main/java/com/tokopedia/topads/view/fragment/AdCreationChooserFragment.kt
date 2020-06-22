package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.auto.view.activity.EditBudgetAutoAdsActivity
import com.tokopedia.topads.auto.view.fragment.AutoAdsBaseBudgetFragment
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.data.response.AutoAdsResponse
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.AdChooserViewModel
import com.tokopedia.topads.view.sheet.ManualAdsConfirmationSheet
import kotlinx.android.synthetic.main.topads_create_ads_chooser_fragment.*
import javax.inject.Inject

class AdCreationChooserFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: AdChooserViewModel
    private var adStatus = 0
    private var dailyBudget = 0
    private var current_auto_ads_status = 0


    companion object {

        private const val TOGGLE_OFF = "toggle_off"
        private const val REQUEST_CODE = 0
        private const val ACTIVE = 500
        private const val NON_ACTIVE = 600
        private const val IN_PROGRESS_200 = 200
        private const val IN_PROGRESS_300 = 300
        private const val IN_PROGRESS_400 = 400
        private const val MANAUAL = 3
        private const val AUTO = 4
        private const val NO_ADS = 2
        private const val NO_PRODUCT = 1

        fun newInstance(): AdCreationChooserFragment {
            val args = Bundle()
            val fragment = AdCreationChooserFragment()
            fragment.arguments = args
            return fragment

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AdChooserViewModel::class.java)
        return inflater.inflate(resources.getLayout(R.layout.topads_create_ads_chooser_fragment), container, false)
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
        when (data.topAdsGetAutoAds.data.status) {
            ACTIVE -> setActiveStatus(R.string.ads_active, R.drawable.active_status_green)
            NON_ACTIVE -> setActiveStatus(R.string.ads_not_delivered, R.drawable.active_status_orange)
            else -> {
                tv_shop_status.text = ""
                tv_shop_status.setBackgroundResource(0)
            }
        }

        dailyBudget = data.topAdsGetAutoAds.data.dailyBudget
        current_auto_ads_status = data.topAdsGetAutoAds.data.status
        when (data.topAdsGetAutoAds.data.status) {
            IN_PROGRESS_200 -> inProgress()
            IN_PROGRESS_300 -> inProgress()
            IN_PROGRESS_400 -> inProgress()
            else -> autoAdsDisableConfirm()
        }
    }

    private fun setActiveStatus(adsActive: Int, bg: Int) {
        tv_shop_status.setText(adsActive)
        tv_shop_status.setBackgroundResource(bg)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.autoAdsData.observe(this, Observer {
            viewModel.getAutoAdsStatus(this::onSuccessAutoAds)

        })
        auto_ads.setOnClickListener {
            if (adStatus == AUTO) {
                val intent = Intent(context, EditBudgetAutoAdsActivity::class.java)
                intent.putExtra(AutoAdsBaseBudgetFragment.KEY_DAILY_BUDGET, dailyBudget)
                intent.putExtra(AutoAdsBaseBudgetFragment.KEY_AUTOADS_STATUS, current_auto_ads_status)
                startActivityForResult(intent, REQUEST_CODE)
            }
            if (adStatus == MANAUAL || adStatus == NO_ADS) {
                RouteManager.route(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
            }
        }
        manual_ads.setOnClickListener {
            if (adStatus == MANAUAL || adStatus == NO_ADS) {
                startActivity(Intent(activity, StepperActivity::class.java))
            }
            if (adStatus == AUTO) {
                ManualAdsConfirmationSheet.newInstance(activity!!, this::manualAdsClick).show()
            }
        }
    }

    private fun autoAdsDisableConfirm() {
        ticker_info.visibility = View.GONE
        auto_ads.isEnabled = true
        manual_ads.isEnabled = true
        auto_ads.alpha = 1f
        manual_ads.alpha = 1f
    }

    private fun inProgress() {
        ticker_info.visibility = View.VISIBLE
        auto_ads.isEnabled = false
        manual_ads.isEnabled = false
        auto_ads.alpha = 0.5f
        manual_ads.alpha = 0.5f
    }

    override fun getScreenName(): String {
        return AdCreationChooserFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    fun manualAdsClick() {
        viewModel.postAutoAds(TOGGLE_OFF, dailyBudget)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.getAutoAdsStatus(this::onSuccessAutoAds)

    }

}