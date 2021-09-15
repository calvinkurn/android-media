package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.di.GoPayKycComponent
import com.tokopedia.gopay_kyc.presentation.activity.GoPayReviewActivity.Companion.KTP_PATH
import com.tokopedia.gopay_kyc.presentation.activity.GoPayReviewActivity.Companion.SELFIE_KTP_PATH
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycOpenCameraListener
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycReviewListener
import com.tokopedia.gopay_kyc.utils.ReviewCancelDialog
import com.tokopedia.gopay_kyc.viewmodel.GoPayKycImageUploadViewModel
import kotlinx.android.synthetic.main.fragment_gopay_review_layout.*
import java.io.File
import javax.inject.Inject

class GoPayReviewAndUploadFragment : BaseDaggerFragment() {

    private var ktpImagePath = ""
    private var ktpSelfieImagePath = ""

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: GoPayKycImageUploadViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(GoPayKycImageUploadViewModel::class.java)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            ReviewCancelDialog.showReviewDialog(requireContext(), { uploadPhotoForKyc() }, {
                activity?.let {
                    (it as GoPayKycOpenCameraListener).exitKycFlow()
                }
            })
        }
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
        setImagePath()
        initViews()
        initListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uploadSuccessLiveData.observe(viewLifecycleOwner, { isKycUploaded ->
            sendKycButton.isLoading = false
            if (isKycUploaded) showKycSuccessScreen() else showKycErrorBottomSheet()
        })
    }

    private fun initListeners() {
        setupOnBackPressed()
        retryKtpIcon.setOnClickListener { openKtpCameraScreen() }
        retryKtpText.setOnClickListener { openKtpCameraScreen() }
        retryKtpSelfieIcon.setOnClickListener { openSelfieKtpCameraScreen() }
        retryKtpSelfieText.setOnClickListener { openSelfieKtpCameraScreen() }
        tncText.setOnClickListener { openHelpScreen() }
        sendKycButton.setOnClickListener {
            sendKycButton.isLoading = true
            uploadPhotoForKyc()
        }
    }

    private fun openHelpScreen() {
        RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, GOPAY_HELP_URL)
    }

    private fun uploadPhotoForKyc() = viewModel.initiateGoPayKyc()

    private fun openKtpCameraScreen() =
        activity?.let { (it as GoPayKycOpenCameraListener).openKtpCameraScreen() }

    private fun openSelfieKtpCameraScreen() =
        activity?.let { (it as GoPayKycOpenCameraListener).openSelfieKtpCameraScreen() }

    private fun showKycErrorBottomSheet()  =
        activity?.let { (it as GoPayKycReviewListener).showKycFailedBottomSheet() }

    private fun showKycSuccessScreen() =
        activity?.let { (it as GoPayKycReviewListener).showKycSuccessScreen() }


    private fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private fun initViews() {
        setImageFromFile(ktpImagePath, ktpImage)
        setImageFromFile(ktpSelfieImagePath, ktpSelfieImage)
    }

    private fun setImageFromFile(filePath: String, imageview: ImageView) {
        context?.let { Glide.with(it).load(File(filePath)).fitCenter().into(imageview) }
    }

    private fun setImagePath() {
        arguments?.let {
            ktpImagePath = it.getString(KTP_PATH, "")
            ktpSelfieImagePath = it.getString(SELFIE_KTP_PATH, "")
        }
    }

    override fun getScreenName() = null
    override fun initInjector() = getComponent(GoPayKycComponent::class.java).inject(this)

    fun updateKtpImage(ktpPath: String) {
        ktpImagePath = ktpPath
        setImageFromFile(ktpImagePath, ktpImage)
    }

    fun updateSelfieKtpImage(selfieKtpPath: String) {
        ktpSelfieImagePath = selfieKtpPath
        setImageFromFile(ktpSelfieImagePath, ktpSelfieImage)
    }

    companion object {
        const val GOPAY_HELP_URL = "http://www.go-pay.co.id/appterms"
        fun newInstance(bundle: Bundle?): GoPayReviewAndUploadFragment {
            val fragment = GoPayReviewAndUploadFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}