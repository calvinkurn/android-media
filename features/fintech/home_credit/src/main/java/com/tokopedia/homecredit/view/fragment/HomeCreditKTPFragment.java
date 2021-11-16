package com.tokopedia.homecredit.view.fragment;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity.HCI_KTP_IMAGE_PATH;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;

import com.otaliastudios.cameraview.controls.Facing;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.homecredit.R;
import com.tokopedia.homecredit.applink.Constants;
import com.tokopedia.homecredit.utils.Utils;

public class HomeCreditKTPFragment extends HomeCreditBaseCameraFragment {
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.CAMERA)
    public static Fragment createInstance() {
        return new HomeCreditKTPFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_credit_ktp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners();
        initViewListeners();
    }

    private void setCameraOverlayImage(ImageView cameraOverlayImg) {
        Intent intent = getActivity().getIntent();
        String cameraType = intent.getStringExtra(Constants.CAMERA_TYPE);
        String cutOutImgUrl = intent.getStringExtra(Constants.CUST_OVERLAY_URL);
        String customHeaderText = intent.getStringExtra(Constants.CUST_HEADER);

        if (!TextUtils.isEmpty(customHeaderText)) {
            headerText.setText(customHeaderText);
        }
        if (!TextUtils.isEmpty(cameraType) && Constants.KTP_NO_OVERLAY.equalsIgnoreCase(cameraType)) {
            cameraOverlayImg.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(cutOutImgUrl)) {
            ImageHandler.loadImageAndCache(cameraOverlayImg, cutOutImgUrl);
        }
    }

    private void initViews(View view) {
        cameraView = view.findViewById(R.id.camera);
        buttonCancel = view.findViewById(R.id.button_cancel);
        flashControl = view.findViewById(R.id.iv_flash_control);
        imageCaptured = view.findViewById(R.id.iv_image_captured);
        cameraActionsRL = view.findViewById(R.id.rl_camera_actions);
        pictureActionLL = view.findViewById(R.id.ll_captured_image_action);
        retakePhoto = view.findViewById(R.id.retake_photo);
        continueUpload = view.findViewById(R.id.continue_upload);
        captureImage = view.findViewById(R.id.iv_capture_image);
        reverseCamera = view.findViewById(R.id.iv_reverse_camera);
        if (Utils.INSTANCE.isFrontCameraAvailable()) {
            reverseCamera.setVisibility(View.GONE);
        }
        cameraLayout = view.findViewById(R.id.hc_camera_layout);
        headerText = view.findViewById(R.id.desc_1);
        cameraView.setFacing(Facing.BACK);
        cameraView.setZoom(0f);
        cameraOverlayImage = view.findViewById(R.id.img_cutout);
        setCameraOverlayImage(cameraOverlayImage);
    }


    private void initViewListeners() {
        continueUpload.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(HCI_KTP_IMAGE_PATH, finalCameraResultFilePath);
            if (getActivity() != null) {
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

}