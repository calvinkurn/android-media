package com.tokopedia.imagepicker.editor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.editor.adapter.ImageEditorEditActionAdapter;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;

/**
 * Created by hendry on 25/04/18.
 */

public class ImageEditPreviewFragment extends Fragment {

    public static final String ARG_ORI_IMAGE_PATH = "arg_ori_img_path";
    public static final String ARG_EDITTED_IMAGE_PATH = "arg_edit_img_path";
    public static final String ARG_MIN_RESOLUTION = "arg_min_resolution";

    private String oriImagePath;
    private String edittedImagePath;
    private int minResolution = 0;

    private View progressBar;

    private UCropView uCropView;
    private View blockingView;

    private OnImageEditPreviewFragmentListener onImageEditPreviewFragmentListener;
    public interface OnImageEditPreviewFragmentListener{
        boolean isInEditMode();
    }

    public static ImageEditPreviewFragment newInstance(String oriImagePath, String edittedImagePath, int minResolution) {
        Bundle args = new Bundle();
        args.putString(ARG_ORI_IMAGE_PATH, oriImagePath);
        args.putString(ARG_EDITTED_IMAGE_PATH, edittedImagePath);
        args.putInt(ARG_MIN_RESOLUTION, minResolution);
        ImageEditPreviewFragment fragment = new ImageEditPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_edit_preview, container, false);

        initVar(savedInstanceState);
        initUCrop(view);
        initProgressBar(view);

        blockingView = view.findViewById(R.id.blocking_view);
        return view;
    }

    private void initVar(@Nullable Bundle savedInstanceState){
        Bundle bundle = getArguments();
        oriImagePath = bundle.getString(ARG_ORI_IMAGE_PATH);
        minResolution = bundle.getInt(ARG_MIN_RESOLUTION);

        if (savedInstanceState == null) {
            edittedImagePath = getArguments().getString(ARG_EDITTED_IMAGE_PATH);
        } else {
            edittedImagePath = savedInstanceState.getString(ARG_EDITTED_IMAGE_PATH);
        }
    }

    private void initUCrop(final View view){
        uCropView = view.findViewById(R.id.ucrop);
        GestureCropImageView gestureCropImageView = uCropView.getCropImageView();

        gestureCropImageView.setTransformImageListener(new TransformImageView.TransformImageListener() {
            @Override
            public void onLoadComplete() {
                hideLoadingAndShowPreview();
            }

            @Override
            public void onLoadFailure(@NonNull Exception e) {
                NetworkErrorHelper.showRedCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getContext(), e));
            }

            @Override
            public void onRotate(float currentAngle) {
                // setAngleText(currentAngle);
            }

            @Override
            public void onScale(float currentScale) {
                // setScaleText(currentScale);
            }
        });

    }

    private void initProgressBar(View view){
        progressBar = view.findViewById(R.id.progressbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImageData();
    }

    private void setImageData() {
        showLoadingAndHidePreview();
        processOptions();
        try {
            Uri inputUri = Uri.fromFile(new File(edittedImagePath));
            GestureCropImageView gestureCropImageView = uCropView.getCropImageView();
            gestureCropImageView.setImageUri(inputUri,
                    Uri.parse(ImageUtils.getTokopediaPhotoPath(edittedImagePath).toString()));
        } catch (Exception e) {

        }
    }

    private void processOptions() {

        GestureCropImageView gestureCropImageView = uCropView.getCropImageView();
        OverlayView overlayView = uCropView.getOverlayView();

        // Crop image view options
        gestureCropImageView.setMaxScaleMultiplier(CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER);
        gestureCropImageView.setImageToWrapCropBoundsAnimDuration(CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);

        // Overlay view options
        overlayView.setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_DISABLE);

        overlayView.setDimmedColor(getResources().getColor(android.R.color.white));
        overlayView.setCircleDimmedLayer(OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER);

        overlayView.setShowCropFrame(OverlayView.DEFAULT_SHOW_CROP_FRAME);
        overlayView.setCropFrameColor(getResources().getColor(com.yalantis.ucrop.R.color.ucrop_color_default_crop_frame));
        overlayView.setCropFrameStrokeWidth(getResources().getDimensionPixelSize(com.yalantis.ucrop.R.dimen.ucrop_default_crop_frame_stoke_width));

        overlayView.setShowCropGrid(OverlayView.DEFAULT_SHOW_CROP_GRID);
        overlayView.setCropGridRowCount(OverlayView.DEFAULT_CROP_GRID_ROW_COUNT);
        overlayView.setCropGridColumnCount(OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT);
        overlayView.setCropGridColor(getResources().getColor(com.yalantis.ucrop.R.color.ucrop_color_default_crop_grid));
        overlayView.setCropGridStrokeWidth(getResources().getDimensionPixelSize(com.yalantis.ucrop.R.dimen.ucrop_default_crop_grid_stoke_width));

        // Aspect ratio options
        float aspectRatioX = 1;
        float aspectRatioY = 1;
        gestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);

        // Result bitmap max size options
        int maxSizeX = 0;
        int maxSizeY = 0;

        if (maxSizeX > 0 && maxSizeY > 0) {
            gestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            gestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }


    private void showLoadingAndHidePreview() {
        uCropView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        blockingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingAndShowPreview() {
        progressBar.setVisibility(View.GONE);
        uCropView.setVisibility(View.VISIBLE);
        uCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
        if (onImageEditPreviewFragmentListener.isInEditMode()) {
            blockingView.setVisibility(View.GONE);
            uCropView.getOverlayView().setShowCropGrid(true);
        } else {
            blockingView.setVisibility(View.VISIBLE);
            uCropView.getOverlayView().setShowCropGrid(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_EDITTED_IMAGE_PATH, edittedImagePath);
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
        onImageEditPreviewFragmentListener = (OnImageEditPreviewFragmentListener) context;
    }

}
