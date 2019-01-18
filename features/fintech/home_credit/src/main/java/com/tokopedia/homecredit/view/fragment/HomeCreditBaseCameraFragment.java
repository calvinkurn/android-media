package com.tokopedia.homecredit.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Size;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.common.util.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class HomeCreditBaseCameraFragment extends BaseDaggerFragment {

    public CameraView cameraView;
    private boolean mCapturingPicture;
    public View reverseCamera;

    public CameraListener cameraListener;
    public boolean isCameraOpen;
    public TextView retakePhoto;
    public TextView continueUpload;
    public View captureImage;
    private int flashIndex;
    private List<Flash> supportedFlashList;
    public FrameLayout cameraLayout;
    public ImageView flashControl;
    public ImageView buttonCancel;

    private Size mCaptureNativeSize;
    public ImageView imageCaptured;
    RelativeLayout cameraActionsRL;
    public LinearLayout pictureActionLL;

    private ProgressDialog progressDialog;
    public String finalCameraResultFilePath;

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public void initialFlash() {
        supportedFlashList = new ArrayList<>();
        if (cameraView == null || cameraView.getCameraOptions() == null) {
            return;
        }
        Set<Flash> flashSet = cameraView.getCameraOptions().getSupportedFlash();
        for (Flash flash : flashSet) {
            if (flash != Flash.TORCH) {
                supportedFlashList.add(flash);
            }
        }
        if (supportedFlashList != null && supportedFlashList.size() > 0) {
            flashControl.setVisibility(View.VISIBLE);
            setCameraFlash();
        } else {
            flashControl.setVisibility(View.GONE);
        }
    }

    private void setCameraFlash() {
        if (supportedFlashList == null || flashIndex < 0 || supportedFlashList.size() <= flashIndex) {
            return;
        }
        Flash flash = supportedFlashList.get(flashIndex);
        if (flash.ordinal() == Flash.TORCH.ordinal()) {
            flashIndex = (flashIndex + 1) % supportedFlashList.size();
            flash = supportedFlashList.get(flashIndex);
        }
        cameraView.set(flash);
        setUIFlashCamera(flash.ordinal());
    }

    private void setUIFlashCamera(int flashEnum) {
        if (flashEnum == Flash.AUTO.ordinal()) {
            flashControl.setImageResource(com.tokopedia.imagepicker.R.drawable.ic_auto_flash);
        } else if (flashEnum == Flash.ON.ordinal()) {
            flashControl.setImageResource(com.tokopedia.imagepicker.R.drawable.ic_on_flash);
        } else if (flashEnum == Flash.OFF.ordinal()) {
            flashControl.setImageResource(com.tokopedia.imagepicker.R.drawable.ic_off_flash);
        }
    }

    public void initCameraProp() {
        cameraView.start();
        imageCaptured.setVisibility(View.GONE);
        cameraActionsRL.setVisibility(View.VISIBLE);
        pictureActionLL.setVisibility(View.GONE);
    }

    public void initListeners() {
        cameraListener = new CameraListener() {

            @Override
            public void onCameraOpened(CameraOptions options) {
                initialFlash();
                isCameraOpen = true;
            }

            @Override
            public void onCameraClosed() {
                super.onCameraClosed();
                isCameraOpen = false;
            }

            @Override
            public void onPictureTaken(byte[] imageByte) {
                try {
                    generateImage(imageByte);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        captureImage.setOnClickListener(v -> capturePicture());

        flashControl.setOnClickListener(v -> {
            if (supportedFlashList != null && supportedFlashList.size() > 0) {
                flashIndex = (flashIndex + 1) % supportedFlashList.size();
                setCameraFlash();
            }
        });

        reverseCamera.setOnClickListener(v -> {
            toggleCamera();
        });

        retakePhoto.setOnClickListener(v -> {
            initCameraProp();
        });

        buttonCancel.setOnClickListener(v -> Objects.requireNonNull(getActivity()).finish());

        cameraView.addCameraListener(cameraListener);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(com.tokopedia.imagepicker.R.string.title_loading));
    }

    private void generateImage(byte[] imageByte) {

        if (mCaptureNativeSize == null) {
            mCaptureNativeSize = cameraView.getPictureSize();
        }
        try {
            CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize.getWidth(), mCaptureNativeSize.getHeight(), new CameraUtils.BitmapCallback() {
                @Override
                public void onBitmapReady(Bitmap bitmap) {
                    File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, bitmap, false);
                    onSuccessImageTakenFromCamera(cameraResultFile);
                }
            });
        } catch (Throwable error) {
            File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false);
            onSuccessImageTakenFromCamera(cameraResultFile);
        }
    }

    private void onSuccessImageTakenFromCamera(File imgFile) {
        String imagePath = imgFile.getAbsolutePath();

        finalCameraResultFilePath = imagePath;
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (myBitmap != null) {
                    if (cameraView.getFacing().ordinal() == Facing.FRONT.ordinal()) {
                        imageCaptured.setImageBitmap(ImageHandler.flip(myBitmap, true, false));
                    } else {
                        imageCaptured.setImageBitmap(myBitmap);
                    }
                }
            }
            hideCameraProp();
        } catch (Throwable e) {

        }
        reset();

    }

    private void reset() {
        mCapturingPicture = false;
        mCaptureNativeSize = null;
        hideLoading();
    }

    private void capturePicture() {
        if (mCapturingPicture || !isCameraOpen) {
            return;
        }
        showLoading();
        mCapturingPicture = true;
        mCaptureNativeSize = cameraView.getPictureSize();
        cameraView.capturePicture();
    }

    private void showLoading() {
        if (isAdded()) {
            progressDialog.show();
        }
    }

    private void hideLoading() {
        if (isAdded()) {
            progressDialog.dismiss();
        }
    }

    private void toggleCamera() {
        if (mCapturingPicture) {
            return;
        }
        cameraView.toggleFacing();
    }


    public void hideCameraProp() {
        cameraView.stop();
        imageCaptured.setVisibility(View.VISIBLE);
        cameraActionsRL.setVisibility(View.GONE);
        pictureActionLL.setVisibility(View.VISIBLE);
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
        }
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
}
