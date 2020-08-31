package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Mode
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment.OnImagePickerCameraFragmentListener
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.permissionchecker.request
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.fragment_inactive_phone_camera_view.*
import java.io.File
import java.io.FileOutputStream

class InactivePhoneCameraFragment : BaseDaggerFragment() {

    private var mode = 0
    private val permissionCheckerHelper = PermissionCheckerHelper()
    private lateinit var onImagePickerCameraFragmentListener: OnImagePickerCameraFragmentListener

    override fun initInjector() {

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_camera_view, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mode = it.getInt(KEY_MODE, 0)
            if (mode == 0) {
                activity?.finish()
            }
        }

        activity?.let {
            permissionCheckerHelper.request(it, arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                    PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
            ), granted = {
            }, denied = {
                it.finish()
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutCameraView()

        btnFlipCamera?.setOnClickListener { _ ->
            cameraView?.toggleFacing()
        }

        btnShutter?.setOnClickListener {
            cameraView?.takePicture()
        }

        btnReCapture?.setOnClickListener {
            showCamera()
        }

        btnUploadData?.setOnClickListener {
            onImagePickerCameraFragmentListener.onImageTaken(filePath())
        }

        txtTitle?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setLayoutCameraView() {
        layoutCameraView?.layoutType = mode
        when (mode) {
            CameraViewMode.ID_CARD.id -> {
                cameraView?.facing = Facing.BACK
                updateTitle(getString(R.string.text_title_id_card))
                showCamera()
            }
            CameraViewMode.SELFIE.id -> {
                cameraView?.facing = Facing.FRONT
                updateTitle(getString(R.string.text_title_selfie))
                showCamera()
            }
            CameraViewMode.SAVING_BOOK.id -> {
                cameraView?.facing = Facing.BACK
                updateTitle(getString(R.string.text_title_saving_book))
                showCamera(true)
            }
        }
    }

    private fun updateTitle(title: String) {
        txtTitle?.text = title
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
        ImageUtils.clearImage(imgPreview)
        ImageUtils.loadImage(imgPreview, file.absolutePath)

        imgPreview?.visibility = View.VISIBLE
        layoutButtonPreview?.visibility = View.VISIBLE
        cameraView?.visibility = View.GONE
        btnShutter?.visibility = View.GONE
        btnFlipCamera?.visibility = View.GONE

    }

    private fun onSuccessTakePicture(pictureResult: PictureResult) {
        pictureResult.toBitmap {
            val file = writeImageToCache(it as Bitmap)
            showPreview(file)
        }
    }

    private fun writeImageToCache(bitmap: Bitmap): File {
        val file = File(filePath())
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    override fun onStop() {
        super.onStop()
        // https://stackoverflow.com/questions/43972053/cameraview-black-on-when-being-used-for-second-time/63629326#63629326

        cameraView?.let {
            if (it.isOpened) {
                it.close()
                it.destroy()
            }
        }
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

    override fun onAttachActivity(context: Context?) {
        onImagePickerCameraFragmentListener = context as OnImagePickerCameraFragmentListener
    }

    override fun getScreenName(): String = ""

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