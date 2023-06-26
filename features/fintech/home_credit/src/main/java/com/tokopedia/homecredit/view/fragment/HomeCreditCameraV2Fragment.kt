package com.tokopedia.homecredit.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.homecredit.R
import com.tokopedia.homecredit.databinding.FragmentHomeCreditV2Binding
import com.tokopedia.homecredit.di.component.HomeCreditComponent
import com.tokopedia.homecredit.domain.model.CameraDetail
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.homecredit.viewModel.HomeCreditViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

class HomeCreditCameraV2Fragment(
    private val cameraDetail: CameraDetail
) : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentHomeCreditV2Binding>()
    private var cameraListener: CameraListener? = null
    private var isCameraOpen = false
    private var mCapturingPicture = false
    private var mCaptureNativeSize: Size? = null
    private var loaderDialog: LoaderDialog? = null
    private var finalCameraResultFilePath: String? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val homeCreditViewModel: HomeCreditViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(HomeCreditViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeCreditV2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupViews()
        initCameraListener()
    }

    private fun setupViews() {
        binding?.cameraIconSwitch?.setOnClickListener {
            if (mCapturingPicture) {
                return@setOnClickListener
            }
            binding?.camera?.toggleFacing()
        }

        binding?.cameraTitle?.text = cameraDetail.cameraTitle
        binding?.cameraTips?.text = cameraDetail.cameraTips

        binding?.cameraIconBack?.setOnClickListener {
            requireActivity().finish()
        }

        binding?.cameraCaptureButton?.setOnClickListener { capturePicture() }
        binding?.cameraRetakeButton?.setOnClickListener {
            hideCameraPreviewActionButtons()
            showCameraProp()
            startCamera()
        }
        binding?.cameraContinueButton?.setOnClickListener {
            val intent = Intent()
            intent.putExtra(
                ApplinkConstInternalFintech.FILE_PATH,
                finalCameraResultFilePath
            )
            intent.putExtra(
                ApplinkConstInternalFintech.TYPE,
                cameraDetail.type
            )
            if (activity != null) {
                requireActivity().setResult(Activity.RESULT_OK, intent)
                requireActivity().finish()
            }
        }

        setCutout(cameraDetail.cameraCutout)
    }

    private fun setCutout(@DrawableRes res: Int) {
        context?.let {
            binding?.cameraCutout?.setImageDrawable(
                ContextCompat.getDrawable(it, res)
            )
        }
    }

    private fun startCamera() {
        try {
            binding?.camera?.clearCameraListeners()
            cameraListener?.let { binding?.camera?.addCameraListener(it) }
            binding?.camera?.facing = cameraDetail.cameraFacing
            binding?.camera?.open()
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    private fun onVisible() {
        if (requireActivity().isFinishing) {
            return
        }
        val permission = Manifest.permission.CAMERA
        if (ActivityCompat.checkSelfPermission(requireContext(), permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        }
    }

    private fun initCameraListener() {
        cameraListener = object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                isCameraOpen = true
            }

            override fun onCameraClosed() {
                super.onCameraClosed()
                isCameraOpen = false
            }

            override fun onPictureTaken(result: PictureResult) {
                try {
                    generateImage(result.data)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun observeViewModel() {
        homeCreditViewModel?.imageDetailLiveData?.observe(
            viewLifecycleOwner
        ) { imageDetail: Result<ImageDetail>? ->
            when (imageDetail) {
                is Success -> {
                    finalCameraResultFilePath = imageDetail.data.imagePath ?: ""
                    loadImagePreview(imageDetail.data)
                    showCameraPreviewActionButtons()
                    hideCameraProp()
                }

                else -> {}
            }
            binding?.cameraCaptureButton?.isClickable = true
            reset()
            hideLoading()
        }
    }

    private fun generateImage(imageByte: ByteArray) {
        if (mCaptureNativeSize == null) {
            mCaptureNativeSize = binding?.camera?.pictureSize
        }
        homeCreditViewModel?.computeImageArray(imageByte, mCaptureNativeSize)
    }

    private fun capturePicture() {
        if (mCapturingPicture || !isCameraOpen) {
            return
        }
        showLoading()
        mCapturingPicture = true
        mCaptureNativeSize = binding?.camera?.pictureSize
        binding?.camera?.takePicture()
        binding?.cameraCaptureButton?.isClickable = false
    }

    private fun reset() {
        mCapturingPicture = false
        mCaptureNativeSize = null
    }

    private fun showLoading() {
        if (isAdded && context != null) {
            loaderDialog = LoaderDialog(requireContext()).apply {
                setLoadingText(getString(R.string.title_loading))
            }
            loaderDialog?.show()
        }
    }

    private fun hideLoading() {
        if (isAdded) {
            loaderDialog?.dialog?.dismiss()
            loaderDialog = null
        }
    }

    private fun hideCameraProp() {
        binding?.camera?.close()
        binding?.camera?.hide()
        binding?.cameraCaptureButton?.invisible()
        binding?.cameraIconSwitch?.invisible()
        binding?.cameraTips?.text = cameraDetail.cameraTipsReview
        binding?.cameraCaptureButton?.isClickable = false
        binding?.cameraIconSwitch?.isClickable = false
    }

    private fun showCameraProp() {
        binding?.camera?.close()
        binding?.camera?.show()
        binding?.cameraCaptureButton?.show()
        binding?.cameraIconSwitch?.show()
        binding?.cameraTips?.text = cameraDetail.cameraTips
        binding?.cameraCaptureButton?.isClickable = true
        binding?.cameraIconSwitch?.isClickable = true
        setCutout(cameraDetail.cameraCutout)
    }

    private fun loadImagePreview(data: ImageDetail) {
        binding?.cameraPreview?.show()
        context?.let { context ->
            binding?.cameraPreview?.let { imageView ->
                Glide.with(context).load(data.imagePath).centerCrop().into(imageView)
            }
        }
    }

    private fun showCameraPreviewActionButtons() {
        binding?.cameraContinueButton?.show()
        binding?.cameraRetakeButton?.show()
        setCutout(cameraDetail.cameraCutoutReview)
    }

    private fun hideCameraPreviewActionButtons() {
        binding?.cameraPreview?.hide()
        binding?.cameraContinueButton?.hide()
        binding?.cameraRetakeButton?.hide()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onPause() {
        super.onPause()
        if (isCameraOpen) {
            binding?.camera?.close()
        }
    }

    override fun initInjector() {
        getComponent(HomeCreditComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {
        @RequiresPermission(Manifest.permission.CAMERA)
        fun createInstance(
            cameraDetail: CameraDetail
        ): HomeCreditCameraV2Fragment {
            return HomeCreditCameraV2Fragment(cameraDetail)
        }
    }
}
