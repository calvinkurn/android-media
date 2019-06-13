package com.tokopedia.power_merchant.subscribe.view.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel

import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.activity.TransitionPeriodPmActivity
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialBenefitPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialMemberPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialTncViewHolder
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.*
import kotlinx.android.synthetic.main.partial_member_power_merchant.*
import kotlinx.android.synthetic.main.partial_power_merchant_benefit.*
import kotlinx.android.synthetic.main.partial_tnc_power_merchant.*
import javax.inject.Inject

class PowerMerchantSubscribeFragment : BaseDaggerFragment(), PmSubscribeContract.View {

    @Inject
    lateinit var presenter: PmSubscribeContract.Presenter
    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    lateinit var partialMemberPmViewHolder: PartialMemberPmViewHolder
    lateinit var partialBenefitPmViewHolder: PartialBenefitPmViewHolder
    lateinit var partialTncViewHolder: PartialTncViewHolder


    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerPowerMerchantSubscribeComponent.builder().build().inject(this)
        presenter.attachView(this)
    }

    companion object {
        fun createInstance() = PowerMerchantSubscribeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePartialPart(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_power_merchant_subscribe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getPmInfo(userSessionInterface.shopId)

    }

    override fun onSuccessGetPmInfo(shopStatusModel: ShopStatusModel) {

        if (shopStatusModel.isTransitionPeriod()) {
            if (shopStatusModel.isPowerMerchantInactive()) {
                context?.let { TransitionPeriodPmActivity.newInstance(it) }
            } else if (shopStatusModel.isPowerMerchantActive()) {
                ticker_blue_container.visibility = View.VISIBLE
                renderView(shopStatusModel)
            }
        } else {
            renderView(shopStatusModel)
        }

    }

    private fun renderView(shopStatusModel: ShopStatusModel) {
        ticker_blue_container.visibility = View.GONE
        if (shopStatusModel.isAutoExtend()) {
            ticker_yellow_container.visibility = View.VISIBLE
            ll_footer_submit.visibility = View.VISIBLE
        } else {
            ticker_yellow_container.visibility = View.GONE
            ll_footer_submit.visibility = View.GONE
        }
        partialMemberPmViewHolder.renderPartialMember(shopStatusModel)
        partialTncViewHolder.renderPartialTnc()
    }

    private fun initializePartialPart(view: View) {
        if (!::partialMemberPmViewHolder.isInitialized) {
            partialMemberPmViewHolder = PartialMemberPmViewHolder.build(base_partial_member, activity)
        }
        if (!::partialTncViewHolder.isInitialized) {
            partialTncViewHolder = PartialTncViewHolder.build(base_partial_tnc, activity)
        }
        if (!::partialBenefitPmViewHolder.isInitialized) {
            partialBenefitPmViewHolder = PartialBenefitPmViewHolder.build(base_partial_benefit, activity)
        }
    }
}
