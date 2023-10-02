package com.tokopedia.homecredit.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.FileCallback
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.engine.CameraEngine
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.homecredit.R
import com.tokopedia.homecredit.databinding.FragmentHomeCreditV2Binding
import com.tokopedia.homecredit.di.component.HomeCreditComponent
import com.tokopedia.homecredit.domain.model.CameraDetail
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.homecredit.domain.usecase.HomeCreditUseCase
import com.tokopedia.homecredit.utils.BitmapCropping
import com.tokopedia.homecredit.utils.BitmapProcessingListener
import com.tokopedia.homecredit.viewModel.HomeCreditViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
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
    private var bitmapCropping: BitmapCropping? = null
    private var originalBitmap: Bitmap? = null

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

        context?.let { context ->
            binding?.camera?.let { cameraView ->
                bitmapCropping = BitmapCropping(context, cameraView)
            }
        }

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
        setupOverlayConstraint(cameraDetail.type)
    }

    private fun setCutout(@DrawableRes res: Int?) {
        context?.let { context ->
            binding?.cameraCutoutSelfie?.shouldShowWithAction(res != null) {
                res?.let {
                    binding?.cameraCutoutSelfie?.setImageDrawable(
                        ContextCompat.getDrawable(context, res)
                    )
                }
            }

            binding?.cameraCutoutKtp?.setBackgroundResource(R.drawable.cutout_ktp)
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun setupOverlayConstraint(type: String) {
        if (type == ApplinkConstInternalFintech.TYPE_KTP) {
            binding?.cameraConstraintLayout?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(it)
                constraintSet.connect(
                    binding?.cameraOverlayTop?.id ?: 0,
                    ConstraintSet.BOTTOM,
                    binding?.cameraCutoutKtp?.id ?: 0,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    binding?.cameraOverlayBottom?.id ?: 0,
                    ConstraintSet.TOP,
                    binding?.cameraCutoutKtp?.id ?: 0,
                    ConstraintSet.BOTTOM
                )
                constraintSet.applyTo(it)
            }
            binding?.cameraOverlayTop
        } else {
            binding?.cameraCutoutKtp?.hide()
            binding?.cameraOverlayLeft?.hide()
            binding?.cameraOverlayRight?.hide()
        }
    }

    private fun startCamera() {
        try {
            binding?.camera?.clearCameraListeners()
            cameraListener?.let { binding?.camera?.addCameraListener(it) }
            binding?.camera?.facing = cameraDetail.cameraFacing
            binding?.camera?.flash = Flash.OFF
            binding?.camera?.useDeviceOrientation = false
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

            @SuppressLint("PII Data Exposure")
            override fun onPictureTaken(result: PictureResult) {
                processPictureTaken(result)
            }
        }
    }

    private fun processPictureTaken(result: PictureResult) {
        try {
            result.toBitmap { bitmap ->
                originalBitmap = bitmap
                if (bitmap != null) {
                    bitmapCropping?.doCropping(
                        bitmap,
                        binding?.cameraCutoutKtp!!,
                        object : BitmapProcessingListener {
                            override fun onBitmapReady(croppedBitmap: Bitmap) {
                                val byteArray = bitmapToByteArray(croppedBitmap)

                                CameraUtils.writeToFile(byteArray, getFileLocationFromDirectory()) {
                                    finalCameraResultFilePath = it?.path ?: ""
                                    loadImagePreview()
                                    showCameraPreviewActionButtons()
                                    hideCameraProp()

                                    binding?.cameraCaptureButton?.isClickable = true
                                    reset()
                                    hideLoading()
                                }
                            }

                            override fun onFailed(
                                originalBitmap: Bitmap,
                                throwable: Throwable
                            ) {
                                reset()
                                hideLoading()
                            }
                        })
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getFileLocationFromDirectory(): File {
        val directory = ContextWrapper(context).getDir(HomeCreditUseCase.FOLDER_NAME, Context.MODE_PRIVATE)
        if (!directory.exists()) directory.mkdir()
        val imageName = System.currentTimeMillis().toString() + HomeCreditUseCase.FILE_EXTENSIONS
        return File(directory.absolutePath, imageName)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun observeViewModel() {
        homeCreditViewModel?.imageDetailLiveData?.observe(
            viewLifecycleOwner
        ) { imageDetail: Result<ImageDetail>? ->
            when (imageDetail) {
                is Success -> {
                    finalCameraResultFilePath = imageDetail.data.imagePath ?: ""
                    loadImagePreview()
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

    private fun loadImagePreview() {
        binding?.cameraPreview?.show()
        context?.let { context ->
            binding?.cameraPreview?.let { imageView ->
                Glide.with(context).load(originalBitmap).centerCrop().into(imageView)
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
