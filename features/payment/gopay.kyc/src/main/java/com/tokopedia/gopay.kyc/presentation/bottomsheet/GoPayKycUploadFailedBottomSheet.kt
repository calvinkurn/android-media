package com.tokopedia.gopay.kyc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.di.GoPayKycComponent
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycNavigationListener
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycReviewResultListener
import com.tokopedia.gopay.kyc.viewmodel.GoPayKycImageUploadViewModel
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.gopay_kyc_success_failed_empty_layout.*
import kotlinx.android.synthetic.main.gopay_kyc_upload_failed_layout.*
import java.lang.IllegalStateException
import javax.inject.Inject

class GoPayKycUploadFailedBottomSheet : BottomSheetUnify() {
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: GoPayKycImageUploadViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(GoPayKycImageUploadViewModel::class.java)
    }

    private val childLayoutRes = R.layout.gopay_kyc_upload_failed_layout
    private var component: GoPayKycComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            viewModel.ktpPath = it.getString(KTP_PATH) ?: ""
            viewModel.selfieKtpPath = it.getString(SELFIE_KTP_PATH) ?: ""
        }
    }

    private fun initInjector() {
        component =
            GoPayKycComponent::class.java.cast((activity as (HasComponent<GoPayKycComponent>)).component)
        component?.inject(this) ?: dismiss()
    }

    private fun setDefaultParams() {
        setTitle(TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = getScreenHeight().toDp()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendImpressionEvent()
        deferredImage.loadRemoteImageDrawable(FAILED_IMAGE_NAME, FAILED_IMAGE_PATH)
        retryUploadButton.setOnClickListener {
            sentRetryEvent()
            retryUploadButton.isLoading = true
            viewModel.initiateGoPayKyc()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uploadSuccessLiveData.observe(viewLifecycleOwner, { isKycUploaded ->
            retryUploadButton.isLoading = false
            if (isKycUploaded) {
                showKycSuccessScreen()
                dismiss()
            }
        })
        viewModel.kycSubmitErrorLiveData.observe(viewLifecycleOwner, {
            val toastType = if (it is IllegalStateException && it.message == GoPayKycImageUploadViewModel.ILLEGAL_STATE)
                Toaster.TYPE_NORMAL else Toaster.TYPE_ERROR
            showToastMessage(ErrorHandler.getErrorMessage(context, it), toastType)
        })
    }

    private fun showToastMessage(message: String, toastType: Int) {
        view?.rootView?.let {
            if (message.isNotEmpty())
                Toaster.build(it, message, Toaster.LENGTH_LONG, toastType)
                    .show()
        }
    }

    private fun sendImpressionEvent() {
        val event = GoPayKycEvent.Impression.KycFailedImpression("")
        activity?.let { (it as GoPayKycNavigationListener).sendAnalytics(event) }
    }

    private fun sentRetryEvent() {
        val event = GoPayKycEvent.Click.RetrySubmitEvent("")
        activity?.let { (it as GoPayKycNavigationListener).sendAnalytics(event) }
    }

    private fun showKycSuccessScreen() =
        activity?.let { (it as GoPayKycReviewResultListener).showKycSuccessScreen() }

    companion object {
        const val TITLE = "Upgrade GoPay Plus"
        private const val TAG = "KycFailedBottomSheet"
        private const val KTP_PATH = "ktp_path"
        private const val SELFIE_KTP_PATH = "selfie_ktp_path"
        const val FAILED_IMAGE_NAME = "gopay_kyc_upload_failed.png"
        const val FAILED_IMAGE_PATH = "https://images.tokopedia.net/img/android/res/singleDpi/gopay_kyc_upload_failed.png"

        fun show(ktpPath: String, selfieKtpPath: String, childFragmentManager: FragmentManager) {
            val bundle = Bundle().apply {
                putString(KTP_PATH, ktpPath)
                putString(SELFIE_KTP_PATH, selfieKtpPath)
            }
            val fragment = GoPayKycUploadFailedBottomSheet()
            fragment.arguments = bundle
            fragment.show(childFragmentManager, TAG)
        }
    }
}