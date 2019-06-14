package com.tokopedia.power_merchant.subscribe.view.fragment


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel

import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.activity.TransitionPeriodPmActivity
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantSuccessBottomSheet
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialBenefitPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialMemberPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialTncViewHolder
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo
import kotlinx.android.synthetic.main.dialog_kyc_verification.*
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.*
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.view.*


import javax.inject.Inject

class PowerMerchantSubscribeFragment : BaseDaggerFragment(), PmSubscribeContract.View {

    @Inject
    lateinit var presenter: PmSubscribeContract.Presenter
    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    lateinit var partialMemberPmViewHolder: PartialMemberPmViewHolder
    lateinit var partialBenefitPmViewHolder: PartialBenefitPmViewHolder
    lateinit var partialTncViewHolder: PartialTncViewHolder
    lateinit var shopStatusModel: ShopStatusModel
    lateinit var getApprovalStatusPojo: GetApprovalStatusPojo


    override fun getScreenName(): String = ""

    override fun initInjector() {
        val appComponent = (activity!!.application as BaseMainApplication).baseAppComponent

        activity?.let {
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(appComponent)
                    .build().inject(this)
        }
        presenter.attachView(this)
    }

    companion object {
        fun createInstance() = PowerMerchantSubscribeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_power_merchant_subscribe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImageHandler.LoadImage(img_top_1,"https://ecs7.tokopedia.net/img/android/power_merchant/pm_intro.png")
        initializePartialPart(view)
        setupYellowTicker()
        button_activate_root.setOnClickListener {
            if (getApprovalStatusPojo.kycStatus.kycStatusDetailPojo.status == 0) {
                setupDialog()?.show()
            } else {

            }
        }
        presenter.getPmStatusInfo(userSessionInterface.shopId)
    }

    private fun setupYellowTicker() {

    }

    private fun setupDialog(): Dialog? {
        context?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_kyc_verification)
            dialog.btn_submit_kyc.setOnClickListener {
                //                RouteManager.route(context, ApplinkConst.SELLER_SHIPPING_EDITOR)
//                activity.finish()
            }
            dialog.btn_close_kyc.setOnClickListener {
                dialog.hide()
            }
            return dialog
        }
        return null

    }

    fun showBottomSheetSuccess() {
        val bottomSheet = PowerMerchantSuccessBottomSheet();
        bottomSheet.show(childFragmentManager, "power_merchant_success")
    }


    fun showBottomSheetCancel() {
        val bottomSheet = PowerMerchantCancelBottomSheet()
        bottomSheet.show(childFragmentManager, "power_merchant_cancel")
    }

    override fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus) {
        shopStatusModel = powerMerchantStatus.shopStatusModel
        getApprovalStatusPojo = powerMerchantStatus.getApprovalStatusPojo

        if (shopStatusModel.isTransitionPeriod()) {
            if (shopStatusModel.isPowerMerchantInactive()) {
                ll_footer_submit.visibility = View.VISIBLE
                context?.let { TransitionPeriodPmActivity.newInstance(it) }
            } else if (shopStatusModel.isPowerMerchantActive()) {
                ticker_blue_container.visibility = View.VISIBLE
                ll_footer_submit.visibility = View.GONE
                renderView(shopStatusModel)
            }
        } else {
            renderView(shopStatusModel)
        }
    }

    private fun showError(message: String) {
        showError(message, null)
    }

    override fun onErrorGetPmInfo(throwable: Throwable) {
        showError(ErrorHandler.getErrorMessage(context, throwable))
    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        ToasterError.make(view, message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show()
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

    private fun initializePartialPart(view: View?) {
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
