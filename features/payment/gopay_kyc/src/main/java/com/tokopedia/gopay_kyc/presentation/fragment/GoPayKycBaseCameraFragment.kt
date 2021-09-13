package com.tokopedia.gopay_kyc.presentation.fragment

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.di.GoPayKycComponent
import com.tokopedia.gopay_kyc.domain.data.CameraImageResult
import com.tokopedia.gopay_kyc.presentation.activity.GoPayKycActivity
import com.tokopedia.gopay_kyc.utils.ReviewCancelDialog
import com.tokopedia.gopay_kyc.viewmodel.GoPayKycViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

abstract class GoPayKycBaseCameraFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: GoPayKycViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(GoPayKycViewModel::class.java)
    }

    private var mCaptureNativeSize: Size? = null
    protected var cameraView: CameraView? = null
    protected var capturedImageView: ImageView? = null
    protected var shutterImageView: ImageUnify? = null
    protected var reverseCamera: IconUnify? = null
    protected var retakeButton: UnifyButton? = null
    protected var cameraControlLayout: Group? = null
    protected var reviewPhotoLayout: Group? = null
    protected var ktpInstructionText: Typography? = null
    protected var cameraLayout: FrameLayout? = null
    private var loader: LoaderDialog? = null

    abstract fun setCaptureInstruction()
    abstract fun setVerificationInstruction()
    abstract fun proceedToNextStep()

    private val cameraListener = object : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
            viewModel.isCameraOpen = true
        }

        override fun onCameraClosed() {
            super.onCameraClosed()
            viewModel.isCameraOpen = false
        }

        override fun onPictureTaken(result: PictureResult) {
            try {
                generateImage(result.data)
                viewModel.mCapturingPicture = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        viewModel.cameraImageResultLiveData.observe(viewLifecycleOwner, {
            context?.let { context -> loadImageFromBitmap(context, it) }
            hideCameraProp()
            resetCapture()
        })
    }

    protected fun getCapturedImagePath() = viewModel.getCapturedImagePath()

    private fun loadImageFromBitmap(context: Context, cameraImageResult: CameraImageResult) {
        val width = cameraImageResult.bitmapWidth
        val height = cameraImageResult.bitmapHeight
        val min: Int
        val max: Int
        if (width > height) {
            min = height
            max = width
        } else {
            min = width
            max = height
        }
        val loadFitCenter = min != 0 && max / min > 2
        capturedImageView?.let {
            if (loadFitCenter)
                Glide.with(context).load(cameraImageResult.compressedByteArray?.toByteArray())
                    .fitCenter()
                    .into(it)
            else Glide.with(context).load(cameraImageResult.compressedByteArray?.toByteArray())
                .into(it)
        }
    }

    private fun generateImage(data: ByteArray) {
        // process Photo
        if (mCaptureNativeSize == null)
            mCaptureNativeSize = cameraView?.pictureSize
        viewModel.processAndSaveImage(
            data,
            mCaptureNativeSize?.width ?: -1,
            mCaptureNativeSize?.height ?: -1,
            cameraView?.facing?.ordinal ?: 1
        )

    }

    private fun reInitCamera() {
        viewModel.canGoBack = true
        cameraView?.open()
        cameraLayout?.visible()
        capturedImageView?.gone()
        cameraControlLayout?.visible()
        reviewPhotoLayout?.gone()
        setCaptureInstruction()
    }

    private fun hideCameraProp() {
        viewModel.canGoBack = false
        cameraView?.close()
        capturedImageView?.visible()
        cameraControlLayout?.gone()
        reviewPhotoLayout?.visible()
        setVerificationInstruction()
    }

    fun initListeners() {
        shutterImageView?.setOnClickListener { capturePicture() }
        reverseCamera?.setOnClickListener { toggleCamera() }
        retakeButton?.setOnClickListener { reInitCamera() }
        cameraView?.addCameraListener(cameraListener)

    }

    private fun capturePicture() {
        if (viewModel.mCapturingPicture || !viewModel.isCameraOpen) return
        showLoading()
        viewModel.mCapturingPicture = true
        mCaptureNativeSize = cameraView?.pictureSize
        cameraView?.takePicture()
    }

    protected open fun toggleCamera() {
        if (viewModel.mCapturingPicture) return
        cameraView?.toggleFacing()
    }


    protected open fun showLoading() {
        context?.let {
            loader = LoaderDialog(it)
            loader?.setLoadingText(getString(R.string.gopay_kyc_image_capture_loading_text))
            loader?.dialog?.setOverlayClose(false)
            loader?.show()
        }
    }

    protected open fun hideLoading() {
        loader?.dialog?.dismiss()
        loader = null
    }

    private fun resetCapture() {
        viewModel.mCapturingPicture = false
        mCaptureNativeSize = null
        hideLoading()
    }

    private fun onVisible() {
        if (activity?.isFinishing == true) return
        context?.let {
            val permission = Manifest.permission.CAMERA
            if (ActivityCompat.checkSelfPermission(
                    it,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            }

        }

    }

    private fun startCamera() {
        try {
            cameraView?.clearCameraListeners();
            cameraView?.addCameraListener(cameraListener);
            cameraView?.open();
        } catch (e: Throwable) {
            e.printStackTrace();
        }
    }

    private fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.canGoBack)
                        activity?.finish()
                    else
                        ReviewCancelDialog.showReviewDialog(
                            requireContext(),
                            { proceedToNextStep() },
                            { exitKycFlow() }
                        )
                }
            })
    }

    private fun exitKycFlow() {
        context?.let {
            val intent = GoPayKycActivity.getIntent(it)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(GoPayKycActivity.IS_EXIT_KYC, true)
            startActivity(intent)
        }
    }

    @TargetApi(23)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAttachActivity(context)
    }

    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onDestroy() {
        hideLoading()
        cameraView?.close()
        super.onDestroy()
    }

    override fun getScreenName() = null
    override fun initInjector() = getComponent(GoPayKycComponent::class.java).inject(this)

}