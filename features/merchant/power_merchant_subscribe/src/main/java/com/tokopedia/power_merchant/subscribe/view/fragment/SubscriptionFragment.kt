package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.Constant
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.PMFeatureAdapter
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.fragment_pm_subscription.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class SubscriptionFragment : BaseFragment() {

    companion object {
        fun createInstance(): SubscriptionFragment {
            return SubscriptionFragment()
        }
    }

    @Inject
    lateinit var pmFeatureAdapter: PMFeatureAdapter

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun getResLayout(): Int = R.layout.fragment_pm_subscription

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observePmStatusInfo()
        observeTickers()
    }

    private fun setupView() = view?.run {
        setupPmFeatures()

        val unsubscribeText = getString(R.string.power_merchant_cancel_membership)
        tvPmUnsubscribe.text = unsubscribeText.parseAsHtml()
        tvPmUnsubscribe.setOnClickListener {
            showUnsubscribeBottomSheet()
        }

        tvPmLabelFeatures.setBackgroundResource(R.drawable.bg_pm_feature_title)
    }

    private fun setupPmFeatures() = view?.rvPmFeatures?.run {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = pmFeatureAdapter

        pmFeatureAdapter.setItems(Constant.Subscription.getPmFeatureList(context))
        pmFeatureAdapter.notifyDataSetChanged()
    }

    private fun showUnsubscribeBottomSheet() {

    }

    private fun observeTickers() {
        view?.tickerPmCommunicationPeriod?.tickerTitle = "Ada perubahan pada Power Merchant"
        view?.tickerPmCommunicationPeriod?.setHtmlDescription("Perubahan Power Merchant mulai berlaku pada\n" +
                "1 Jan 2021. <a href=\"${Constant.Url.URL_FREE_SHIPPING_TERMS_AND_CONDITION}\">Pelajari Perubahan</a>")
        view?.tickerPmCommunicationPeriod?.tickerType = Ticker.TYPE_ANNOUNCEMENT
    }

    private fun observePmStatusInfo() {
        val pmStatusInfo = PowerMerchantStatus(

        )
        view?.viewPmStatus?.show(pmStatusInfo) {

        }
    }
}