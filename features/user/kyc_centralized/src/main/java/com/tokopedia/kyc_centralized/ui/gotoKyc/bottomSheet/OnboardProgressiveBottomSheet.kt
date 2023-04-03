package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.app.Activity
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
import com.tokopedia.kyc_centralized.common.getMessage
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycOnboardProgressiveBinding
import com.tokopedia.kyc_centralized.di.DaggerGoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycRouterFragment
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class OnboardProgressiveBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycOnboardProgressiveBinding>()

    private val startKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            KYCConstant.RESULT_FINISH -> {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OnboardProgressiveViewModel::class.java]
    }

    private var projectId = ""
    private var source = ""
    private var encryptedName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        arguments?.let {
            projectId = it.getString(PROJECT_ID).orEmpty()
            source = it.getString(SOURCE).orEmpty()
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
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserver()
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
        binding?.btnSubmit?.setOnClickListener {
            viewModel.registerProgressiveUseCase(projectId)
        }
    }

    private fun initObserver() {
        viewModel.registerProgressive.observe(viewLifecycleOwner) {
            when (it) {
                is RegisterProgressiveResult.Loading -> {
                    setButtonLoading(true)
                }
                is RegisterProgressiveResult.RiskyUser -> {
                    setButtonLoading(false)
                    val parameter = GotoKycMainParam(
                        projectId = projectId,
                        challengeId = it.challengeId
                    )
                    gotoDobChallenge(parameter)
                }
                is RegisterProgressiveResult.NotRiskyUser -> {
                    setButtonLoading(false)
                    val parameter = GotoKycMainParam(
                        sourcePage = source,
                        gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                        status = "0"
                    )
                    gotoStatusSubmissionPending(parameter)
                }
                is RegisterProgressiveResult.Failed -> {
                    setButtonLoading(false)
                    it.throwable?.let { throwable ->
                        showToasterError(throwable)
                    }
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

    private fun showToasterError(throwable: Throwable) {
        val message = throwable.getMessage(requireActivity())
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun setTokopediaCareView() {
        val message = getString(R.string.goto_kyc_question_ktp_issue)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToTokopediaCare()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isFakeBoldText = true
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
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

    companion object {
        private const val PATH_TOKOPEDIA_CARE = "help?lang=id?isBack=true"

        private const val PROJECT_ID = "project_id"
        private const val SOURCE = "source"
        private const val ENCRYPTED_NAME = "encrypted_name"

        fun newInstance(projectId: String, source: String, encryptedName: String) =
            OnboardProgressiveBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PROJECT_ID, projectId)
                    putString(SOURCE, source)
                    putString(ENCRYPTED_NAME, encryptedName)
                }
            }
    }

}
