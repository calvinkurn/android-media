package com.tokopedia.imagepicker.picker.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Size;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.util.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerCameraFragment extends TkpdBaseV4Fragment {

    public static final String SAVED_FLASH_INDEX = "saved_flash_index";

    private CameraView cameraView;
    private ImageButton flashImageButton;
    private View shutterImageButton;
    private View flipImageButton;
    private RelativeLayout cameraLayout;
    private OnImagePickerCameraFragmentListener onImagePickerCameraFragmentListener;

    private boolean mCapturingPicture;
    private Size mCaptureNativeSize;
    private long mCaptureTime;
    private List<Flash> supportedFlashList;
    private int flashIndex;
    private ProgressDialog progressDialog;

    public interface OnImagePickerCameraFragmentListener {
        void onImageTaken(String filePath);

        boolean isMaxImageReached();

        boolean isFinishEditting();
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission("android.permission.CAMERA")
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
        cameraView = view.findViewById(R.id.camera_view);
        flashImageButton = view.findViewById(R.id.image_button_flash);
        shutterImageButton = view.findViewById(R.id.image_button_shutter);
        flipImageButton = view.findViewById(R.id.image_button_flip);
        cameraLayout = view.findViewById(R.id.layout_camera);
        cameraView.addCameraListener(new CameraListener() {

            @Override
            public void onCameraOpened(CameraOptions options) {
                initialFlash();
                setPreviewCameraLayoutOneByOne();
            }

            private void initialFlash() {
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
                    flashImageButton.setVisibility(View.VISIBLE);
                    setCameraFlash();
                } else {
                    flashImageButton.setVisibility(View.GONE);
                }
            }

            private void setPreviewCameraLayoutOneByOne() {
                ViewGroup.LayoutParams params = cameraLayout.getLayoutParams();
                int cameraSize = getPreviewSizeOneByOne();
                params.width = cameraSize;
                params.height = cameraSize;
                cameraLayout.setLayoutParams(params);
            }

            @Override
            public void onPictureTaken(byte[] imageByte) {
                generateImage(imageByte);
            }
        });

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
                if (mCapturingPicture) {
                    return;
                }
                if (onImagePickerCameraFragmentListener.isMaxImageReached()) {
                    return;
                }
                if (isAdded()) {
                    progressDialog.show();
                }
                mCapturingPicture = true;
                mCaptureTime = System.currentTimeMillis();
                mCaptureNativeSize = cameraView.getPictureSize();
                cameraView.capturePicture();
            }
        });
        flipImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCamera();
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.title_loading));
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
            CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize.getWidth(), mCaptureNativeSize.getHeight(), new CameraUtils.BitmapCallback() {
                @Override
                public void onBitmapReady(Bitmap bitmap) {
                    File file = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, bitmap, false);
                    onImagePickerCameraFragmentListener.onImageTaken(file.getAbsolutePath());
                    reset();
                }
            });
        } catch (OutOfMemoryError error) {
            File file = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false);
            onImagePickerCameraFragmentListener.onImageTaken(file.getAbsolutePath());
            reset();
        }
    }

    private void reset() {
        mCapturingPicture = false;
        mCaptureTime = 0;
        mCaptureNativeSize = null;
        if (isAdded()) {
            progressDialog.hide();
        }
    }

    private void toggleCamera() {
        if (mCapturingPicture) {
            return;
        }
        cameraView.toggleFacing();
    }

    private int getPreviewSizeOneByOne() {
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

        // This is to prevent bug in cameraview library
        // https://github.com/natario1/CameraView/issues/154
        if (onImagePickerCameraFragmentListener.isFinishEditting()) {
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

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyCamera();
    }

    private void startCamera() {
        try {
            cameraView.start();
        } catch (Exception e) {
            // no-op
        }
    }

    private void stopCamera() {
        try {
            cameraView.stop();
        } catch (Exception e) {
            // no-op
        }
    }

    private void destroyCamera() {
        try {
            cameraView.destroy();
        } catch (Exception e) {
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
