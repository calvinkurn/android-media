package com.tokopedia.homecredit.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otaliastudios.cameraview.Facing;
import com.tokopedia.homecredit.R;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity.HCI_KTP_IMAGE_PATH;

public class HomeCreditSelfieFragment extends HomeCreditBaseCameraFragment {
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.CAMERA)
    public static Fragment createInstance() {
        return new HomeCreditSelfieFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_credit_selfie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners();
        initViewListeners();
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
        cameraLayout = view.findViewById(R.id.hc_camera_layout);
        cameraView.setFacing(Facing.FRONT);
        cameraView.setZoom(0f);
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