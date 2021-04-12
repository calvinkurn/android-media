package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_NOT_FOUND
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.KycUrl
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.analytics.UserIdentificationAnalytics
import com.tokopedia.kyc_centralized.analytics.UserIdentificationAnalytics.Companion.createInstance
import com.tokopedia.kyc_centralized.di.DaggerUserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationInfoActivity
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyButton.Type.MAIN
import com.tokopedia.unifycomponents.UnifyButton.Variant.FILLED
import com.tokopedia.unifycomponents.UnifyButton.Variant.GHOST
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.KycCommonUrl
import javax.inject.Inject

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationInfoFragment : BaseDaggerFragment(), UserIdentificationInfoActivity.Listener {
    private var globalErrorView: GlobalError? = null
    private var image: ImageView? = null
    private var title: TextView? = null
    private var text: TextView? = null
    private var progressBar: View? = null
    private var containerMainView: View? = null
    private var mainView: View? = null
    private var button: UnifyButton? = null
    private var clReason: ConstraintLayout? = null
    private var reasonOne: TextView? = null
    private var reasonTwo: TextView? = null
    private var iconOne: View? = null
    private var iconTwo: View? = null
    private var isSourceSeller = false
    private var analytics: UserIdentificationAnalytics? = null
    private var statusCode = 0
    private var projectId = -1
    private var callback: String? = null
    private var kycBenefitLayout: View? = null
    private var kycBenefitImage: ImageUnify? = null
    private var kycBenefitPowerMerchant: ImageUnify? = null
    private var kycBenefitFintech: ImageUnify? = null
    private var kycBenefitShield: ImageUnify? = null
    private var kycBenefitButton: UnifyButton? = null
    private var kycBenefitCloseButton: ImageButton? = null
    private var defaultStatusBarColor = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(UserIdentificationViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_user_identification_info, container, false)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) defaultStatusBarColor = activity?.window?.statusBarColor ?: 0
        initView(parentView)
        return parentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isSourceSeller = arguments?.getBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER)?: false
            projectId = arguments?.getInt(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, KYCConstant.KYC_PROJECT_ID)?: KYCConstant.KYC_PROJECT_ID
            callback = arguments?.getString(ApplinkConstInternalGlobal.PARAM_CALL_BACK)
        }
        if (isSourceSeller) {
            goToFormActivity()
        }
        analytics = createInstance(projectId)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val daggerUserIdentificationComponent = DaggerUserIdentificationCommonComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
        daggerUserIdentificationComponent.inject(this)
    }

    private fun initView(parentView: View) {
        globalErrorView = parentView.findViewById(R.id.fragment_user_identification_global_error)
        containerMainView = parentView.findViewById(R.id.container_main_view)
        mainView = parentView.findViewById(R.id.main_view)
        image = parentView.findViewById(R.id.main_image)
        title = parentView.findViewById(R.id.title)
        text = parentView.findViewById(R.id.text)
        button = parentView.findViewById(R.id.button)
        progressBar = parentView.findViewById(R.id.progress_bar)
        clReason = parentView.findViewById(R.id.cl_reason)
        reasonOne = parentView.findViewById(R.id.txt_reason_1)
        reasonTwo = parentView.findViewById(R.id.txt_reason_2)
        iconOne = parentView.findViewById(R.id.ic_x_1)
        iconTwo = parentView.findViewById(R.id.ic_x_2)
        kycBenefitLayout = parentView.findViewById(R.id.layout_kyc_benefit)
        kycBenefitButton = parentView.findViewById(R.id.kyc_benefit_btn)
        kycBenefitCloseButton = parentView.findViewById(R.id.close_button)
        kycBenefitImage = parentView.findViewById(R.id.image_banner)
        kycBenefitPowerMerchant = parentView.findViewById(R.id.image_power_merchant)
        kycBenefitFintech = parentView.findViewById(R.id.image_fintech)
        kycBenefitShield = parentView.findViewById(R.id.image_shiled_star)
        setupKycBenefitImage()
        containerMainView?.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (projectId != KYCConstant.STATUS_DEFAULT) {
            statusInfo
        } else {
            toggleNotFoundView(true)
        }

        initObserver()
    }

    private fun initObserver() {
        viewModel.userProjectInfo.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    if(it.data.kycProjectInfo.status == KYCConstant.STATUS_BLACKLISTED ||
                            it.data.kycProjectInfo.statusName != null && it.data.kycProjectInfo.statusName == "") {
                        onUserBlacklist()
                    } else {
                        onSuccessGetUserProjectInfo(it.data.kycProjectInfo.status, it.data.kycProjectInfo.reasonList)
                    }
                }
                is Fail -> { onErrorGetUserProjectInfo(it.throwable) }
            }
        })
    }

    private val statusInfo: Unit
        get() {
            showLoading()
            viewModel.getUserProjectInfo(projectId)
        }

    private fun onUserBlacklist() {
        hideLoading()
        showStatusBlacklist()
    }

    private fun onSuccessGetUserProjectInfo(status: Int, reasons: List<String>) {
        hideLoading()
        hideKycBenefit()
        hideRejectedReason()
        statusCode = status
        when (status) {
            KYCConstant.STATUS_REJECTED -> showStatusRejected(reasons)
            KYCConstant.STATUS_APPROVED -> showStatusPending()
            KYCConstant.STATUS_PENDING -> showStatusPending()
            KYCConstant.STATUS_VERIFIED -> showStatusVerified()
            KYCConstant.STATUS_EXPIRED -> showStatusNotVerified()
            KYCConstant.STATUS_NOT_VERIFIED -> showStatusNotVerified()
            KYCConstant.STATUS_DEFAULT -> toggleNotFoundView(true)
            else -> onErrorGetUserProjectInfo(
                    MessageErrorException(String.format("%s (%s)",
                            getString(R.string.user_identification_default_request_error_unknown),
                            KYCConstant.ERROR_STATUS_UNKNOWN)))
        }
    }

    private fun onErrorGetUserProjectInfo(throwable: Throwable) {
        if (context != null) {
            hideLoading()
            val error = ErrorHandler.getErrorMessage(context, throwable)
            NetworkErrorHelper.showEmptyState(context, mainView, error) { statusInfo }
        }
    }

    private fun toggleNotFoundView(isVisible: Boolean) {
        if (isVisible) {
            mainView?.visibility = View.GONE
            globalErrorView?.setType(PAGE_NOT_FOUND)
            globalErrorView?.visibility = View.VISIBLE
            globalErrorView?.setActionClickListener { view: View? ->
                if (activity != null) {
                    RouteManager.route(context, ApplinkConst.HOME)
                }
            }
        } else {
            mainView?.visibility = View.VISIBLE
            globalErrorView?.visibility = View.GONE
        }
    }

    private fun showStatusNotVerified() {
        setStatusBar(com.tokopedia.unifyprinciples.R.color.Unify_T200)
        mainView?.hide()
        kycBenefitLayout?.show()
        kycBenefitButton?.setOnClickListener(onGoToFormActivityButton(KYCConstant.STATUS_NOT_VERIFIED))
        kycBenefitCloseButton?.setOnClickListener {
            activity?.onBackPressed()
        }
        analytics?.eventViewOnKYCOnBoarding()
    }

    private fun setupKycBenefitImage() {
        (kycBenefitImage as ImageView).loadImage(KycUrl.KYC_BENEFIT_BANNER)
        kycBenefitImage?.cornerRadius = 0
        (kycBenefitPowerMerchant as ImageView).loadImage(KycUrl.KYC_BENEFIT_POWER_MERCHANT)
        (kycBenefitFintech as ImageView).loadImage(KycUrl.KYC_BENEFIT_FINTECH)
        (kycBenefitShield as ImageView).loadImage(KycUrl.KYC_BENEFIT_SHIELD)
    }

    private fun setStatusBar(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity?.window ?: return
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = MethodChecker.getColor(context, color)
        }

        if(activity != null && activity is AppCompatActivity) {
              (activity as AppCompatActivity).supportActionBar?.hide()
        }
    }

    private fun restoreStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity?.window ?: return
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = if(defaultStatusBarColor.isZero()) {
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            } else {
                defaultStatusBarColor
            }
        }

        if(activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.show()
        }
    }

    private fun showStatusVerified() {
        ImageHandler.LoadImage(image, KycUrl.ICON_SUCCESS_VERIFY)
        title?.setText(R.string.kyc_verified_title)
        text?.setText(R.string.kyc_verified_text)
        if (callback == null) {
            button?.setText(R.string.kyc_verified_button)
            button?.setOnClickListener(onGoToTermsButton())
        } else {
            button?.setText(R.string.camera_next_button)
            button?.setOnClickListener(goToCallBackUrl(callback))
        }
        button?.buttonVariant = FILLED
        button?.buttonType = MAIN
        button?.visibility = View.VISIBLE
        analytics?.eventViewSuccessPage()
    }

    private fun showStatusPending() {
        ImageHandler.LoadImage(image, KycUrl.ICON_WAITING)
        title?.setText(R.string.kyc_pending_title)
        text?.setText(R.string.kyc_pending_text)
        if (callback == null) {
            button?.setText(R.string.kyc_pending_button)
            button?.setOnClickListener(onGoToAccountSettingButton(KYCConstant.STATUS_PENDING))
        } else {
            button?.setText(R.string.camera_next_button)
            button?.setOnClickListener(goToCallBackUrl(callback))
        }
        button?.buttonVariant = GHOST
        button?.visibility = View.VISIBLE
        button?.setOnClickListener(onGoToAccountSettingButton(KYCConstant.STATUS_PENDING))
        analytics?.eventViewPendingPage()
    }

    private fun showStatusRejected(reasons: List<String>) {
        ImageHandler.LoadImage(image, KycUrl.ICON_FAIL_VERIFY)
        title?.setText(R.string.kyc_failed_title)
        if (reasons.isNotEmpty()) {
            text?.setText(R.string.kyc_failed_text_with_reason)
            showRejectedReason(reasons)
        } else {
            text?.setText(R.string.kyc_failed_text)
        }
        button?.setText(R.string.kyc_failed_button)
        button?.visibility = View.VISIBLE
        button?.setOnClickListener(onGoToFormActivityButton(KYCConstant.STATUS_REJECTED))
        analytics?.eventViewRejectedPage()
    }

    private fun showRejectedReason(reasons: List<String>) {
        clReason?.visibility = View.VISIBLE
        reasonOne?.visibility = View.VISIBLE
        iconOne?.visibility = View.VISIBLE
        reasonOne?.text = reasons[0]
        if (reasons.size > 1) {
            reasonTwo?.visibility = View.VISIBLE
            iconTwo?.visibility = View.VISIBLE
            reasonTwo?.text = reasons[1]
        } else {
            reasonTwo?.visibility = View.GONE
            iconTwo?.visibility = View.GONE
        }
    }

    private fun hideRejectedReason() {
        clReason?.visibility = View.GONE
        reasonOne?.visibility = View.GONE
        iconOne?.visibility = View.GONE
        reasonTwo?.visibility = View.GONE
        iconTwo?.visibility = View.GONE
    }

    private fun hideKycBenefit() {
        activity?.actionBar?.show()
        restoreStatusBar()
        mainView?.show()
        kycBenefitLayout?.hide()
    }

    private fun showStatusBlacklist() {
        ImageHandler.LoadImage(image, KycUrl.ICON_FAIL_VERIFY)
        title?.setText(R.string.kyc_failed_title)
        text?.setText(R.string.kyc_blacklist_text)
        button?.setText(R.string.kyc_blacklist_button)
        button?.visibility = View.VISIBLE
        button?.setOnClickListener(onGoToAccountSettingButton(KYCConstant.STATUS_BLACKLISTED))
    }

    private fun showLoading() {
        mainView?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        mainView?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

    override fun onTrackBackPressed() {
        when (statusCode) {
            KYCConstant.STATUS_REJECTED -> analytics?.eventClickBackRejectedPage()
            KYCConstant.STATUS_PENDING -> analytics?.eventClickBackPendingPage()
            KYCConstant.STATUS_VERIFIED -> analytics?.eventClickBackSuccessPage()
            KYCConstant.STATUS_EXPIRED -> analytics?.eventClickOnBackOnBoarding()
            KYCConstant.STATUS_NOT_VERIFIED -> analytics?.eventClickOnBackOnBoarding()
            KYCConstant.STATUS_BLACKLISTED -> analytics?.eventClickBackBlacklistPage()
            else -> {
            }
        }
    }

    private fun onGoToFormActivityButton(status: Int): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            when (status) {
                KYCConstant.STATUS_NOT_VERIFIED -> analytics?.eventClickOnNextOnBoarding()
                KYCConstant.STATUS_REJECTED -> analytics?.eventClickNextRejectedPage()
                else -> {}
            }
            goToFormActivity()
        }
    }

    private fun onGoToAccountSettingButton(status: Int): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            when (status) {
                KYCConstant.STATUS_PENDING -> analytics?.eventClickOnButtonPendingPage()
                KYCConstant.STATUS_BLACKLISTED -> analytics?.eventClickOnButtonBlacklistPage()
            }
            activity?.finish()
        }
    }

    private fun goToFormActivity() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM, projectId.toString())
            startActivityForResult(intent, FLAG_ACTIVITY_KYC_FORM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLAG_ACTIVITY_KYC_FORM && resultCode == Activity.RESULT_OK) {
            statusInfo
            NetworkErrorHelper.showGreenSnackbar(activity, getString(R.string.text_notification_success_upload))
            analytics?.eventViewSuccessSnackbarPendingPage()
        } else if (requestCode == FLAG_ACTIVITY_KYC_FORM && resultCode == KYCConstant.USER_EXIT) {
            activity?.finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onGoToTermsButton(): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            analytics?.eventClickTermsSuccessPage()
            RouteManager.route(activity, KycCommonUrl.APPLINK_TERMS_AND_CONDITION)
        }
    }

    private fun goToCallBackUrl(callback: String?): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            if (callback != null) {
                RouteManager.route(activity, callback)
                activity?.finish()
            }
        }
    }

    companion object {
        private const val FLAG_ACTIVITY_KYC_FORM = 1301
        fun createInstance(isSourceSeller: Boolean, projectid: Int, callback: String?): UserIdentificationInfoFragment {
            val fragment = UserIdentificationInfoFragment()
            val args = Bundle()
            args.putBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER, isSourceSeller)
            args.putInt(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectid)
            args.putString(ApplinkConstInternalGlobal.PARAM_CALL_BACK, callback)
            fragment.arguments = args
            return fragment
        }
    }
}