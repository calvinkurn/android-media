package com.tokopedia.gopay.kyc.presentation.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.di.GoPayKycComponent
import com.tokopedia.gopay.kyc.presentation.activity.GoPayReviewResultActivity.Companion.KTP_PATH
import com.tokopedia.gopay.kyc.presentation.activity.GoPayReviewResultActivity.Companion.SELFIE_KTP_PATH
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseFragment
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycNavigationListener
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycReviewResultListener
import com.tokopedia.gopay.kyc.utils.ReviewCancelDialog
import com.tokopedia.gopay.kyc.viewmodel.GoPayKycImageUploadViewModel
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_gopay_review_layout.*
import java.io.File
import java.lang.IllegalStateException
import javax.inject.Inject

class GoPayReviewAndUploadFragment : GoPayKycBaseFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: GoPayKycImageUploadViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(GoPayKycImageUploadViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_review_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImagePathInViewModel()
        initViews()
        initListeners()
        observeViewModel()
    }

    private fun initViews() {
        setTncText()
        setImageFromFile(viewModel.ktpPath, ktpImage)
        setImageFromFile(viewModel.selfieKtpPath, ktpSelfieImage)
    }

    private fun setTncText() {
        val tncSpannableString = createTncTextSpannable()
        tncSpannableString?.let {
            tncText.text = it
            tncText.highlightColor =
                MethodChecker.getColor(context, android.R.color.transparent)
            tncText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun createTncTextSpannable(): SpannableStringBuilder? {
        val originalText = getString(R.string.gopay_kyc_accept_tnc_text)
        val startClickableIndex =
            originalText.indexOf(getString(R.string.gopay_kyc_accept_tnc_syarat_text))
        val endClickableIndex =
            originalText.indexOf(getString(R.string.gopay_kyc_accept_tnc_untuk_text))

        val spannableStringTnc = SpannableString(originalText)

        val color =
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        spannableStringTnc.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openHelpScreen()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
                ds.isFakeBoldText = true
            }
        }, startClickableIndex, endClickableIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        return SpannableStringBuilder.valueOf(spannableStringTnc)
    }

    private fun setImageFromFile(filePath: String, imageview: ImageView) {
        context?.let { Glide.with(it).load(File(filePath)).fitCenter().into(imageview) }
    }

    private fun setImagePathInViewModel() {
        arguments?.let {
            viewModel.ktpPath = it.getString(KTP_PATH, "")
            viewModel.selfieKtpPath = it.getString(SELFIE_KTP_PATH, "")
        }
    }

    private fun observeViewModel() {
        viewModel.uploadSuccessLiveData.observe(viewLifecycleOwner, { isKycUploaded ->
            sendKycButton.isLoading = false
            if (isKycUploaded) showKycSuccessScreen() else showKycErrorBottomSheet()
        })
        viewModel.kycSubmitErrorLiveData.observe(viewLifecycleOwner, {
            val toastType = if (it is IllegalStateException && it.message == GoPayKycImageUploadViewModel.ILLEGAL_STATE)
                Toaster.TYPE_NORMAL else Toaster.TYPE_ERROR
            showToastMessage(ErrorHandler.getErrorMessage(context, it), toastType)
        })
    }

    private fun initListeners() {
        retryKtpIcon.setOnClickListener { openKtpCameraScreen() }
        retryKtpText.setOnClickListener { openKtpCameraScreen() }
        retryKtpSelfieIcon.setOnClickListener { openSelfieKtpCameraScreen() }
        retryKtpSelfieText.setOnClickListener { openSelfieKtpCameraScreen() }
        sendKycButton.setOnClickListener {
            sendAnalytics(
                GoPayKycEvent.Click.UploadKycEvent(
                    GoPayKycConstants.ScreenNames.GOPAY_KYC_SUMMARY_PAGE
                )
            )
            sendKycButton.isLoading = true
            uploadPhotoForKyc()
        }
        tncCheckbox.setOnCheckedChangeListener { _, isChecked ->
            sendKycButton.isEnabled = isChecked
            sendKycButton.isClickable = isChecked
        }
        tncText.setOnClickListener { tncCheckbox.toggle() }
    }

    private fun openHelpScreen() {
        sendAnalytics(
            GoPayKycEvent.Click.TncEvent(
                GoPayKycConstants.ScreenNames.GOPAY_KYC_SUMMARY_PAGE
            )
        )
        RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, GOPAY_HELP_URL)
    }

    private fun uploadPhotoForKyc() = viewModel.initiateGoPayKyc()

    private fun openKtpCameraScreen() =
        activity?.let { (it as GoPayKycNavigationListener).openKtpCameraScreen() }

    private fun openSelfieKtpCameraScreen() =
        activity?.let { (it as GoPayKycNavigationListener).openSelfieKtpCameraScreen() }

    private fun showKycErrorBottomSheet() =
        activity?.let {
            (it as GoPayKycReviewResultListener).showKycFailedBottomSheet(
                viewModel.ktpPath,
                viewModel.selfieKtpPath
            )
        }

    private fun showKycSuccessScreen() =
        activity?.let { (it as GoPayKycReviewResultListener).showKycSuccessScreen() }

    fun updateKtpImage(ktpPath: String) {
        viewModel.ktpPath = ktpPath
        setImageFromFile(ktpPath, ktpImage)
    }

    fun updateSelfieKtpImage(selfieKtpPath: String) {
        viewModel.selfieKtpPath = selfieKtpPath
        setImageFromFile(selfieKtpPath, ktpSelfieImage)
    }

    private fun showToastMessage(message: String, toastType: Int) {
        if (message.isNotEmpty())
            Toaster.build(sendKycButton, message, Toaster.LENGTH_LONG, toastType)
                .show()
    }

    override fun getScreenName() = null
    override fun initInjector() = getComponent(GoPayKycComponent::class.java).inject(this)
    fun sendAnalytics(event: GoPayKycEvent) =
        activity?.let { (it as GoPayKycNavigationListener).sendAnalytics(event) }

    override fun handleBackPressForGopay() {
        sendAnalytics(
            GoPayKycEvent.Click.BackPressEvent(
                GoPayKycConstants.ScreenNames.GOPAY_KYC_SUMMARY_PAGE
            )
        )
        ReviewCancelDialog.showReviewDialog(requireContext(),
            {
                sendAnalytics(
                    GoPayKycEvent.Click.ConfirmOkDialogEvent(
                        "",
                        GoPayKycConstants.ScreenNames.GOPAY_KYC_SUMMARY_PAGE
                    )
                )
                uploadPhotoForKyc()
            }, {
                sendAnalytics(
                    GoPayKycEvent.Click.ExitKycDialogEvent(
                        "",
                        GoPayKycConstants.ScreenNames.GOPAY_KYC_SUMMARY_PAGE
                    )
                )
                activity?.let {
                    (it as GoPayKycNavigationListener).exitKycFlow()
                }
            })
    }

    override fun sendOpenScreenGopayEvent() {
        sendAnalytics(
            GoPayKycEvent.Impression.OpenScreenEvent(
                GoPayKycConstants.ScreenNames.GOPAY_KYC_SUMMARY_PAGE
            )
        )
    }

    companion object {
        const val GOPAY_HELP_URL = "https://www.go-pay.co.id/appterms"
        fun newInstance(bundle: Bundle?): GoPayReviewAndUploadFragment {
            val fragment = GoPayReviewAndUploadFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
