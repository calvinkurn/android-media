package com.tokopedia.kyc.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.otaliastudios.cameraview.controls.Facing;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentCardIdCamera extends KycBaseCameraFragment {
    public static String ACTION_CREATOR_ARG = "action_creator_arg";
    public static String ACTION_KEYS_PROVIDER_ARG = "action_keys_provider_arg";
    private ActionCreator actionCreator;
    private ActionDataProvider actionDataProvider;

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.CAMERA)
    public static BaseDaggerFragment newInstance() {
        return new FragmentCardIdCamera();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kyc_cardid_camera, container, false);
        ((ImageView) view.findViewById(R.id.iv_capture_image)).setImageResource(R.drawable.ic_button_capture);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListeners();

    }

    private void initView(View view) {
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
        if (!Utils.isFrontCameraAvailable()) {
            reverseCamera.setVisibility(View.GONE);
        }
        cameraLayout = view.findViewById(R.id.hc_camera_layout);
        headerText = view.findViewById(R.id.desc_1);
        cameraView.setFacing(Facing.BACK);
        cameraView.setZoom(0f);
    }

    @Override
    protected void onSuccessImageTakenFromCamera(File imgFile) {
        String imagePath = imgFile.getAbsolutePath();
        boolean toBeFlipped = false;
        hideLoading();
        cameraView.close();//always call this method if you do not want awkward issues
        getActivity().getSupportFragmentManager().popBackStack();
        if (actionCreator != null) {
            if (!TextUtils.isEmpty(imagePath)) {
                if (cameraView.getFacing().ordinal() == Facing.FRONT.ordinal()) {
                    toBeFlipped = true;
                }
                ArrayList<String> keysList = (ArrayList<String>) actionDataProvider.getData(1, null);
                HashMap<String, Object> dataMap = new HashMap<>();
                dataMap.put(keysList.get(0), imagePath);
                dataMap.put(keysList.get(1), toBeFlipped);

                actionCreator.actionSuccess(1, dataMap);
            } else {
                actionCreator.actionError(1, 101);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionCreator = (ActionCreator) getArguments().getSerializable(ACTION_CREATOR_ARG);
        actionDataProvider = (ActionDataProvider) getArguments().getSerializable(ACTION_KEYS_PROVIDER_ARG);
        getArguments().clear();
    }

    @Override
    public void onDestroy() {
        hideLoading();
        cameraView.close();
        super.onDestroy();
    }

}
