package com.tokopedia.imagepicker.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.editor.presenter.ImageEditPreviewPresenter;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;

/**
 * Created by hendry on 25/04/18.
 */

public class ImageEditPreviewFragment extends Fragment implements ImageEditPreviewPresenter.ImageEditPreviewView {

    public static final String ARG_ORI_IMAGE_PATH = "arg_ori_img_path";
    public static final String ARG_EDITTED_IMAGE_PATH = "arg_edit_img_path";
    public static final String ARG_MIN_RESOLUTION = "arg_min_resolution";

    private String oriImagePath;
    private String edittedImagePath;
    private int rotationCount = 0;
    private int minResolution = 0;

    private ImageEditPreviewPresenter imageEditPreviewPresenter;
    private View progressBar;
    private View snapButton;

    private UCropView uCropView;
    private GestureCropImageView gestureCropImageView;
    private OverlayView overlayView;

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

        setImageData();

        return view;
    }

    private void setImageData() {
        processOptions();

        try {
            Uri inputUri = Uri.fromFile(new File(edittedImagePath));
            gestureCropImageView.setImageUri(inputUri,
                    Uri.parse(ImageUtils.getTokopediaPhotoPath(edittedImagePath).toString()));
        } catch (Exception e) {

        }
    }

    private void processOptions() {

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
        gestureCropImageView = uCropView.getCropImageView();
        overlayView = uCropView.getOverlayView();

        gestureCropImageView.setTransformImageListener(new TransformImageView.TransformImageListener() {
            @Override
            public void onLoadComplete() {
                uCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());

                // TODO
                //mBlockingView.setClickable(false);
                //mShowLoader = false;
                // getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onLoadFailure(@NonNull Exception e) {
                NetworkErrorHelper.showRedCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getContext(), e));
            }

            @Override
            public void onRotate(float currentAngle) {
//                setAngleText(currentAngle);
            }

            @Override
            public void onScale(float currentScale) {
//                setScaleText(currentScale);
            }
        });

    }

    private void initProgressBar(View view){
        progressBar = view.findViewById(R.id.progressbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadNewImage();
    }

    private void loadNewImage() {
//        rotationCount = 0;
//        imageEditPreviewPresenter = new ImageEditPreviewPresenter();
//        imageEditPreviewPresenter.attachView(this);
//        showPreviewLoading();
//
//        imageEditPreviewPresenter.convertImagePathToPreviewBitmap(edittedImagePath);
    }

    @Override
    public void onErrorConvertPathToPreviewBitmap(Throwable e) {
        hidePreviewLoading();
        NetworkErrorHelper.showRedCloseSnackbar(getView(), ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void onSuccessConvertPathToPreviewBitmap(ImageEditPreviewPresenter.BitmapPreviewResult bitmapPreviewResult) {
//        hidePreviewLoading();
//
//        Bitmap previewBitmap = bitmapPreviewResult.previewBitmap;

//        float maxZoomResolution = -1;
//        if (minResolution > 0) {
//            int originalResolution = Math.min(bitmapPreviewResult.originalWidth, bitmapPreviewResult.originalHeight);
//            maxZoomResolution = (float) originalResolution / minResolution;
//        }
//        float defaultMaxZoom = cropperView.getWidth() * 2 / bitmapPreviewResult.previewWidth;
//
//        float maxZoom = Math.min(defaultMaxZoom, maxZoomResolution);
//        cropperView.setMaxZoom(maxZoom > 1 ? maxZoom : 1);
        //cropperView.setMaxZoom(1.6f);

//        Matrix matrix = cropperView.getMatrix();
//        float scaleX = getMatrixValue(matrix, Matrix.MSCALE_X);
//        cropperView.setMaxZoom(cropperView.getWidth()/bitmapPreviewResult.previewWidth);
//        cropperView.setMaxZoom(5);
//
//        cropperView.setImageBitmap(previewBitmap);

    }

//    private float[] mMatrixValues = new float[9];
//
//    private float getMatrixValue(Matrix matrix, int whichValue) {
//        matrix.getValues(mMatrixValues);
//        return mMatrixValues[whichValue];
//    }

    private void showPreviewLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidePreviewLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageEditPreviewPresenter != null) {
            imageEditPreviewPresenter.detachView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_EDITTED_IMAGE_PATH, edittedImagePath);
    }


}
