package com.tokopedia.useridentification.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Size;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.analytics.UserIdentificationAnalytics;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.user_identification_common.KYCConstant.EXTRA_STRING_IMAGE_RESULT;

/**
 * @author by alvinatin on 12/11/18.
 */
@RuntimePermissions
public class UserIdentificationCameraFragment extends TkpdBaseV4Fragment {

    public static final String ARG_VIEW_MODE = "view_mode";
    public static final int PARAM_VIEW_MODE_KTP = 1;
    public static final int PARAM_VIEW_MODE_FACE = 2;

    private static final long DEFAULT_ONE_MEGABYTE = 1024;
    private static final int MAX_FILE_SIZE = 10240;

    private CameraView cameraView;
    private ImageButton closeButton;
    private TextView title;
    private TextView subtitle;
    private View focusedFaceView;
    private View focusedKtpView;
    private View shutterButton;
    private View loading;
    private View switchCamera;
    private ImageView imagePreview;
    private View buttonLayout;
    private View reCaptureButton;
    private View nextButton;
    private String imagePath;
    private Size mCaptureNativeSize;
    private UserIdentificationAnalytics analytics;

    private int viewMode;

    private CameraListener cameraListener;

    public static Fragment createInstance(int viewMode) {
        UserIdentificationCameraFragment fragment = new UserIdentificationCameraFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_VIEW_MODE, viewMode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewMode = getArguments().getInt(ARG_VIEW_MODE, 1);
        }
        analytics = UserIdentificationAnalytics.createInstance(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_focus_view, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        cameraView = view.findViewById(R.id.full_camera_view);
        closeButton = view.findViewById(R.id.close_button);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        imagePreview = view.findViewById(R.id.full_image_preview);
        focusedFaceView = view.findViewById(R.id.focused_view_face);
        focusedKtpView = view.findViewById(R.id.focused_view_ktp);
        shutterButton = view.findViewById(R.id.image_button_shutter);
        switchCamera = view.findViewById(R.id.image_button_flip);
        buttonLayout = view.findViewById(R.id.button_layout);
        reCaptureButton = view.findViewById(R.id.recapture_button);
        nextButton = view.findViewById(R.id.next_button);
        loading = view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateView();
    }

    private void populateView() {
        shutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnalyticClickShutter();
                capturePictureWithCheck();
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnalyticClickFlipCamera();
                toggleCamera();
            }
        });

        reCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnalyticClickRecapture();
                showCameraView();
            }
        });

        cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(CameraOptions options) {
                super.onCameraOpened(options);
            }

            @Override
            public void onCameraClosed() {
                super.onCameraClosed();
            }

            @Override
            public void onPictureTaken(byte[] imageByte) {
                saveToFileWithCheck(imageByte);
            }
        };

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                }
                if (isCameraVisible()) {
                    sendAnalyticClickBackCamera();
                } else {
                    sendAnalyticClickCloseImagePreview();
                }
                getActivity().finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (isFileSizeQualified(imagePath)) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_STRING_IMAGE_RESULT, imagePath);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                    } else {
                        getActivity().setResult(KYCConstant.IS_FILE_IMAGE_TOO_BIG);
                    }
                }
                sendAnalyticClickNext();
                getActivity().finish();
            }
        });
        populateViewByViewMode();
        showCameraView();
        sendAnalyticOpenCamera();
    }

    private void sendAnalyticOpenCamera() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventViewOpenCameraKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventViewOpenCameraSelfie();
                break;
            default:
                break;
        }
    }

    private void sendAnalyticClickBackCamera() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventClickBackCameraKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventClickBackCameraSelfie();
                break;
            default:
                break;
        }
    }

    private void sendAnalyticClickShutter() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventClickShutterCameraKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventClickShutterCameraSelfie();
                break;
            default:
                break;
        }
    }

    private void sendAnalyticClickFlipCamera() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventClickFlipCameraKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventClickFlipCameraSelfie();
                break;
            default:
                break;
        }
    }

    private void sendAnalyticViewImagePreview() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventViewImagePreviewKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventViewImagePreviewSelfie();
                break;
            default:
                break;
        }
    }

    private void sendAnalyticClickCloseImagePreview() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventClickCloseImagePreviewKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventClickCloseImagePreviewSelfie();
                break;
            default:
                break;
        }
    }

    private void sendAnalyticClickRecapture() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventClickRecaptureKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventClickRecaptureSelfie();
                break;
            default:
                break;
        }
    }

    private void sendAnalyticClickNext() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                analytics.eventClickNextImagePreviewKtp();
                break;
            case PARAM_VIEW_MODE_FACE:
                analytics.eventClickNextImagePreviewSelfie();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        destroyCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        showCameraView();
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

    private void destroyCamera() {
        try {
            cameraView.destroy();
        } catch (Throwable e) {
            // no-op
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void capturePicture() {
        hideCameraButtonAndShowLoading();
        cameraView.capturePicture();
    }

    private void capturePictureWithCheck() {
        UserIdentificationCameraFragmentPermissionsDispatcher.capturePictureWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void saveToFile(byte[] imageByte) {
        mCaptureNativeSize = cameraView.getPictureSize();
        try {
            //rotate the bitmap using the library
            CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize.getWidth(), mCaptureNativeSize
                    .getHeight(), new CameraUtils.BitmapCallback() {
                @Override
                public void onBitmapReady(Bitmap bitmap) {
                    Observable.just(bitmap).flatMap(new Func1<Bitmap, Observable<File>>() {
                        @Override
                        public Observable<File> call(Bitmap bitmap) {
                            File tempFile = ImageUtils.writeImageToTkpdPath(ImageUtils
                                    .DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, bitmap, false);
                            File cameraResultFile = ImageUtils.resizeBitmapToFile(tempFile.getAbsolutePath(), 640, 640, false,
                                    ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA);
                            if (cameraResultFile == null) {
                                cameraResultFile = tempFile;
                            } else {
                                tempFile.delete();
                            }
                            return Observable.just(cameraResultFile);
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<File>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef
                                            .DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false);
                                    onSuccessImageTakenFromCamera(cameraResultFile);
                                }

                                @Override
                                public void onNext(File cameraResultFile) {
                                    onSuccessImageTakenFromCamera(cameraResultFile);
                                }
                            });
                }
            });
        } catch (Throwable error) {
            File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef
                    .DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false);
            onSuccessImageTakenFromCamera(cameraResultFile);
        }
    }

    private void saveToFileWithCheck(byte[] imageByte) {
        UserIdentificationCameraFragmentPermissionsDispatcher.saveToFileWithCheck(this, imageByte);
    }

    private void onSuccessImageTakenFromCamera(File cameraResultFile) {
        if (cameraResultFile.exists()) {
            ImageHandler.loadImageFromFile(getContext(), imagePreview, cameraResultFile);
            imagePath = cameraResultFile.getAbsolutePath();
            showImagePreview();
        } else {
            Toast.makeText(getContext(), "Terjadi kesalahan, silahkan coba lagi", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void populateViewByViewMode() {
        switch (viewMode) {
            case PARAM_VIEW_MODE_KTP:
                focusedKtpView.setVisibility(View.VISIBLE);
                focusedFaceView.setVisibility(View.GONE);
                title.setText(R.string.camera_ktp_title);
                subtitle.setText(R.string.camera_ktp_subtitle);
                break;
            case PARAM_VIEW_MODE_FACE:
                focusedKtpView.setVisibility(View.GONE);
                focusedFaceView.setVisibility(View.VISIBLE);
                title.setText(R.string.camera_face_title);
                subtitle.setText(R.string.camera_face_subtitle);
                toggleCamera();
                break;
        }
    }

    private void showCameraView() {
        cameraView.setVisibility(View.VISIBLE);
        shutterButton.setVisibility(View.VISIBLE);
        switchCamera.setVisibility(View.VISIBLE);
        startCamera();
        loading.setVisibility(View.GONE);
        imagePreview.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.GONE);
    }

    private void showImagePreview() {
        cameraView.setVisibility(View.GONE);
        shutterButton.setVisibility(View.GONE);
        switchCamera.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        destroyCamera();
        imagePreview.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);
        sendAnalyticViewImagePreview();
    }

    private void hideCameraButtonAndShowLoading() {
        shutterButton.setVisibility(View.GONE);
        switchCamera.setVisibility(View.GONE);
        imagePreview.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void toggleCamera() {
        if (isCameraVisible()) {
            cameraView.toggleFacing();
        }
    }

    private boolean isFileSizeQualified(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            int fileSize = Integer.parseInt(String.valueOf(file.length() / DEFAULT_ONE_MEGABYTE));
            return (fileSize <= MAX_FILE_SIZE);
        } else
            return false;
    }

    public boolean isCameraVisible() {
        return (cameraView != null &&
                cameraView.getVisibility() == View.VISIBLE);
    }
}
