package com.tokopedia.power_merchant.subscribe.view.fragment


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showEmptyState
import com.tokopedia.kotlin.extensions.view.showLoading

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
        root_view_pm.showLoading()
        ImageHandler.LoadImage(img_top_1, "https://ecs7.tokopedia.net/img/android/power_merchant_subscribe/pm_intro.png")
        initializePartialPart(view)
        partialTncViewHolder.renderPartialTnc()
        button_activate_root.setOnClickListener {
            if (getApprovalStatusPojo.kycStatus.kycStatusDetailPojo.status == 1) {
                    // go to activate power merchant
//                if (score < 65) {
//                    showDialogScore()
//                } else {
//                    // goto activate
//                }
            } else {
                setupDialogKyc()?.show()
            }
        }
        presenter.getPmStatusInfo(userSessionInterface.shopId)
    }

    private fun refreshData(){
        presenter.getPmStatusInfo(userSessionInterface.shopId)

    }

    private fun setupDialogKyc(): Dialog? {
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

    private fun setupDialogScore(): Dialog? {
        context?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_score_verification)

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
            renderViewTransitionPeriod()
        } else {
            renderViewNonTransitionPeriod()
        }
        root_view_pm.hideLoading()
    }

    private fun showError(message: String) {
        showError(message, null)
    }

    private fun renderViewNonTransitionPeriod() {
        if (shopStatusModel.isPowerMerchantIdle()) {
            //check score
        } else if (shopStatusModel.isPowerMerchantActive()) {
            if (isAutoExtend()) {
                hideButtonActivatedPm()
            } else {
                showExpiredDate()
            }
            partialMemberPmViewHolder.renderPartialMember(shopStatusModel,isAutoExtend())
        } else if (shopStatusModel.isPowerMerchantInactive()) {
            if (isAutoExtend()) {
                hideButtonActivatedPm()
            }
        }
        partialMemberPmViewHolder.renderPartialMember(shopStatusModel,isAutoExtend())

    }

    private fun renderViewTransitionPeriod() {
        if (shopStatusModel.isPowerMerchantInactive()) {
            if (isPmPending()) {
                ll_footer_submit.visibility = View.GONE
                ticker_yellow_container.visibility = View.VISIBLE
            } else {
                ticker_yellow_container.visibility = View.GONE
            }
        } else if (shopStatusModel.isPowerMerchantActive() or shopStatusModel.isPowerMerchantIdle()) {
            if (getApprovalStatusPojo.kycStatus.kycStatusDetailPojo.status != 1) {
                val intent = context?.let { TransitionPeriodPmActivity.newInstance(it) }
                startActivity(intent)
                activity?.finish()
            } else {
                if(isAutoExtend()){
                    hideButtonActivatedPm()
                }
                ticker_blue_container.visibility = View.VISIBLE
            }
        }
        partialMemberPmViewHolder.renderPartialMember(shopStatusModel,isAutoExtend())

    }


    private fun hideButtonActivatedPm() {
        ll_footer_submit.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun isAutoExtend(): Boolean {
        return shopStatusModel.isAutoExtend()
    }

    private fun isPmPending(): Boolean {
//        if(status == pending){
//            return true
//        }else {
//            return false
//        }
        return false
    }


    override fun onErrorGetPmInfo(throwable: Throwable) {
        showError(ErrorHandler.getErrorMessage(context, throwable))
        root_view_pm.showEmptyState(ErrorHandler.getErrorMessage(context, throwable),::refreshData)

    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        ToasterError.make(view, message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show()
    }

    private fun renderViewIsPmExtend(){
        hideButtonActivatedPm()
    }

    private fun renderViewIsPmNotExtend(){
        showExpiredDate()
    }


    private fun showExpiredDate(){
        ticker_yellow_container.visibility = View.VISIBLE
        val shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,
                DateFormatUtils.FORMAT_D_MMMM_YYYY,
                shopStatusModel.powerMerchant.expiredTime)
        txt_ticker_yellow.text = getString(R.string.expired_label,shopCloseUntilString)
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
