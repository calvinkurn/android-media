package com.tokopedia.kyc_centralized.ui.tokoKyc.info

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.R as unifyR
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_REDIRECT_URL
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_NOT_FOUND
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.common.KycUrl
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.analytics.UserIdentificationAnalytics
import com.tokopedia.kyc_centralized.analytics.UserIdentificationAnalytics.Companion.createInstance
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycServerLogger
import com.tokopedia.kyc_centralized.common.KycStatus
import com.tokopedia.kyc_centralized.databinding.FragmentUserIdentificationInfoBinding
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.ui.customview.KycOnBoardingViewInflater
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.FailedSavePreferenceBottomSheet
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.UnifyButton.Type.MAIN
import com.tokopedia.unifycomponents.UnifyButton.Variant.FILLED
import com.tokopedia.unifycomponents.UnifyButton.Variant.GHOST
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationInfoFragment : BaseDaggerFragment(),
    UserIdentificationInfoActivity.Listener {

    private var viewBinding by autoClearedNullable<FragmentUserIdentificationInfoBinding>()
    private var isSourceSeller = false
    private var analytics: UserIdentificationAnalytics? = null
    private var statusCode: KycStatus = KycStatus.PENDING
    private var projectId = -1
    private var kycType = ""
    private var redirectUrl: String? = null
    private var defaultStatusBarColor = 0
    private var allowedSelfie = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )
    }
    private val viewModel by lazy { viewModelFragmentProvider.get(UserIdentificationViewModel::class.java) }

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val startLoginForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                saveInitDataToPreference()
            }
            else -> {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUserIdentificationInfoBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, unifyR.color.Unify_Background))
        }

        if (arguments != null) {
            isSourceSeller = arguments?.getBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER) ?: false
            projectId = arguments?.getInt(PARAM_PROJECT_ID) ?: KYCConstant.KYC_PROJECT_ID
            redirectUrl = arguments?.getString(PARAM_REDIRECT_URL).orEmpty()
            kycType = arguments?.getString(PARAM_KYC_TYPE).orEmpty()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        ActivityComponentFactory.instance
            .createActivityComponent(activity as Activity)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver(view)

        if (projectId != KycStatus.DEFAULT.code) {
            handleRequireLogin()
        } else {
            toggleNotFoundView(true)
        }
    }

    private fun handleRequireLogin() {
        if (userSessionInterface.isLoggedIn) {
            saveInitDataToPreference()
        } else {
            gotoLogin()
        }
    }

    private fun gotoLogin() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalUserPlatform.LOGIN
        )
        startLoginForResult.launch(intent)
    }

    private fun saveInitDataToPreference() {
        showLoading()
        val isSuccessSavePreference = kycSharedPreference.saveStringCache(
            key = KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE,
            value = KYCConstant.SharedPreference.VALUE_KYC_FLOW_TYPE_CKYC
        )

        KycServerLogger.sendLogStatusSavePreferenceKyc(
            flow = KycServerLogger.FLOW_CKYC,
            isSuccess = isSuccessSavePreference
        )

        if (isSuccessSavePreference) {
            if (isSourceSeller) {
                goToFormActivity()
            }
            val kycFlowType = kycSharedPreference.getStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)
            analytics = createInstance(projectId, kycFlowType)
            getStatusInfo()
        } else {
            showBlankLayout()
            showFailedSavePreferenceBottomSheet()
        }
    }

    private fun showFailedSavePreferenceBottomSheet() {
        val failedSavePreferenceBottomSheet = FailedSavePreferenceBottomSheet()

        failedSavePreferenceBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_FAILED_SAVE_PREFERENCE
        )

        failedSavePreferenceBottomSheet.setOnDismissWithDataListener { isReload ->
            if (isReload) {
                saveInitDataToPreference()
            } else {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    private fun initObserver(view: View) {
        viewModel.userProjectInfo.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    allowedSelfie = it.data.kycProjectInfo?.isSelfie == true
                    if (it.data.kycProjectInfo?.status == KycStatus.BLACKLISTED.code ||
                        it.data.kycProjectInfo?.statusName?.isEmpty() == true
                    ) {
                        onUserBlacklist()
                    } else {
                        onSuccessGetUserProjectInfo(
                            view,
                            it.data.kycProjectInfo?.status.orZero(),
                            it.data.kycProjectInfo?.reasonList.orEmpty()
                        )
                    }
                }
                is Fail -> {
                    onErrorGetUserProjectInfo(it.throwable)
                }
            }
        }
    }

    private fun loadUserConsent() {
        val consentParam = ConsentCollectionParam(
            collectionId = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                KYCConstant.consentCollectionIdStaging
            } else {
                KYCConstant.consentCollectionIdProduction
            }
        )
        viewBinding?.layoutKycBenefit?.userConsentKyc?.load(consentParam)

        viewBinding?.layoutKycBenefit?.kycBenefitBtn?.setOnClickListener {
            analytics?.eventClickOnNextOnBoarding()
            goToFormActivity()
            viewBinding?.layoutKycBenefit?.userConsentKyc?.submitConsent()
        }

        viewBinding?.layoutKycBenefit?.userConsentKyc?.setOnCheckedChangeListener { isChecked ->
            analytics?.eventClickKycTnc(isChecked)
            viewBinding?.layoutKycBenefit?.kycBenefitBtn?.isEnabled = isChecked
        }

        viewBinding?.layoutKycBenefit?.userConsentKyc?.setOnFailedGetCollectionListener { throwable ->
            Toast.makeText(context, throwable.message.orEmpty(), Toast.LENGTH_LONG).show()
        }
    }

    private fun getStatusInfo() {
        showLoading()
        loadUserConsent()
        viewModel.getUserProjectInfo(projectId)
    }

    private fun onUserBlacklist() {
        hideLoading()
        showStatusBlacklist()
    }

    private fun onSuccessGetUserProjectInfo(view: View, status: Int, reasons: List<String>) {
        hideLoading()
        hideKycBenefit()
        hideRejectedReason()

        KycStatus.map(status)?.let {
            statusCode = it
        }

        when (statusCode) {
            KycStatus.REJECTED -> showStatusRejected(reasons)
            KycStatus.APPROVED -> showStatusPending()
            KycStatus.PENDING -> showStatusPending()
            KycStatus.VERIFIED -> showStatusVerified()
            KycStatus.EXPIRED -> showStatusNotVerified(view)
            KycStatus.NOT_VERIFIED -> showStatusNotVerified(view)
            KycStatus.DEFAULT -> toggleNotFoundView(true)
            else -> onErrorGetUserProjectInfo(
                Throwable(
                    String.format(
                        Locale.getDefault(), "%s (%s)",
                        getString(R.string.user_identification_default_request_error_unknown),
                        KYCConstant.ERROR_STATUS_UNKNOWN
                    )
                )
            )
        }
    }

    private fun onErrorGetUserProjectInfo(throwable: Throwable) {
        if (context != null) {
            hideLoading()
            val error = ErrorHandler.getErrorMessage(context, throwable)
            NetworkErrorHelper.showEmptyState(context, viewBinding?.mainView, error) {
                getStatusInfo()
            }
        }
    }

    private fun toggleNotFoundView(isVisible: Boolean) {
        if (isVisible) {
            viewBinding?.mainView?.visibility = View.GONE
            viewBinding?.fragmentUserIdentificationGlobalError?.setType(PAGE_NOT_FOUND)
            viewBinding?.fragmentUserIdentificationGlobalError?.visibility = View.VISIBLE
            viewBinding?.fragmentUserIdentificationGlobalError?.setActionClickListener { view: View? ->
                if (activity != null) {
                    RouteManager.route(context, ApplinkConst.HOME)
                }
            }
        } else {
            viewBinding?.mainView?.visibility = View.VISIBLE
            viewBinding?.fragmentUserIdentificationGlobalError?.visibility = View.GONE
        }
    }

    private fun showStatusNotVerified(view: View) {
        KycOnBoardingViewInflater.setupKycBenefitToolbar(activity)
        viewBinding?.mainView?.hide()
        viewBinding?.layoutKycBenefit?.root?.show()
        KycOnBoardingViewInflater.setupKycBenefitView(requireActivity(), view, closeButtonAction = {
            activity?.onBackPressed()
        })
        analytics?.eventViewOnKYCOnBoarding()
    }

    private fun showStatusVerified() {
        viewBinding?.mainImage?.loadImage(KycUrl.ICON_SUCCESS_VERIFY)
        viewBinding?.title?.setText(R.string.kyc_verified_title)
        viewBinding?.text?.setText(R.string.kyc_verified_text)
        if (redirectUrl == null) {
            viewBinding?.button?.setText(R.string.kyc_verified_button)
            viewBinding?.button?.setOnClickListener(onGoToTermsButton())
        } else {
            viewBinding?.button?.setText(R.string.camera_next_button)
            viewBinding?.button?.setOnClickListener(goToCallBackUrl(redirectUrl))
        }
        viewBinding?.button?.buttonVariant = FILLED
        viewBinding?.button?.buttonType = MAIN
        viewBinding?.button?.visibility = View.VISIBLE
        analytics?.eventViewSuccessPage()
    }

    private fun showStatusPending() {
        hideRejectedReason()

        viewBinding?.mainImage?.loadImage(KycUrl.ICON_WAITING)
        viewBinding?.title?.setText(R.string.kyc_pending_title)
        viewBinding?.text?.setText(R.string.kyc_pending_text)
        if (redirectUrl == null) {
            viewBinding?.button?.setText(R.string.kyc_pending_button)
            viewBinding?.button?.setOnClickListener(onGoToAccountSettingButton(KycStatus.PENDING))
        } else {
            viewBinding?.button?.setText(R.string.camera_next_button)
            viewBinding?.button?.setOnClickListener(goToCallBackUrl(redirectUrl))
        }
        viewBinding?.button?.buttonVariant = GHOST
        viewBinding?.button?.visibility = View.VISIBLE
        analytics?.eventViewPendingPage()
    }

    private fun showStatusRejected(reasons: List<String>) {
        viewBinding?.mainImage?.loadImage(KycUrl.ICON_FAIL_VERIFY)
        viewBinding?.title?.setText(R.string.kyc_failed_title)
        if (reasons.isNotEmpty()) {
            viewBinding?.text?.setText(R.string.kyc_failed_text_with_reason)
            showRejectedReason(reasons)
        } else {
            viewBinding?.text?.setText(R.string.kyc_failed_text)
        }
        viewBinding?.button?.setText(R.string.kyc_failed_button)
        viewBinding?.button?.visibility = View.VISIBLE
        viewBinding?.button?.setOnClickListener(onGoToFormActivityButton())
        analytics?.eventViewRejectedPage()
    }

    private fun showRejectedReason(reasons: List<String>) {
        when (reasons.size) {
            REJECTED_REASON_SIZE_ONE -> {
                viewBinding?.icX1?.show()
                viewBinding?.txtReason1?.apply {
                    text = reasons[REJECTED_REASON_ONE]
                }?.show()

                viewBinding?.clReason?.show()
            }
            REJECTED_REASON_SIZE_TWO -> {
                viewBinding?.icX1?.show()
                viewBinding?.txtReason1?.apply {
                    text = reasons[REJECTED_REASON_ONE]
                }?.show()

                viewBinding?.icX2?.show()
                viewBinding?.txtReason2?.apply {
                    text = reasons[REJECTED_REASON_TWO]
                }?.show()

                viewBinding?.clReason?.show()
            }
            REJECTED_REASON_SIZE_THREE -> {
                viewBinding?.icX1?.show()
                viewBinding?.txtReason1?.apply {
                    text = reasons[REJECTED_REASON_ONE]
                }?.show()

                viewBinding?.icX2?.show()
                viewBinding?.txtReason2?.apply {
                    text = reasons[REJECTED_REASON_TWO]
                }?.show()

                viewBinding?.icX3?.show()
                viewBinding?.txtReason3?.apply {
                    text = reasons[REJECTED_REASON_THREE]
                }?.show()

                viewBinding?.clReason?.show()
            }
            REJECTED_REASON_SIZE_FOUR -> {
                viewBinding?.icX1?.show()
                viewBinding?.txtReason1?.apply {
                    text = reasons[REJECTED_REASON_ONE]
                }?.show()

                viewBinding?.icX2?.show()
                viewBinding?.txtReason2?.apply {
                    text = reasons[REJECTED_REASON_TWO]
                }?.show()

                viewBinding?.icX3?.show()
                viewBinding?.txtReason3?.apply {
                    text = reasons[REJECTED_REASON_THREE]
                }?.show()

                viewBinding?.icX4?.show()
                viewBinding?.txtReason4?.apply {
                    text = reasons[REJECTED_REASON_FOUR]
                }?.show()

                viewBinding?.clReason?.show()
            }
            else -> {
                viewBinding?.clReason?.hide()
            }
        }
    }

    private fun hideRejectedReason() {
        viewBinding?.clReason?.hide()
    }

    private fun hideKycBenefit() {
        activity?.actionBar?.show()
        KycOnBoardingViewInflater.restoreStatusBar(activity, defaultStatusBarColor)
        viewBinding?.mainView?.show()
        viewBinding?.layoutKycBenefit?.root?.hide()
    }

    private fun showStatusBlacklist() {
        viewBinding?.mainImage?.loadImage(KycUrl.ICON_FAIL_VERIFY)
        viewBinding?.title?.setText(R.string.kyc_failed_title)
        viewBinding?.text?.setText(R.string.kyc_blacklist_text)
        viewBinding?.button?.setText(R.string.kyc_blacklist_button)
        viewBinding?.button?.visibility = View.VISIBLE
        viewBinding?.button?.setOnClickListener(onGoToAccountSettingButton(KycStatus.BLACKLISTED))
    }

    private fun showBlankLayout() {
        viewBinding?.mainView?.hide()
        viewBinding?.layoutKycBenefit?.root?.hide()
        viewBinding?.progressBar?.hide()
    }

    private fun showLoading() {
        viewBinding?.mainView?.visibility = View.GONE
        viewBinding?.layoutKycBenefit?.root?.visibility = View.GONE
        viewBinding?.progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        viewBinding?.mainView?.visibility = View.VISIBLE
        viewBinding?.progressBar?.visibility = View.GONE
    }

    override fun onTrackBackPressed() {
        when (statusCode) {
            KycStatus.REJECTED -> analytics?.eventClickBackRejectedPage()
            KycStatus.PENDING -> analytics?.eventClickBackPendingPage()
            KycStatus.VERIFIED -> analytics?.eventClickBackSuccessPage()
            KycStatus.EXPIRED -> analytics?.eventClickOnBackOnBoarding()
            KycStatus.NOT_VERIFIED -> analytics?.eventClickOnBackOnBoarding()
            KycStatus.BLACKLISTED -> analytics?.eventClickBackBlacklistPage()
            else -> {
            }
        }
    }

    private fun onGoToFormActivityButton(): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            analytics?.eventClickNextRejectedPage()
            goToFormActivity()
        }
    }

    private fun onGoToAccountSettingButton(status: KycStatus): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            when (status) {
                KycStatus.PENDING -> analytics?.eventClickOnButtonPendingPage()
                KycStatus.BLACKLISTED -> analytics?.eventClickOnButtonBlacklistPage()
                else -> {}
            }
            kycSharedPreference.removeStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)
            activity?.finish()
        }
    }

    private fun goToFormActivity() {
        if (activity != null) {
            val intent = RouteManager.getIntent(
                activity,
                ApplinkConstInternalUserPlatform.KYC_FORM,
                projectId.toString(),
                redirectUrl
            )
            intent.putExtra(PARAM_KYC_TYPE, kycType)
            intent.putExtra(ALLOW_SELFIE_FLOW_EXTRA, allowedSelfie)
            startActivityForResult(intent, FLAG_ACTIVITY_KYC_FORM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLAG_ACTIVITY_KYC_FORM && resultCode == Activity.RESULT_OK) {
            hideKycBenefit()
            showStatusPending()
            analytics?.eventViewSuccessSnackbarPendingPage()
        } else if (requestCode == FLAG_ACTIVITY_KYC_FORM && resultCode == KYCConstant.USER_EXIT) {
            kycSharedPreference.removeStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)
            activity?.finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onGoToTermsButton(): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            analytics?.eventClickTermsSuccessPage()
            RouteManager.route(activity, KycUrl.APPLINK_TERMS_AND_CONDITION)
        }
    }

    private fun goToCallBackUrl(callback: String?): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            if (callback != null) {
                kycSharedPreference.removeStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)
                RouteManager.route(activity, callback)
                activity?.finish()
            }
        }
    }

    companion object {
        private const val FLAG_ACTIVITY_KYC_FORM = 1301
        private const val REJECTED_REASON_ONE = 0
        private const val REJECTED_REASON_TWO = 1
        private const val REJECTED_REASON_THREE = 2
        private const val REJECTED_REASON_FOUR = 3
        private const val REJECTED_REASON_SIZE_ONE = 1
        private const val REJECTED_REASON_SIZE_TWO = 2
        private const val REJECTED_REASON_SIZE_THREE = 3
        private const val REJECTED_REASON_SIZE_FOUR = 4

        const val ALLOW_SELFIE_FLOW_EXTRA = "allow_selfie_flow"
        private const val TAG_BOTTOM_SHEET_FAILED_SAVE_PREFERENCE = "bottom_sheet_failed_save_preference"
        fun createInstance(
            isSourceSeller: Boolean,
            projectid: Int,
            kycType: String = "",
            redirectUrl: String?
        ): UserIdentificationInfoFragment {
            return UserIdentificationInfoFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER, isSourceSeller)
                    putInt(PARAM_PROJECT_ID, projectid)
                    putString(PARAM_REDIRECT_URL, redirectUrl)
                    putString(PARAM_KYC_TYPE, kycType)
                }
            }
        }
    }
}
