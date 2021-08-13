package com.tokopedia.homecredit.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.size.Size;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.homecredit.R;
import com.tokopedia.iconunify.IconUnify;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    public IconUnify flashControl;
    public ImageView buttonCancel;

    private Size mCaptureNativeSize;
    public ImageView imageCaptured;
    RelativeLayout cameraActionsRL;
    public LinearLayout pictureActionLL;

    private ProgressDialog progressDialog;
    public String finalCameraResultFilePath;
    protected ImageView cameraOverlayImage;
    protected TextView headerText;

    private static int IMAGE_QUALITY = 95;

    private static final String FOLDER_NAME = "extras";
    private static final String FILE_EXTENSIONS = ".jpg";

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
        Collection<Flash> flashSet = cameraView.getCameraOptions().getSupportedFlash();
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
        int colorWhite = ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_Static_White);
        if (flashEnum == Flash.AUTO.ordinal()) {
            flashControl.setImageDrawable(MethodChecker.getDrawable(getActivity(), com.tokopedia.imagepicker.common.R.drawable.ic_auto_flash));
        } else if (flashEnum == Flash.ON.ordinal()) {
            flashControl.setImage(IconUnify.FLASH_ON, colorWhite, colorWhite, colorWhite, colorWhite);
        } else if (flashEnum == Flash.OFF.ordinal()) {
            flashControl.setImage(IconUnify.FLASH_OFF, colorWhite, colorWhite, colorWhite, colorWhite);
        }
    }

    public void initCameraProp() {
        cameraView.open();
        cameraLayout.setVisibility(View.VISIBLE);
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
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
            }

            @Override
            public void onCameraClosed() {
                super.onCameraClosed();
                isCameraOpen = false;
            }

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                try {
                    generateImage(result.getData());
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
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    private void generateImage(byte[] imageByte) {
        if (mCaptureNativeSize == null) {
            mCaptureNativeSize = cameraView.getPictureSize();
        }
        try {
            CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize.getWidth(), mCaptureNativeSize.getHeight(), bitmap -> {
                if (bitmap != null) {
                    File cameraResultFile = saveToCacheDirectory(imageByte);
                    if (cameraResultFile != null) {
                        onSuccessImageTakenFromCamera(cameraResultFile);
                    }
                }
            });
        } catch (Throwable error) {
            File cameraResultFile = saveToCacheDirectory(imageByte);
            if (cameraResultFile != null) {
                onSuccessImageTakenFromCamera(cameraResultFile);
            }
        }
    }

    protected void onSuccessImageTakenFromCamera(File imgFile) {
        String imagePath = imgFile.getAbsolutePath();
        finalCameraResultFilePath = imagePath;
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (myBitmap != null) {
                    if (cameraView.getFacing().ordinal() == Facing.FRONT.ordinal()) {
                        Bitmap flippedBitmap = ImageHandler.flip(myBitmap, true, false);
                        myBitmap.recycle();
                        loadImageFromBitmap(getContext(), imageCaptured, flippedBitmap);
                    } else {
                        loadImageFromBitmap(getContext(), imageCaptured, myBitmap);
                    }
                }
            }
            hideCameraProp();
        } catch (Throwable e) {

        }
        reset();

    }

    private void loadImageFromBitmap(Context context, final ImageView imageView, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int min, max;
        if (width > height) {
            min = height;
            max = width;
        } else {
            min = width;
            max = height;
        }
        boolean loadFitCenter = min != 0 && (max / min) > 2;
        if (loadFitCenter)
            Glide.with(context).load(bitmapToByte(bitmap)).fitCenter().into(imageView);
        else
            Glide.with(context).load(bitmapToByte(bitmap)).into(imageView);
    }


    private byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);
        return stream.toByteArray();
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
        cameraView.takePicture();
    }

    private void showLoading() {
        if (isAdded()) {
            progressDialog.show();
        }
    }

    protected void hideLoading() {
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
        cameraView.close();
        cameraLayout.setVisibility(View.GONE);
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
            cameraView.open();
        } catch (Throwable e) {
            e.printStackTrace();
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

    @Override
    public void onDestroy() {
        hideLoading();
        cameraView.close();
        super.onDestroy();
    }

    private File getFileLocationFromDirectory() {
        File directory = new ContextWrapper(getActivity()).getDir(FOLDER_NAME, Context.MODE_PRIVATE);
        if (!directory.exists())
            directory.mkdir();

        String imageName = System.currentTimeMillis() + FILE_EXTENSIONS;
        return new File(directory.getAbsolutePath(), imageName);

    }


    private File saveToCacheDirectory(byte[] imageByte) {
        FileOutputStream out = null;
        try {
            File file = getFileLocationFromDirectory();
            out = new FileOutputStream(file);
            out.write(imageByte);
            return file;
        } catch (Exception e) {
            if (getActivity() != null)
                getActivity().finish();
            return null;
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                }

            }
        }
    }

}
