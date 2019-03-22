package com.tokopedia.imagepicker.picker.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.tokopedia.cameraview.BitmapCallback;
import com.tokopedia.cameraview.CameraListener;
import com.tokopedia.cameraview.CameraOptions;
import com.tokopedia.cameraview.CameraUtils;
import com.tokopedia.cameraview.CameraView;
import com.tokopedia.cameraview.Flash;
import com.tokopedia.cameraview.PictureResult;
import com.tokopedia.cameraview.Size;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.presenter.ImageRatioCropPresenter;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerCameraFragment extends TkpdBaseV4Fragment implements ImageRatioCropPresenter.ImageRatioCropView {

    public static final String SAVED_FLASH_INDEX = "saved_flash_index";

    private ImageView previewImageView;
    private CameraView cameraView;
    private ImageButton flashImageButton;
    private FrameLayout cameraLayout;
    private View previewLayout;
    private OnImagePickerCameraFragmentListener onImagePickerCameraFragmentListener;

    private boolean mCapturingPicture;
    private Size mCaptureNativeSize;
    private long mCaptureTime;
    private List<Flash> supportedFlashList;
    private int flashIndex;
    private ProgressDialog progressDialog;
    private String finalCameraResultFilePath;
    private ImageRatioCropPresenter imageRatioCropPresenter;
    private boolean isCameraOpen;
    private CameraListener cameraListener;

    public interface OnImagePickerCameraFragmentListener {
        void onImageTaken(String filePath);

        boolean isMaxImageReached();

        boolean isFinishEditting();

        void onCameraViewVisible();

        void onPreviewCameraViewVisible();

        boolean needShowCameraPreview();

        int getRatioX();

        int getRatioY();
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.CAMERA)
    public static ImagePickerCameraFragment newInstance() {
        return new ImagePickerCameraFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            flashIndex = savedInstanceState.getInt(SAVED_FLASH_INDEX, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_picker_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        previewImageView = view.findViewById(R.id.image_preview);
        cameraView = view.findViewById(R.id.camera_view);
        flashImageButton = view.findViewById(R.id.image_button_flash);
        View shutterImageButton = view.findViewById(R.id.image_button_shutter);
        View flipImageButton = view.findViewById(R.id.image_button_flip);
        cameraLayout = view.findViewById(R.id.layout_camera);
        previewLayout = view.findViewById(R.id.layout_preview);
        View useImageLayout = view.findViewById(R.id.layout_use);
        View recaptureLayout = view.findViewById(R.id.layout_recapture);

        //noinspection SuspiciousNameCombination
        cameraListener = new CameraListener() {

            @Override
            public void onCameraOpened(CameraOptions options) {
                initialFlash();
                setPreviewCameraLayout();
                isCameraOpen = true;
            }

            private void initialFlash() {
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
                    flashImageButton.setVisibility(View.VISIBLE);
                    setCameraFlash();
                } else {
                    flashImageButton.setVisibility(View.GONE);
                }
            }

            private void setPreviewCameraLayout() {
                if (isOneOneRatio()) {
                    ViewGroup.LayoutParams params = cameraLayout.getLayoutParams();
                    int cameraSize = getDeviceWidth();
                    params.width = cameraSize;
                    params.height = cameraSize;
                    cameraLayout.setLayoutParams(params);

                    FrameLayout.LayoutParams previewParams = (FrameLayout.LayoutParams) previewImageView.getLayoutParams();
                    int width = cameraSize - previewParams.leftMargin - previewParams.rightMargin;
                    previewParams.width = width;
                    //noinspection SuspiciousNameCombination
                    previewParams.height = width;
                    previewImageView.setLayoutParams(previewParams);

                    previewImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    ViewGroup.LayoutParams params = cameraLayout.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    cameraLayout.setLayoutParams(params);

                    params = previewImageView.getLayoutParams();
                    //noinspection SuspiciousNameCombination
                    params.height = params.width;
                    previewImageView.setLayoutParams(params);

                    previewImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }

            @Override
            public void onCameraClosed() {
                super.onCameraClosed();
                isCameraOpen = false;
            }

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                generateImage(result.getData());
            }

        };
        cameraView.addCameraListener(cameraListener);

        flashImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (supportedFlashList != null && supportedFlashList.size() > 0) {
                    flashIndex = (flashIndex + 1) % supportedFlashList.size();
                    setCameraFlash();
                }
            }
        });

        shutterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }

            private void capturePhoto() {
                if (mCapturingPicture || onImagePickerCameraFragmentListener.isMaxImageReached() ||
                        !isCameraOpen) {
                    return;
                }
                showLoading();
                mCapturingPicture = true;
                mCaptureTime = System.currentTimeMillis();
                mCaptureNativeSize = cameraView.getPictureSize();
                cameraView.takePicture();
            }
        });

        flipImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCamera();
            }
        });

        recaptureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraView();
            }
        });

        useImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImagePickerCameraFragmentListener.onImageTaken(finalCameraResultFilePath);

                //if multiple selection, will continue preview camera
                if (onImagePickerCameraFragmentListener.needShowCameraPreview()) {
                    showCameraView();
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    private boolean isOneOneRatio() {
        int ratioX = onImagePickerCameraFragmentListener.getRatioX();
        int ratioY = onImagePickerCameraFragmentListener.getRatioY();
        return ratioX > 0 && ratioY > 0 && ratioX == ratioY;
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
            flashImageButton.setImageResource(R.drawable.ic_auto_flash);
        } else if (flashEnum == Flash.ON.ordinal()) {
            flashImageButton.setImageResource(R.drawable.ic_on_flash);
        } else if (flashEnum == Flash.OFF.ordinal()) {
            flashImageButton.setImageResource(R.drawable.ic_off_flash);
        }
    }


    private void generateImage(byte[] imageByte) {
        long callbackTime = System.currentTimeMillis();
        // This can happen if picture was taken with a gesture.
        if (mCaptureTime == 0) {
            mCaptureTime = callbackTime - 300;
        }
        if (mCaptureNativeSize == null) {
            mCaptureNativeSize = cameraView.getPictureSize();
        }
        try {
            //rotate the bitmap using the library
            CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize != null ? mCaptureNativeSize.getWidth() : 0, mCaptureNativeSize.getHeight(), bitmap -> {
                File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, bitmap, false);
                onSuccessImageTakenFromCamera(cameraResultFile);
            });
        } catch (Throwable error) {
            File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false);
            onSuccessImageTakenFromCamera(cameraResultFile);
        }
    }

    private void onSuccessImageTakenFromCamera(File file) {
        //crop the bitmap if it is not aligned with the expected ratio
        if (isOneOneRatio()) {
            initCropPresenter();
            ArrayList<String> list = new ArrayList<>();
            list.add(file.getAbsolutePath());
            ArrayList<ImageRatioTypeDef> ratioList = new ArrayList<>();
            ratioList.add(ImageRatioTypeDef.RATIO_1_1);
            imageRatioCropPresenter.cropBitmapToExpectedRatio(list, ratioList, false,
                    ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA);
        } else {
            onFinishEditAfterTakenFromCamera(file.getAbsolutePath());
        }

    }

    private void initCropPresenter(){
        if (imageRatioCropPresenter == null) {
            imageRatioCropPresenter = new ImageRatioCropPresenter();
            imageRatioCropPresenter.attachView(this);
        }
    }

    @Override
    public void onErrorCropImageToRatio(ArrayList<String> localImagePath, Throwable e) {
        onFinishEditAfterTakenFromCamera(localImagePath.get(0));
    }

    @Override
    public void onSuccessCropImageToRatio(ArrayList<String> cropppedImagePaths, ArrayList<Boolean> isEditted) {
        onFinishEditAfterTakenFromCamera(cropppedImagePaths.get(0));
    }

    public void onFinishEditAfterTakenFromCamera(String imagePath) {
        if (onImagePickerCameraFragmentListener.needShowCameraPreview()) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    previewImageView.setImageBitmap(myBitmap);
                }
                showPreviewView();
            } catch (Throwable e) {
                onImagePickerCameraFragmentListener.onImageTaken(imagePath);
            }
        } else {
            onImagePickerCameraFragmentListener.onImageTaken(imagePath);
        }
        finalCameraResultFilePath = imagePath;
        reset();
    }

    public void onVisible(){
        // This is to prevent bug in cameraview library
        // https://github.com/natario1/CameraView/issues/154
        if (onImagePickerCameraFragmentListener.isFinishEditting()) {
            return;
        }
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

    public void onInvisible(){
        destroyCamera();
    }

    private void showLoading(){
        if (isAdded()) {
            progressDialog.show();
        }
    }

    private void hideLoading(){
        if (isAdded()) {
            progressDialog.dismiss();
        }
    }

    private void showCameraView() {
        previewLayout.setVisibility(View.GONE);
        cameraLayout.setVisibility(View.VISIBLE);
        onImagePickerCameraFragmentListener.onCameraViewVisible();
    }

    private void showPreviewView() {
        previewLayout.setVisibility(View.VISIBLE);
        cameraLayout.setVisibility(View.GONE);
        onImagePickerCameraFragmentListener.onPreviewCameraViewVisible();
    }

    private void reset() {
        mCapturingPicture = false;
        mCaptureTime = 0;
        mCaptureNativeSize = null;
        hideLoading();
    }

    private void toggleCamera() {
        if (mCapturingPicture) {
            return;
        }
        cameraView.toggleFacing();
    }

    private int getDeviceWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }

    @Override
    public void onResume() {
        super.onResume();
        onVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        // https://github.com/natario1/CameraView/issues/122
        destroyCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyCamera();
    }

    private void startCamera() {
        try {
            showCameraView();
            cameraView.clearCameraListeners();
            cameraView.addCameraListener(cameraListener);
            cameraView.open();
        } catch (Throwable e) {
            // no-op
        }
    }

    private void stopCamera() {
        try {
            cameraView.close();
        } catch (Throwable e) {
            // no-op
        }
    }

    private void destroyCamera() {
        try {
            hideLoading();
            cameraView.destroy();
            isCameraOpen = false;
        } catch (Throwable e) {
            // no-op
        }
    }

    @Override
    protected String getScreenName() {
        return null;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_FLASH_INDEX, flashIndex);
    }
}
