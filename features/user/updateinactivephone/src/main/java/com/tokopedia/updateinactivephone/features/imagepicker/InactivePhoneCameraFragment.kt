package com.tokopedia.updateinactivephone.features.imagepicker

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Mode
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.common.utils.convertBitmapToImageFile
import com.tokopedia.updateinactivephone.databinding.FragmentInactivePhoneCameraViewBinding
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.io.File

class InactivePhoneCameraFragment : BaseDaggerFragment() {

    private var viewBinding by autoClearedNullable<FragmentInactivePhoneCameraViewBinding>()
    val tracker = InactivePhoneTracker()

    private var mode = 0
    private var isPictureTaken = false

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentInactivePhoneCameraViewBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isPictureTaken = false
        arguments?.let {
            mode = it.getInt(KEY_MODE, 0)
            if (mode == 0) {
                activity?.finish()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutCameraView()

        viewBinding?.btnFlipCamera?.setOnClickListener {
            viewBinding?.cameraView?.toggleFacing()
        }

        viewBinding?.btnShutter?.setOnClickListener {
            cameraViewModeCondition(
                onIdCardMode = { tracker.clickOnCaptureButtonCameraViewIdCard() },
                onSelfieMode = { tracker.clickOnCaptureButtonCameraViewSelfie() }
            )

            viewBinding?.cameraView?.takePicture()
        }

        viewBinding?.btnReCapture?.setOnClickListener {
            viewBinding?.imgPreview?.let {
                ImageUtils.clearImage(it)
            }

            showCamera()
        }

        viewBinding?.buttonNext?.setOnClickListener {
            activity?.let {

                cameraViewModeCondition(
                    onIdCardMode = { tracker.clickOnNextButtonCameraViewIdCardConfirmation() },
                    onSelfieMode = { tracker.clickOnNextButtonCameraViewSelfiewConfirmation() }
                )

                it.setResult(Activity.RESULT_OK)
                it.finish()
            }
        }

        viewBinding?.btnBack?.setOnClickListener {
            cameraViewModeCondition(
                onIdCardMode = {
                    if (isPictureTaken) {
                        tracker.clickOnBackButtonCameraViewIdCardConfirmation()
                    } else {
                        tracker.clickOnBackButtonCameraViewIdCard()
                    }
                },
                onSelfieMode = {
                    if (isPictureTaken) {
                        tracker.clickOnBackButtonCameraViewSelfieConfirmation()
                    } else {
                        tracker.clickOnBackButtonCameraViewSelfiew()
                    }
                }
            )
            activity?.onBackPressed()
        }
    }

    private fun setLayoutCameraView() {
        viewBinding?.layoutCameraView?.layoutType = mode
        cameraViewModeCondition(
            onIdCardMode = { setLayoutCameraIdCard() },
            onSelfieMode = { setLayoutCameraSelfie() }
        )
    }

    private fun setLayoutCameraIdCard() {
        viewBinding?.cameraView?.facing = Facing.BACK
        updateTitle(getString(R.string.text_title_id_card))
        updateDescription(getString((R.string.text_camera_description_id_card)))
        showCamera()
    }

    private fun setLayoutCameraSelfie() {
        viewBinding?.cameraView?.facing = Facing.FRONT
        updateTitle(getString(R.string.text_title_selfie))
        updateDescription(getString((R.string.text_camera_description_selfie)))
        showCamera()
    }

    private fun updateTitle(title: String) {
        viewBinding?.textTitle?.text = title
    }

    private fun updateDescription(description: String) {
        viewBinding?.txtDescription?.text = description
    }

    private fun showCamera(isSavingBook: Boolean = false) {
        viewBinding?.imgPreview?.visibility = View.GONE
        viewBinding?.layoutButtonPreview?.visibility = View.GONE
        viewBinding?.cameraView?.visibility = View.VISIBLE
        viewBinding?.btnShutter?.visibility = View.VISIBLE
        viewBinding?.btnFlipCamera?.visibility = View.VISIBLE

        if (isSavingBook) {
            viewBinding?.txtDescription?.visibility = View.GONE
            viewBinding?.txtDescriptionSavingBook?.visibility = View.VISIBLE
        } else {
            viewBinding?.txtDescription?.visibility = View.VISIBLE
            viewBinding?.txtDescriptionSavingBook?.visibility = View.GONE
        }

        viewBinding?.cameraView?.apply {
            clearCameraListeners()
            if (isOpened) {
                close()
            }

            mode = Mode.PICTURE
            addCameraListener(listenerOnPictureTaken {
                onSuccessTakePicture(it)
            })
            open()
        }
    }

    private fun showPreview(file: File) {
        viewBinding?.imgPreview?.let {
            ImageUtils.loadImage(it, file.absolutePath)
            it.visibility = View.VISIBLE
        }

        viewBinding?.layoutButtonPreview?.visibility = View.VISIBLE
        viewBinding?.cameraView?.visibility = View.GONE
        viewBinding?.btnShutter?.visibility = View.GONE
        viewBinding?.btnFlipCamera?.visibility = View.GONE
    }

    private fun onSuccessTakePicture(pictureResult: PictureResult) {
        pictureResult.toBitmap { bitmap ->
            bitmap?.let {
                val file = convertBitmapToImageFile(it, 100, filePath().orEmpty())
                if (file.exists()) {
                    showPreview(file)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // https://stackoverflow.com/questions/43972053/cameraview-black-on-when-being-used-for-second-time/63629326# 63629326
        viewBinding?.cameraView?.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding?.cameraView?.destroy()
    }

    private fun filePath(): String? {
        return context?.let { InactivePhoneConstant.filePath(it, mode) }
    }

    private fun listenerOnPictureTaken(result: (PictureResult) -> Unit): CameraListener {
        return object : CameraListener() {
            override fun onPictureTaken(pictureResult: PictureResult) {
                result.invoke(pictureResult)
            }
        }
    }

    private fun onError(message: String) {
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun cameraViewModeCondition(onIdCardMode: () -> Unit = {}, onSelfieMode: () -> Unit = {}) {
        when (mode) {
            CameraViewMode.ID_CARD.id -> onIdCardMode.invoke()
            CameraViewMode.SELFIE.id -> onSelfieMode.invoke()
        }
    }

    companion object {
        private const val KEY_MODE = "mode"

        fun instance(mode: CameraViewMode): InactivePhoneCameraFragment {
            val bundle = Bundle()
            val fragment = InactivePhoneCameraFragment()
            bundle.putInt(KEY_MODE, mode.id)
            fragment.arguments = bundle
            return fragment
        }
    }
}