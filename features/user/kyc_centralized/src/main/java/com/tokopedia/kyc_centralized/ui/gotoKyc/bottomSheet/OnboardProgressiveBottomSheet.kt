package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycOnboardProgressiveBinding
import com.tokopedia.kyc_centralized.di.DaggerGoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.router.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.router.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.router.GotoKycRouterFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.getGotoKycErrorMessage
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class OnboardProgressiveBottomSheet: BottomSheetUnify() {

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    private var binding by autoClearedNullable<LayoutGotoKycOnboardProgressiveBinding>()

    private var exhaustedParam = DobChallengeExhaustedParam()

    private var dismissDialogWithDataListener: (DobChallengeExhaustedParam) -> Unit = {}

    private val startKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                finishWithResult(Activity.RESULT_OK)
            }
            KYCConstant.ActivityResult.RESULT_FINISH -> {
                finishWithResult(Activity.RESULT_CANCELED)
            }
        }
    }

    private fun finishWithResult(result: Int) {
        kycSharedPreference.removeProjectId()
        activity?.setResult(result)
        activity?.finish()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OnboardProgressiveViewModel::class.java]
    }

    private var projectId = ""
    private var source = ""
    private var callback = ""
    private var encryptedName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        arguments?.let {
            projectId = it.getString(PROJECT_ID).orEmpty()
            source = it.getString(SOURCE).orEmpty()
            callback = it.getString(CALLBACK).orEmpty()
            encryptedName = it.getString(ENCRYPTED_NAME).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycOnboardProgressiveBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GotoKycAnalytics.sendViewKycOnboardingPage(
            projectId = projectId,
            kycFlowType = KYCConstant.GotoKycFlow.PROGRESSIVE
        )
        initView()
        initListener()
        initObserver()
        initUserConsent()
    }

    private fun initUserConsent() {
        val consentParam = ConsentCollectionParam(
            collectionId = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                KYCConstant.collectionIdGotoKycProgressiveStaging
            } else {
                KYCConstant.collectionIdGotoKycProgressiveProduction
            }
        )

        binding?.consentGotoKycProgressive?.load(
            lifecycleOwner = viewLifecycleOwner,
            viewModelStoreOwner = this,
            consentCollectionParam = consentParam
        )

        binding?.consentGotoKycProgressive?.setOnCheckedChangeListener { isChecked ->
            binding?.btnSubmit?.isEnabled = isChecked
        }
    }

    private fun initView() {
        setTokopediaCareView()
        binding?.apply {
            layoutDataKtp.tvNameKtp.text = encryptedName
            layoutDataKtp.imgItem.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_gopay)
            )
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.apply {
            setOnClickListener {
                if (!isLoading) {
                    GotoKycAnalytics.sendClickButtonPakaiKtpIni(
                        projectId = projectId,
                        kycFlowType = KYCConstant.GotoKycFlow.PROGRESSIVE
                    )
                    binding?.consentGotoKycProgressive?.submitConsent()
                    viewModel.registerProgressive(projectId)
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.registerProgressive.observe(viewLifecycleOwner) {
            when (it) {
                is RegisterProgressiveResult.Loading -> {
                    setButtonLoading(true)
                }
                is RegisterProgressiveResult.Exhausted -> {
                    setButtonLoading(false)
                    exhaustedParam.apply {
                        isExhausted = true
                        cooldownTimeInSeconds = it.cooldownTimeInSeconds
                        maximumAttemptsAllowed = it.maximumAttemptsAllowed
                    }
                    dismiss()
                }
                is RegisterProgressiveResult.RiskyUser -> {
                    setButtonLoading(false)
                    val parameter = GotoKycMainParam(
                        projectId = projectId,
                        challengeId = it.challengeId,
                        callback = callback
                    )
                    gotoDobChallenge(parameter)
                }
                is RegisterProgressiveResult.NotRiskyUser -> {
                    setButtonLoading(false)
                    val parameter = GotoKycMainParam(
                        projectId = projectId,
                        sourcePage = source,
                        gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                        status = it.status.toString(),
                        rejectionReason = it.rejectionReason,
                        callback = callback
                    )
                    gotoStatusSubmissionPending(parameter)
                }
                is RegisterProgressiveResult.Failed -> {
                    setButtonLoading(false)
                    showToasterError(it.throwable)
                }
            }
        }
    }

    private fun setButtonLoading(isLoading: Boolean) {
        binding?.btnSubmit?.isLoading = isLoading
    }

    private fun gotoDobChallenge(parameter: GotoKycMainParam) {
        val intent = Intent(requireActivity(), GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_DOB_CHALLENGE)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
    }

    private fun gotoStatusSubmissionPending(parameter: GotoKycMainParam) {
        val intent = Intent(requireActivity(), GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_STATUS_SUBMISSION)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
    }

    private fun showToasterError(throwable: Throwable?) {
        val message = throwable.getGotoKycErrorMessage(requireContext())
        binding?.root?.rootView?.apply {
            Toaster.build(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun setTokopediaCareView() {
        val message = getString(R.string.goto_kyc_question_ktp_issue)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    GotoKycAnalytics.sendClickOnButtonTokopediaCareOnboardingPage(
                        projectId = projectId,
                        kycFlowType = KYCConstant.GotoKycFlow.PROGRESSIVE
                    )
                    goToTokopediaCare()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isFakeBoldText = true
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                }
            },
            message.indexOf(getString(R.string.goto_kyc_contact_tokopedia_care)),
            message.length,
            0
        )
        binding?.tvTokopediaCare?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().WEB.plus(PATH_TOKOPEDIA_CARE)
        )
    }

    private fun initInjector() {
        DaggerGoToKycComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissDialogWithDataListener(exhaustedParam)

        GotoKycAnalytics.sendClickOnButtonCloseOnboardingBottomSheet(
            projectId = projectId,
            kycFlowType = KYCConstant.GotoKycFlow.PROGRESSIVE
        )
    }

    fun setOnDismissWithDataListener(exhaustedParam: (DobChallengeExhaustedParam) -> Unit) {
        dismissDialogWithDataListener = exhaustedParam
    }

    companion object {
        private const val PATH_TOKOPEDIA_CARE = "help/article/nama-yang-muncul-bukan-nama-saya?lang=id?isBack=true"

        private const val PROJECT_ID = "project_id"
        private const val SOURCE = "source"
        private const val CALLBACK = "callBack"
        private const val ENCRYPTED_NAME = "encrypted_name"

        fun newInstance(projectId: String, source: String, encryptedName: String, callback: String) =
            OnboardProgressiveBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PROJECT_ID, projectId)
                    putString(SOURCE, source)
                    putString(CALLBACK, callback)
                    putString(ENCRYPTED_NAME, encryptedName)
                }
            }
    }

}

data class DobChallengeExhaustedParam(
    var isExhausted: Boolean = false,
    var cooldownTimeInSeconds: String = "",
    var maximumAttemptsAllowed: String = ""
)
