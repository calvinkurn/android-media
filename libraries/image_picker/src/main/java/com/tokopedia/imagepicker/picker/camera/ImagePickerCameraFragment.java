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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.size.Size;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.iconunify.IconUnify;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImageRatioType;
import com.tokopedia.imagepicker.common.presenter.ImageRatioCropPresenter;
import com.tokopedia.utils.image.ImageProcessingUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerCameraFragment extends TkpdBaseV4Fragment implements ImageRatioCropPresenter.ImageRatioCropView {

    public static final String SAVED_FLASH_INDEX = "saved_flash_index";

    private ImageView previewImageView;
    private CameraView cameraView;
    private IconUnify flashImageButton;
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

        //initialize of cameraView mode
        cameraView.setMode(Mode.PICTURE);

        cameraListener = new CameraListener() {

            @Override
            public void onCameraOpened(@NonNull CameraOptions options) {
                super.onCameraOpened(options);
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
                if (supportedFlashList.size() > 0) {
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
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading));
    }

    private boolean isOneOneRatio() {
        int ratioX = onImagePickerCameraFragmentListener.getRatioX();
        int ratioY = onImagePickerCameraFragmentListener.getRatioY();
        return ratioY > 0 && ratioX == ratioY;
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
        if (getContext() != null) {
            int colorWhite = ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_Static_White);
            if (flashEnum == Flash.AUTO.ordinal() && getActivity() != null) {
                flashImageButton.setImageDrawable(MethodChecker.getDrawable(getActivity(), com.tokopedia.imagepicker.common.R.drawable.ic_auto_flash));
            } else if (flashEnum == Flash.ON.ordinal()) {
                flashImageButton.setImage(IconUnify.FLASH_ON, colorWhite, colorWhite, colorWhite, colorWhite);
            } else if (flashEnum == Flash.OFF.ordinal()) {
                flashImageButton.setImage(IconUnify.FLASH_OFF, colorWhite, colorWhite, colorWhite, colorWhite);
            }
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
            if (mCaptureNativeSize != null) {
                CameraUtils.decodeBitmap(
                        imageByte,
                        mCaptureNativeSize.getWidth(),
                        mCaptureNativeSize.getHeight(), bitmap -> {
                            if (bitmap!= null) {
                                File cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG);
                                onSuccessImageTakenFromCamera(cameraResultFile);
                            }
                        });
            }
        } catch (Throwable error) {
            File cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(imageByte, Bitmap.CompressFormat.JPEG);
            onSuccessImageTakenFromCamera(cameraResultFile);
        }
    }

    private void onSuccessImageTakenFromCamera(File file) {
        if (file == null) {
            return;
        }
        //crop the bitmap if it is not aligned with the expected ratio
        if (isOneOneRatio()) {
            initCropPresenter();
            ArrayList<String> list = new ArrayList<>();
            list.add(file.getAbsolutePath());
            ArrayList<ImageRatioType> ratioList = new ArrayList<>();
            ratioList.add(ImageRatioType.RATIO_1_1);
            imageRatioCropPresenter.cropBitmapToExpectedRatio(list, ratioList, false, false);
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
        if (getActivity() != null) {
            if (getActivity().isFinishing()) {
                return;
            }
        }
        String permission = Manifest.permission.CAMERA;
        if (getContext() != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
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
        if (getActivity() != null) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            return width;
        } else {
            return 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onVisible();
    }

    @Override
    public void onPause() {
        hideLoading();
        super.onPause();
        // https://github.com/natario1/CameraView/issues/122
        stopCamera();
    }

    @Override
    public void onDestroy() {
        hideLoading();
        super.onDestroy();
        stopCamera();
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
