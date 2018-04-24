package com.tokopedia.imagepicker.picker.camera;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentStateAdapter;
import com.github.florent37.camerafragment.widgets.CameraSettingsView;
import com.github.florent37.camerafragment.widgets.CameraSwitchView;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.ImageUtils;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerCameraFragment extends CameraFragment {
    private CameraSettingsView cameraSettingsView;
    private FlashSwitchView flashSwitchView;
    private RecordButton recordButton;
    private CameraSwitchView cameraSwitchView;

    private OnImagePickerCameraFragmentListener onImagePickerCameraFragmentListener;
    public interface OnImagePickerCameraFragmentListener {
        void onImageTaken(String filePath);
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission("android.permission.CAMERA")
    public static ImagePickerCameraFragment newInstance(Configuration configuration) {
        return (ImagePickerCameraFragment) CameraFragment.newInstance(new ImagePickerCameraFragment(), configuration);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStateListener(new CameraFragmentStateAdapter() {
            @Override
            public void onCurrentCameraBack() {
                cameraSwitchView.displayBackCamera();
            }

            @Override
            public void onCurrentCameraFront() {
                cameraSwitchView.displayFrontCamera();
            }

            @Override
            public void onFlashAuto() {
                flashSwitchView.displayFlashAuto();
            }

            @Override
            public void onFlashOn() {
                flashSwitchView.displayFlashOn();
            }

            @Override
            public void onFlashOff() {
                flashSwitchView.displayFlashOff();
            }

            @Override
            public void onCameraSetupForPhoto() {
                recordButton.displayPhotoState();
                flashSwitchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void shouldRotateControls(int degrees) {
                cameraSwitchView.setRotation(degrees);
                flashSwitchView.setRotation(degrees);
            }

            @Override
            public void onRecordStateVideoReadyForRecord() {
                recordButton.displayVideoRecordStateReady();
            }

            @Override
            public void onRecordStateVideoInProgress() {
                recordButton.displayVideoRecordStateInProgress();
            }

            @Override
            public void onRecordStatePhoto() {
                recordButton.displayPhotoState();
            }

        });

        setControlsListener(new CameraFragmentControlsAdapter() {
            @Override
            public void lockControls() {
                cameraSwitchView.setEnabled(false);
                recordButton.setEnabled(false);
                cameraSettingsView.setEnabled(false);
                flashSwitchView.setEnabled(false);
            }

            @Override
            public void unLockControls() {
                cameraSwitchView.setEnabled(true);
                recordButton.setEnabled(true);
                cameraSettingsView.setEnabled(true);
                flashSwitchView.setEnabled(true);
            }

            @Override
            public void allowCameraSwitching(boolean allow) {
                cameraSwitchView.setVisibility(allow ? View.VISIBLE : View.GONE);
            }

            @Override
            public void allowRecord(boolean allow) {
                recordButton.setEnabled(allow);
            }

        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cameraView = super.onCreateView(inflater, container, savedInstanceState);
        final ViewGroup containerView = (ViewGroup) inflater.inflate(R.layout.fragment_image_picker_camera, container, false);

        cameraSettingsView = containerView.findViewById(R.id.settings_view);
        flashSwitchView = containerView.findViewById(R.id.flash_switch_view);
        cameraSwitchView = containerView.findViewById(R.id.front_back_camera_switcher);

        recordButton = containerView.findViewById(R.id.record_button);

        ViewGroup contentContainer = containerView.findViewById(R.id.content_container);
        contentContainer.addView(cameraView, 0);
        return containerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraSettingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDialog();
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoOrCaptureVideo(
                        new CameraFragmentResultAdapter() {
                            @Override
                            public void onPhotoTaken(byte[] bytes, String filePath) {
                                onImagePickerCameraFragmentListener.onImageTaken(filePath);
                                onPause();
                                onResume();
                            }
                        },
                        ImageUtils.getTokopediaPublicDirectory().getAbsolutePath(),
                        ImageUtils.generateUniqueFileName());
            }
        });

        cameraSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCameraTypeFrontBack();
            }
        });
        flashSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlashMode();
            }
        });
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachActivity(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity);
        }
    }

    protected void onAttachActivity(Context context) {
        onImagePickerCameraFragmentListener = (OnImagePickerCameraFragmentListener) context;
    }

}
