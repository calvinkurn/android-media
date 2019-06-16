package com.tokopedia.power_merchant.subscribe.view.fragment


import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showEmptyState
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.power_merchant.subscribe.*
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantTermsActivity
import com.tokopedia.power_merchant.subscribe.view.activity.TransitionPeriodPmActivity
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantSuccessBottomSheet
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialBenefitPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialMemberPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialTncViewHolder
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.KYCConstant
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
    var shopScore: Int = 0
    var minScore: Int = 0
    var isSuccessActivatedPm: Boolean = false
    lateinit var bottomSheetSuccess: PowerMerchantSuccessBottomSheet
    lateinit var bottomSheetCancel: PowerMerchantCancelBottomSheet

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(appComponent)
                    .build().inject(this)
        }
        presenter.attachView(this)
    }

    companion object {
        fun createInstance() = PowerMerchantSubscribeFragment()
        const val ACTIVATE_INTENT_CODE = 123
        const val AUTOEXTEND_INTENT_CODE = 321
        const val MINIMUM_SCORE_ACTIVATE_REGULAR = 75
        const val MINIMUM_SCORE_ACTIVATE_IDLE = 65
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_power_merchant_subscribe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root_view_pm.showLoading()
        ImageHandler.LoadImage(img_top_1, IMG_URL_PM_INTRO)
        initializePartialPart(view)
        partialTncViewHolder.renderPartialTnc()

        button_activate_root.setOnClickListener {
            if (getApprovalStatusPojo.kycStatus.kycStatusDetailPojo.status == 1) {
                if (shopStatusModel.isPowerMerchantInactive()) {
                    if (shopScore < MINIMUM_SCORE_ACTIVATE_REGULAR) {
                        setupDialogScore()?.show()
                        return@setOnClickListener
                    }
                    val intent = context?.let { PowerMerchantTermsActivity.createIntent(it, ACTION_ACTIVATE) }
                    startActivityForResult(intent, ACTIVATE_INTENT_CODE)
                } else {
                    if (shopScore < MINIMUM_SCORE_ACTIVATE_IDLE) {
                        setupDialogScore()?.show()
                        return@setOnClickListener
                    }
                    val intent = context?.let { PowerMerchantTermsActivity.createIntent(it, ACTION_AUTO_EXTEND) }
                    startActivityForResult(intent, AUTOEXTEND_INTENT_CODE)
                }
            } else {
                setupDialogKyc()?.show()
            }
        }

        presenter.getPmStatusInfo(userSessionInterface.shopId)
    }

    private val onViewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.member_cancellation_button -> showBottomSheetCancel()
            else -> {

            }
        }
    }

    override fun cancelMembership() {
        bottomSheetCancel.dismiss()
        presenter.setAutoExtendOff(false)
    }


    override fun refreshData() {
        root_view_pm.showLoading()
        bottomSheetSuccess.dismiss()
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
                RouteManager.route(context, ApplinkConst.KYC_SELLER_DASHBOARD)
                activity?.finish()
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

    private fun showBottomSheetSuccess(shopStatusModel: ShopStatusModel) {
        val imgUrl = IMG_URL_BS_SUCCESS
        val bottomSheetModel: PowerMerchantSuccessBottomSheet.BottomSheetModel =
                if (shopStatusModel.isTransitionPeriod()) {
                    val headerString = getString(R.string.pm_label_bs_success_header_transition)
                    val descString = getString(R.string.pm_label_bs_success_desc_transition)
                    val btnString = getString(R.string.pm_label_bs_success_button_transition)
                    PowerMerchantSuccessBottomSheet.BottomSheetModel(headerString, descString, imgUrl, btnString)
                } else {
                    val headerString = getString(R.string.pm_label_bs_success_header)
                    val descString = getString(R.string.pm_label_bs_success_desc)
                    val btnString = getString(R.string.pm_label_bs_success_desc_transition)
                    PowerMerchantSuccessBottomSheet.BottomSheetModel(headerString, descString, imgUrl, btnString)
                }

        bottomSheetSuccess = PowerMerchantSuccessBottomSheet.newInstance(bottomSheetModel)
        bottomSheetSuccess.setListener(object : PowerMerchantSuccessBottomSheet.BottomSheetListener {
            override fun onButtonClicked() {
                refreshData()
            }
        })
        bottomSheetSuccess.show(childFragmentManager, "power_merchant_success")
    }

    fun showBottomSheetCancel() {
        bottomSheetCancel = PowerMerchantCancelBottomSheet()
        bottomSheetCancel.show(childFragmentManager, "power_merchant_cancel")
    }

    override fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus) {
        shopStatusModel = powerMerchantStatus.shopStatusModel
        getApprovalStatusPojo = powerMerchantStatus.getApprovalStatusPojo
        shopScore = powerMerchantStatus.shopScore.data?.data?.value ?: 0
        minScore = powerMerchantStatus.shopScore.data?.badgeScore ?: 0
        var isTransitionPeriod = shopStatusModel.isTransitionPeriod()
        if (isTransitionPeriod) {
            renderViewTransitionPeriod()
        } else {
            renderViewNonTransitionPeriod()
        }
        partialMemberPmViewHolder.renderPartialMember(shopStatusModel, isAutoExtend())
        isSuccessActivatedPm = true
        root_view_pm.hideLoading()

        if (isSuccessActivatedPm) {
            showBottomSheetSuccess(shopStatusModel)
        }

    }

    private fun renderViewNonTransitionPeriod() {
        var isPowerMerchant = shopStatusModel.isPowerMerchantIdle() or shopStatusModel.isPowerMerchantActive()
        if (isPowerMerchant) {
            if (isAutoExtend()) {
                hideButtonActivatedPm()
            } else {
                showExpiredDate()
            }
        }

    }

    private fun renderViewTransitionPeriod() {
        var isPowerMerchant = shopStatusModel.isPowerMerchantActive() or shopStatusModel.isPowerMerchantIdle()
        var isPending = shopStatusModel.isPowerMerchantPending()
        if (isPowerMerchant) {
            var isKyc = getApprovalStatusPojo.kycStatus.kycStatusDetailPojo.status != KYCConstant.STATUS_VERIFIED
            if (isKyc) {
                val intent = context?.let { TransitionPeriodPmActivity.newInstance(it) }
                startActivity(intent)
                activity?.finish()
            } else {
                if (isAutoExtend()) {
                    hideButtonActivatedPm()
                }
                ticker_blue_container.visibility = View.VISIBLE
            }
        } else if (isPending) {
            ticker_yellow_container.visibility = View.VISIBLE
            hideButtonActivatedPm()
        } else {
            // if inactive do nothing
            // default state: button activate is visible
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVATE_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            isSuccessActivatedPm = true
            refreshData()
        } else if (requestCode == AUTOEXTEND_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            isSuccessActivatedPm = true
            refreshData()
        }
    }

    override fun onErrorGetPmInfo(throwable: Throwable) {
    }

    override fun showEmptyState(throwable: Throwable) {
        root_view_pm.showEmptyState(ErrorHandler.getErrorMessage(context, throwable), ::refreshData)
        root_view_pm.hideLoading()
    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        ToasterError.make(view, message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show()
    }

    private fun showExpiredDate() {
        ticker_yellow_container.visibility = View.VISIBLE
        val shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,
                DateFormatUtils.FORMAT_D_MMMM_YYYY,
                shopStatusModel.powerMerchant.expiredTime)
        txt_ticker_yellow.text = getString(R.string.expired_label, shopCloseUntilString)
    }

    private fun initializePartialPart(view: View?) {
        if (!::partialMemberPmViewHolder.isInitialized) {
            partialMemberPmViewHolder = PartialMemberPmViewHolder.build(base_partial_member, onViewClickListener)
        }
        if (!::partialTncViewHolder.isInitialized) {
            partialTncViewHolder = PartialTncViewHolder.build(base_partial_tnc, activity)
        }
        if (!::partialBenefitPmViewHolder.isInitialized) {
            partialBenefitPmViewHolder = PartialBenefitPmViewHolder.build(base_partial_benefit, activity)
        }
    }
}
