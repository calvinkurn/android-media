package com.tokopedia.homecredit.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.homecredit.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Objects;

public class HomeCreditSelfieFragment extends BaseDaggerFragment {
    private CameraView cameraView;
    private CameraListener cameraListener;
    private boolean isCameraOpen;
    private ImageView buttonCancel;
    private ImageView flashControl;
    private ImageView imageCaptured;
    RelativeLayout cameraActionsRL;
    private LinearLayout pictureActionLL;
    private TextView retakePhoto;
    private TextView continueUpload;

    private byte[] capturedImageBitmap;
    private View reverseCamera;
    private View captureImage;

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.CAMERA)
    public static Fragment createInstance() {
        return new HomeCreditSelfieFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_credit_selfie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
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
        cameraView.setFacing(Facing.FRONT);
        cameraView.setZoom(0f);
    }


    private void initViewListeners() {

        cameraListener = new CameraListener() {

            @Override
            public void onCameraOpened(CameraOptions options) {
                isCameraOpen = true;
            }

            @Override
            public void onCameraClosed() {
                // super.onCameraClosed();
                isCameraOpen = false;
            }

            @Override
            public void onPictureTaken(byte[] imageByte) {
                try {
                    File file = new File(Environment.getExternalStorageDirectory(), Calendar.getInstance().getTime() + "_photo.jpg");
                    if (file.exists()) {
                        file.delete();
                    }

                    OutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(imageByte);
                    outputStream.close();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    if (bitmap != null) {
                        capturedImageBitmap = imageByte;
                        if (cameraView.getFacing() == Facing.FRONT) {
                            imageCaptured.setImageBitmap(ImageHandler.flip(ImageHandler.rotatedBitmap(bitmap, file.getAbsolutePath()), true, false));
                        } else {
                            imageCaptured.setImageBitmap(ImageHandler.rotatedBitmap(bitmap, file.getAbsolutePath()));
                        }

                        hideCameraProp();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        captureImage.setOnClickListener(v -> cameraView.capturePicture());

        flashControl.setOnClickListener(v -> {
            if (cameraView != null) {
                if (cameraView.getFlash() == Flash.TORCH) {
                    cameraView.setFlash(Flash.OFF);
                } else {
                    cameraView.setFlash(Flash.TORCH);
                }
            }
        });

        reverseCamera.setOnClickListener(v -> {
            if (cameraView.getFacing() == Facing.FRONT) {
                cameraView.setFacing(Facing.BACK);
            } else {
                cameraView.setFacing(Facing.FRONT);
            }
        });

        retakePhoto.setOnClickListener(v -> {
            capturedImageBitmap = null;
            initCameraProp();
        });

        continueUpload.setOnClickListener(v -> {

            Toast.makeText(getContext(), "Upload Image", Toast.LENGTH_SHORT).show();
            // TODO: 17/1/19 continue upload bitmap;
        });

        buttonCancel.setOnClickListener(v -> Objects.requireNonNull(getActivity()).finish());
        cameraView.addCameraListener(cameraListener);
    }

    private void initCameraProp() {
        cameraView.start();
        imageCaptured.setVisibility(View.GONE);
        cameraActionsRL.setVisibility(View.VISIBLE);
        pictureActionLL.setVisibility(View.GONE);
    }

    private void hideCameraProp() {
        cameraView.stop();
        imageCaptured.setVisibility(View.VISIBLE);
        cameraActionsRL.setVisibility(View.GONE);
        pictureActionLL.setVisibility(View.VISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();
        onVisible();
    }

    public void onVisible() {
        if (getActivity().isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String permission = Manifest.permission.CAMERA;
            if (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        try {
            cameraView.clearCameraListeners();
            cameraView.addCameraListener(cameraListener);
            cameraView.start();
        } catch (Throwable e) {
            // no-op
        }
    }
}