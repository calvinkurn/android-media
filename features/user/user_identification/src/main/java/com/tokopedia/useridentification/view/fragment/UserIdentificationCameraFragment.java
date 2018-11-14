package com.tokopedia.useridentification.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.useridentification.R;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author by alvinatin on 12/11/18.
 */
@RuntimePermissions
public class UserIdentificationCameraFragment extends TkpdBaseV4Fragment {

    public static final String ARG_VIEW_MODE = "view_mode";
    public static final int PARAM_VIEW_MODE_KTP = 1;
    public static final int PARAM_VIEW_MODE_FACE = 2;

    private CameraView cameraView;
    private ImageButton closeButton;
    private TextView title;
    private TextView subtitle;
    private View focusedFaceView;
    private View focusedKtpView;
    private View shutterButton;
    private ImageButton switchCamera;
    private ImageView imagePreview;
    private View buttonLayout;
    private View reCaptureButton;
    private View nextButton;
    private File imageFile;

    private int viewMode;

    private CameraListener cameraListener;
    private OnCameraFragmentListener onCameraFragmentListener;

    public interface OnCameraFragmentListener {
        void onImageTaken(int viewMode, File imageFile);
    }

    public static Fragment createInstance(int viewMode){
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
        onCameraFragmentListener = (OnCameraFragmentListener) context;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker_camera, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        cameraView = view.findViewById(R.id.camera_view);
        closeButton = view.findViewById(R.id.close_button);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        imagePreview = view.findViewById(R.id.image_preview);
        focusedFaceView = view.findViewById(R.id.focused_view_face);
        focusedKtpView = view.findViewById(R.id.focused_view_ktp);
        shutterButton = view.findViewById(R.id.image_button_shutter);
        switchCamera = view.findViewById(R.id.image_button_flip);
        buttonLayout = view.findViewById(R.id.button_layout);
        reCaptureButton = view.findViewById(R.id.recapture_button);
        nextButton = view.findViewById(R.id.next_button);

        shutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePictureWithCheck();
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCamera();
            }
        });

        reCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add pass image to model

                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        populateViewByViewMode(viewMode);
        showCameraView();
        cameraView.addCameraListener(cameraListener);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void capturePicture() {
        cameraView.capturePicture();
    }

    private void capturePictureWithCheck(){
        UserIdentificationCameraFragmentPermissionsDispatcher.capturePictureWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void saveToFile(byte[] imageByte){
        File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false);
        onSuccessImageTakenFromCamera(cameraResultFile);
    }

    private void saveToFileWithCheck(byte[] imageByte){
        UserIdentificationCameraFragmentPermissionsDispatcher.saveToFileWithCheck(this, imageByte);
    }

    private void onSuccessImageTakenFromCamera(File cameraResultFile) {
        imageFile = new File(cameraResultFile.getAbsolutePath());
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imagePreview.setImageBitmap(bitmap);
        }
        showImagePreview();
    }

    private void populateViewByViewMode(int viewMode) {
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
                break;
        }
    }

    private void showCameraView() {
        cameraView.setVisibility(View.VISIBLE);
        shutterButton.setVisibility(View.VISIBLE);
        switchCamera.setVisibility(View.VISIBLE);

        imagePreview.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.GONE);
    }

    private void showImagePreview() {
        cameraView.setVisibility(View.GONE);
        shutterButton.setVisibility(View.GONE);
        switchCamera.setVisibility(View.GONE);

        imagePreview.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);
    }

    private void toggleCamera() {
        if (cameraView.getVisibility() == View.VISIBLE) {
            cameraView.toggleFacing();
        }
    }
}
