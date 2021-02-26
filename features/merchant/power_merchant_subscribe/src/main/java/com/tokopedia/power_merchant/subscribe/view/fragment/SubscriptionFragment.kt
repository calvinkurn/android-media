package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.Constant
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.PMFeatureAdapter
import kotlinx.android.synthetic.main.fragment_pm_communication_period.view.*
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

    override fun getResLayout(): Int = R.layout.fragment_pm_communication_period

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
}