package com.tokopedia.imagepicker.editor.main.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.tokopedia.imagepicker.picker.main.util.ExpectedImageRatioDef;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.util.BitmapLoadUtils;
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

    public static final String ARG_IMAGE_PATH = "arg_img_path";
    public static final String ARG_MIN_RESOLUTION = "arg_min_resolution";
    public static final String ARG_EXPECTED_RATIO = "arg_expected_ratio";
    public static final String ARG_CIRCLE_PREVIEW = "arg_circle_preview";

    private String edittedImagePath;
    private int minResolution = 0;
    private @ExpectedImageRatioDef
    int expectedRatio;
    private boolean isCirclePreview;

    private View progressBar;

    private UCropView uCropView;
    private View blockingView;

    private OnImageEditPreviewFragmentListener onImageEditPreviewFragmentListener;
    private boolean loadCompleted;

    public interface OnImageEditPreviewFragmentListener {
        boolean isInEditMode();

        void onSuccessSaveEditImage(Uri resultUri);

        void onErrorSaveEditImage(Throwable throwable);
    }

    public static ImageEditPreviewFragment newInstance(String imagePath, int minResolution,
                                                       @ExpectedImageRatioDef int ratioDef,
                                                       boolean isCirclePreview) {
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_PATH, imagePath);
        args.putInt(ARG_MIN_RESOLUTION, minResolution);
        args.putInt(ARG_EXPECTED_RATIO, ratioDef);
        args.putBoolean(ARG_CIRCLE_PREVIEW, isCirclePreview);
        ImageEditPreviewFragment fragment = new ImageEditPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_edit_preview, container, false);

        Bundle bundle = getArguments();
        minResolution = bundle.getInt(ARG_MIN_RESOLUTION);
        edittedImagePath = bundle.getString(ARG_IMAGE_PATH);
        expectedRatio = bundle.getInt(ARG_EXPECTED_RATIO);
        isCirclePreview = bundle.getBoolean(ARG_CIRCLE_PREVIEW);

        initUCrop(view);
        initProgressBar(view);

        blockingView = view.findViewById(R.id.blocking_view);
        return view;
    }

    private void initUCrop(final View view) {
        uCropView = view.findViewById(R.id.ucrop);
        GestureCropImageView gestureCropImageView = uCropView.getCropImageView();

        gestureCropImageView.setTransformImageListener(new TransformImageView.TransformImageListener() {
            @Override
            public void onLoadComplete() {
                loadCompleted = true;
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

    public void saveEdittedImage() {
        uCropView.getCropImageView().cropAndSaveImage(
                edittedImagePath.endsWith("png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, new BitmapCropCallback() {
                    @Override
                    public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                        onImageEditPreviewFragmentListener.onSuccessSaveEditImage(resultUri);
                    }

                    @Override
                    public void onCropFailure(@NonNull Throwable t) {
                        onImageEditPreviewFragmentListener.onErrorSaveEditImage(t);
                    }
                });
    }

    private void initProgressBar(View view) {
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
                    Uri.parse(ImageUtils.getTokopediaPhotoPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_EDIT, edittedImagePath).toString()));
        } catch (Exception e) {

        }
    }

    private void processOptions() {

        GestureCropImageView gestureCropImageView = uCropView.getCropImageView();
        OverlayView overlayView = uCropView.getOverlayView();

        // Crop image view options
        gestureCropImageView.setRotateEnabled(false);

        // set preview max bitmap, so it will fit the screen can also be zoomed.
        int maxPreviewWidth = BitmapLoadUtils.calculateMaxBitmapSize(getContext());
        gestureCropImageView.setMaxBitmapSize(maxPreviewWidth);

        // set max scale so it cannnot be zoomed under min resolution
        // same logic with the calculateInSampleSize;
        int[] widthHeight = ImageUtils.getWidthAndHeight(edittedImagePath);
        int maxWidthHeight = Math.max(widthHeight[0], widthHeight[1]);
        while (maxWidthHeight > maxPreviewWidth) {
            maxWidthHeight = maxWidthHeight / 2;
        }
        if (maxWidthHeight > minResolution) {
            gestureCropImageView.setMaxScaleMultiplier((float) maxWidthHeight / minResolution);
        } else {
            gestureCropImageView.setMaxScaleMultiplier(1);
        }

        gestureCropImageView.setImageToWrapCropBoundsAnimDuration(CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);

        // Overlay view options
        overlayView.setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_DISABLE);

        overlayView.setDimmedColor(getResources().getColor(R.color.grey_title));
        overlayView.setCircleDimmedLayer(isCirclePreview);

        overlayView.setShowCropFrame(OverlayView.DEFAULT_SHOW_CROP_FRAME);
        overlayView.setCropFrameColor(getResources().getColor(R.color.white));
        overlayView.setCropFrameStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_1));

        overlayView.setShowCropGrid(OverlayView.DEFAULT_SHOW_CROP_GRID);
        overlayView.setCropGridRowCount(OverlayView.DEFAULT_CROP_GRID_ROW_COUNT);
        overlayView.setCropGridColumnCount(OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT);
        overlayView.setCropGridColor(getResources().getColor(R.color.white_65));
        overlayView.setCropGridStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_1));

        float aspectRatioX = 1;
        float aspectRatioY = 1;

        // Aspect ratio options
        switch (expectedRatio) {
            case ExpectedImageRatioDef.TYPE_1_1:
                aspectRatioX = 1;
                aspectRatioY = 1;
                break;
            case ExpectedImageRatioDef.TYPE_4_5:
                aspectRatioX = 4;
                aspectRatioY = 5;
                break;
            case ExpectedImageRatioDef.TYPE_5_4:
                aspectRatioX = 5;
                aspectRatioY = 4;
                break;
        }
        gestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);

        int maxSizeX = ImageUtils.DEF_WIDTH;
        int maxSizeY = ImageUtils.DEF_HEIGHT;
        gestureCropImageView.setMaxResultImageSizeX(maxSizeX);
        gestureCropImageView.setMaxResultImageSizeY(maxSizeY);
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
        setEditMode(onImageEditPreviewFragmentListener.isInEditMode());
    }

    public void setEditMode(boolean isEditMode) {
        if (loadCompleted) {
            if (isEditMode) {
                blockingView.setVisibility(View.GONE);
                uCropView.getOverlayView().setShowCropGrid(true);
            } else {
                blockingView.setVisibility(View.VISIBLE);
                uCropView.getOverlayView().setShowCropGrid(false);
            }
            uCropView.getOverlayView().invalidate();
        }
    }

    public void cancelCropRotateImage() {
        GestureCropImageView gestureCropImageView = uCropView.getCropImageView();
        gestureCropImageView.postRotate(-gestureCropImageView.getCurrentAngle());
        gestureCropImageView.zoomOutImage(gestureCropImageView.getMinScale());
        gestureCropImageView.setImageToWrapCropBounds(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
