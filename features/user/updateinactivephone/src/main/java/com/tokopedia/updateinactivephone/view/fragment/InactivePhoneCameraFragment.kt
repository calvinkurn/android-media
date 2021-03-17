package com.tokopedia.updateinactivephone.view.fragment

import android.app.Activity
import android.os.Build
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
import com.tokopedia.updateinactivephone.view.InactivePhoneTracker
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.fragment_inactive_phone_camera_view.*
import java.io.File

class InactivePhoneCameraFragment : BaseDaggerFragment() {

    lateinit var tracker: InactivePhoneTracker

    private var mode = 0

    private var isPictureTaken = false

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_camera_view, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tracker = InactivePhoneTracker()

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

        btnFlipCamera?.setOnClickListener { _ ->
            cameraView?.toggleFacing()
        }

        btnShutter?.setOnClickListener {
            cameraViewModeCondition(
                    onIdCardMode = { tracker.clickOnCaptureButtonCameraViewIdCard() },
                    onSelfieMode = { tracker.clickOnCaptureButtonCameraViewSelfie() }
            )

            cameraView?.takePicture()
        }

        btnReCapture?.setOnClickListener {
            imgPreview?.let {
                ImageUtils.clearImage(it)
            }

            showCamera()
        }

        btnNext?.setOnClickListener {
            activity?.let {

                cameraViewModeCondition(
                        onIdCardMode = { tracker.clickOnNextButtonCameraViewIdCardConfirmation() },
                        onSelfieMode = { tracker.clickOnNextButtonCameraViewSelfiewConfirmation()}
                )

                it.setResult(Activity.RESULT_OK)
                it.finish()
            }
        }

        btnBack?.setOnClickListener {
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
        layoutCameraView?.layoutType = mode
        cameraViewModeCondition(
                onIdCardMode = { setLayoutCameraIdCard() },
                onSelfieMode = { setLayoutCameraSelfie() }
        )
    }

    private fun setLayoutCameraIdCard() {
        cameraView?.facing = Facing.BACK
        updateTitle(getString(R.string.text_title_id_card))
        updateDescription(getString((R.string.text_camera_description_id_card)))
        showCamera()
    }

    private fun setLayoutCameraSelfie() {
        cameraView?.facing = Facing.FRONT
        updateTitle(getString(R.string.text_title_selfie))
        updateDescription(getString((R.string.text_camera_description_selfie)))
        showCamera()
    }

    private fun updateTitle(title: String) {
        txtTitle?.text = title
    }

    private fun updateDescription(description: String) {
        txtDescription?.text = description
    }

    private fun showCamera(isSavingBook: Boolean = false) {
        imgPreview?.visibility = View.GONE
        layoutButtonPreview?.visibility = View.GONE
        cameraView?.visibility = View.VISIBLE
        btnShutter?.visibility = View.VISIBLE
        btnFlipCamera?.visibility = View.VISIBLE

        if (isSavingBook) {
            txtDescription?.visibility = View.GONE
            txtDescriptionSavingBook?.visibility = View.VISIBLE
        } else {
            txtDescription?.visibility = View.VISIBLE
            txtDescriptionSavingBook?.visibility = View.GONE
        }

        cameraView?.apply {
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
        imgPreview?.let {
            ImageUtils.loadImage(it, file.absolutePath)
            it.visibility = View.VISIBLE
        }

        layoutButtonPreview?.visibility = View.VISIBLE
        cameraView?.visibility = View.GONE
        btnShutter?.visibility = View.GONE
        btnFlipCamera?.visibility = View.GONE
    }

    private fun onSuccessTakePicture(pictureResult: PictureResult) {
        pictureResult.toBitmap { bitmap ->
            bitmap?.let {
                val file = convertBitmapToImageFile(it, 100, filePath())
                showPreview(file)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // https://stackoverflow.com/questions/43972053/cameraview-black-on-when-being-used-for-second-time/63629326#63629326
        cameraView?.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView?.destroy()
    }

    private fun filePath(): String {
        context?.let {
            return InactivePhoneConstant.filePath(it, mode)
        }

        return ""
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