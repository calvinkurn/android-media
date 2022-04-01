package com.tokopedia.homecredit.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.controls.Facing
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.homecredit.R
import com.tokopedia.homecredit.applink.Constants
import com.tokopedia.homecredit.utils.Utils.isFrontCameraAvailable
import com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity

class HomeCreditKTPFragment : HomeCreditBaseCameraFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_credit_ktp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        initViewListeners()
    }

    private fun initViews(view: View) {
        cameraView = view.findViewById(R.id.camera)
        buttonCancel = view.findViewById(R.id.button_cancel)
        flashControl = view.findViewById(R.id.iv_flash_control)
        imageCaptured = view.findViewById(R.id.iv_image_captured)
        cameraActionsRL = view.findViewById(R.id.rl_camera_actions)
        pictureActionLL = view.findViewById(R.id.ll_captured_image_action)
        retakePhoto = view.findViewById(R.id.retake_photo)
        continueUpload = view.findViewById(R.id.continue_upload)
        captureImage = view.findViewById(R.id.iv_capture_image)
        reverseCamera = view.findViewById(R.id.iv_reverse_camera)
        if (!isFrontCameraAvailable) {
            reverseCamera?.visibility = View.GONE
        }
        cameraLayout = view.findViewById(R.id.hc_camera_layout)
        headerText = view.findViewById(R.id.desc_1)
        cameraView?.facing = Facing.BACK
        cameraView?.zoom = 0f
        cameraOverlayImage = view.findViewById(R.id.img_cutout)
        setCameraOverlayValue(cameraOverlayImage)
    }

    private fun setCameraOverlayValue(cameraOverlayImage: ImageView?) {
        if (activity != null) {
            val intent = requireActivity().intent
            val cameraType = intent.getStringExtra(Constants.CAMERA_TYPE)
            val cutOutImgUrl = intent.getStringExtra(Constants.CUST_OVERLAY_URL)
            val customHeaderText = intent.getStringExtra(Constants.CUST_HEADER)
            if (!TextUtils.isEmpty(customHeaderText)) {
                headerText?.text = customHeaderText
            }
            if (!TextUtils.isEmpty(cameraType)
                    && Constants.KTP_NO_OVERLAY.equals(cameraType, ignoreCase = true)) {
                cameraOverlayImage?.visibility = View.GONE
            } else if (!TextUtils.isEmpty(cutOutImgUrl)) {
                ImageHandler.loadImageAndCache(cameraOverlayImage, cutOutImgUrl)
            }
        }
    }

    private fun initViewListeners() {
        continueUpload?.setOnClickListener {
            val intent = Intent()
            intent.putExtra(
                HomeCreditRegisterActivity.HCI_KTP_IMAGE_PATH,
                finalCameraResultFilePath
            )
            if (activity != null) {
                requireActivity().setResult(Activity.RESULT_OK, intent)
                requireActivity().finish()
            }
        }
    }

    companion object {
        @SuppressLint("MissingPermission")
        @RequiresPermission(Manifest.permission.CAMERA)
        fun createInstance(): Fragment {
            return HomeCreditKTPFragment()
        }
    }
}