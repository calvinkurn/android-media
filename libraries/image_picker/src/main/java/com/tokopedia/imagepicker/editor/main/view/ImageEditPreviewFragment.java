package com.tokopedia.imagepicker.editor.main.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;

import static com.tokopedia.imagepicker.editor.main.Constant.BRIGHTNESS_PRECISION;
import static com.tokopedia.imagepicker.editor.main.Constant.CONTRAST_PRECISION;
import static com.tokopedia.imagepicker.editor.main.Constant.INITIAL_CONTRAST_VALUE;

/**
 * Created by hendry on 25/04/18.
 */

public class ImageEditPreviewFragment extends Fragment implements ImageEditPreviewPresenter.ImageEditPreviewView {

    public static final String ARG_IMAGE_INDEX = "arg_img_index";
    public static final String ARG_IMAGE_PATH = "arg_img_path";
    public static final String ARG_MIN_RESOLUTION = "arg_min_resolution";
    public static final String ARG_EXPECTED_RATIO_X = "arg_expected_ratio_x";
    public static final String ARG_EXPECTED_RATIO_Y = "arg_expected_ratio_y";
    public static final String ARG_CIRCLE_PREVIEW = "arg_circle_preview";
    public static final int ROTATE_WIDGET_SENSITIVITY = 1;

    public static final String SAVED_BRIGHTNESS = "svd_brightness";
    public static final String SAVED_CONTRAST = "svd_contrast";

    private String edittedImagePath;
    private int minResolution = 0;
    private int expectedRatioX, expectedRatioY;
    private boolean isCirclePreview;
    private float brightness = 0, contrast = INITIAL_CONTRAST_VALUE;

    private View progressBar;

    private UCropView uCropView;
    private View blockingView;

    private OnImageEditPreviewFragmentListener onImageEditPreviewFragmentListener;
    private boolean loadCompleted;
    private GestureCropImageView gestureCropImageView;
    private OverlayView overlayView;

    private ImageView ivUndo;
    private ImageView ivRedo;

    private ImageEditPreviewPresenter imageEditPreviewPresenter;
    private int imageIndex;

    public interface OnImageEditPreviewFragmentListener {
        boolean isInEditMode();

        void onEditDoNothing();

        void onSuccessSaveEditImage(String path);

        void onErrorSaveEditImage(Throwable throwable);

        void setRotateAngle(float angle);

        void undoToPrevImage(int imageIndex);

        void redoToPrevImage(int imageIndex);

        boolean hasHistory(int imageIndex);

        boolean canUndo(int imageIndex);

        boolean canRedo(int imageIndex);
    }

    public static ImageEditPreviewFragment newInstance(int imageIndex,
                                                       String imagePath, int minResolution,
                                                       int ratioX, int ratioY,
                                                       boolean isCirclePreview) {
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_INDEX, imageIndex);
        args.putString(ARG_IMAGE_PATH, imagePath);
        args.putInt(ARG_MIN_RESOLUTION, minResolution);
        args.putInt(ARG_EXPECTED_RATIO_X, ratioX);
        args.putInt(ARG_EXPECTED_RATIO_Y, ratioY);
        args.putBoolean(ARG_CIRCLE_PREVIEW, isCirclePreview);
        ImageEditPreviewFragment fragment = new ImageEditPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageEditPreviewPresenter = new ImageEditPreviewPresenter();
        imageEditPreviewPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_edit_preview, container, false);

        Bundle bundle = getArguments();
        imageIndex = bundle.getInt(ARG_IMAGE_INDEX);
        minResolution = bundle.getInt(ARG_MIN_RESOLUTION);
        edittedImagePath = bundle.getString(ARG_IMAGE_PATH);
        expectedRatioX = bundle.getInt(ARG_EXPECTED_RATIO_X);
        expectedRatioY = bundle.getInt(ARG_EXPECTED_RATIO_Y);
        isCirclePreview = bundle.getBoolean(ARG_CIRCLE_PREVIEW);

        if (savedInstanceState == null) {
            brightness = 0;
            contrast = INITIAL_CONTRAST_VALUE;
        } else {
            brightness = savedInstanceState.getFloat(SAVED_BRIGHTNESS);
            contrast = savedInstanceState.getFloat(SAVED_CONTRAST);
        }

        initUCrop(view);
        initProgressBar(view);

        blockingView = view.findViewById(R.id.blocking_view);

        setupUndoRedo(view);

        return view;
    }

    private void setupUndoRedo(View view) {
        ivUndo = view.findViewById(R.id.iv_undo);
        ivRedo = view.findViewById(R.id.iv_redo);
        ivUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUndoButtonClicked();
            }
        });
        ivRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRedoButtonClicked();
            }
        });
        renderUndoRedo();
    }

    private void renderUndoRedo() {
        if (onImageEditPreviewFragmentListener.hasHistory(imageIndex) && onImageEditPreviewFragmentListener.isInEditMode()) {

            if (onImageEditPreviewFragmentListener.canUndo(imageIndex)) {
                ivUndo.getDrawable().clearColorFilter();
            } else {
                ivUndo.getDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_700),
                        PorterDuff.Mode.MULTIPLY);
            }
            ivUndo.setVisibility(View.VISIBLE);

            if (onImageEditPreviewFragmentListener.canRedo(imageIndex)) {
                ivRedo.getDrawable().clearColorFilter();
            } else {
                ivRedo.getDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_700),
                        PorterDuff.Mode.MULTIPLY);
            }
            ivRedo.setVisibility(View.VISIBLE);
        } else {
            ivUndo.setVisibility(View.GONE);
            ivRedo.setVisibility(View.GONE);
        }
    }

    private void onUndoButtonClicked() {
        onImageEditPreviewFragmentListener.undoToPrevImage(imageIndex);
    }

    private void onRedoButtonClicked() {
        onImageEditPreviewFragmentListener.redoToPrevImage(imageIndex);
    }

    private void initUCrop(final View view) {
        uCropView = view.findViewById(R.id.ucrop);
        gestureCropImageView = uCropView.getCropImageView();

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
                onImageEditPreviewFragmentListener.setRotateAngle(currentAngle);
            }

            @Override
            public void onScale(float currentScale) {
                // no scale Text shown.
            }
        });

        if (brightness != 0) {
            gestureCropImageView.setColorFilter(imageEditPreviewPresenter.getBrightnessMatrix(brightness / BRIGHTNESS_PRECISION));
        }

        if (contrast != INITIAL_CONTRAST_VALUE) {
            gestureCropImageView.setColorFilter(imageEditPreviewPresenter.getContrastMatrix(contrast / CONTRAST_PRECISION));
        }
    }

    public float getBrightness() {
        return brightness;
    }

    public float getContrast() {
        return contrast;
    }

    public void setBrightness(float brightnessValue) {
        if (brightness != brightnessValue) {
            this.brightness = brightnessValue;
            imageEditPreviewPresenter.getDebounceBrightnessMatrix(brightnessValue / BRIGHTNESS_PRECISION);
        }
    }

    public void setContrast(float contrastValue) {
        if (contrast != contrastValue) {
            this.contrast = contrastValue;
            imageEditPreviewPresenter.getDebounceContrastMatrix(contrastValue / CONTRAST_PRECISION);
        }
    }

    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public void onSuccessGetBrightnessMatrix(ColorMatrixColorFilter colorMatrixColorFilter) {
        gestureCropImageView.setColorFilter(colorMatrixColorFilter);
    }

    @Override
    public void onErrorGetBrightnessMatrix(Throwable e) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void onSuccessGetContrastMatrix(ColorMatrixColorFilter colorMatrixColorFilter) {
        gestureCropImageView.setColorFilter(colorMatrixColorFilter);
    }

    @Override
    public void onErrorGetContrastMatrix(Throwable e) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getContext(), e));
    }

    public void cropAndSaveImage() {
        if (gestureCropImageView.getCurrentAngle() == 0 &&
                gestureCropImageView.getCurrentScale() == gestureCropImageView.getMinScale()) {
            onImageEditPreviewFragmentListener.onEditDoNothing();
            return;
        }
        uCropView.getCropImageView().cropAndSaveImage(
                ImageUtils.isPng(edittedImagePath) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, new BitmapCropCallback() {
                    @Override
                    public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                        onImageEditPreviewFragmentListener.onSuccessSaveEditImage(resultUri.getPath());
                    }

                    @Override
                    public void onCropFailure(@NonNull Throwable t) {
                        onImageEditPreviewFragmentListener.onErrorSaveEditImage(t);
                    }
                });
    }

    public void saveBrightnessImage() {
        if (brightness == 0) {
            onImageEditPreviewFragmentListener.onEditDoNothing();
            return;
        }
        Bitmap bitmap = gestureCropImageView.getViewBitmap();
        imageEditPreviewPresenter.saveBrightnessImage(bitmap, brightness / BRIGHTNESS_PRECISION, ImageUtils.isPng(edittedImagePath));
    }

    public void saveContrastImage() {
        if (contrast == INITIAL_CONTRAST_VALUE) {
            onImageEditPreviewFragmentListener.onEditDoNothing();
            return;
        }
        Bitmap bitmap = gestureCropImageView.getViewBitmap();
        imageEditPreviewPresenter.saveContrastImage(bitmap, contrast / CONTRAST_PRECISION, ImageUtils.isPng(edittedImagePath));
    }

    @Override
    public void onErrorSaveBrightnessImage(Throwable e) {
        onImageEditPreviewFragmentListener.onErrorSaveEditImage(e);
    }

    @Override
    public void onSuccessSaveBrightnessImage(String filePath) {
        brightness = 0;
        onImageEditPreviewFragmentListener.onSuccessSaveEditImage(filePath);
    }


    @Override
    public void onErrorSaveContrastImage(Throwable e) {
        onImageEditPreviewFragmentListener.onErrorSaveEditImage(e);
    }

    @Override
    public void onSuccessSaveContrastImage(String filePath) {
        contrast = INITIAL_CONTRAST_VALUE;
        onImageEditPreviewFragmentListener.onSuccessSaveEditImage(filePath);
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
            gestureCropImageView.setImageUri(inputUri,
                    Uri.parse(ImageUtils.getTokopediaPhotoPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE, edittedImagePath).toString()));
        } catch (Exception e) {

        }
    }

    private void processOptions() {

        gestureCropImageView = uCropView.getCropImageView();
        overlayView = uCropView.getOverlayView();

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

        float scaleMultiplier;
        if (maxWidthHeight > minResolution) {
            scaleMultiplier = (float) maxWidthHeight / minResolution;
        } else {
            scaleMultiplier = 1;
        }
        gestureCropImageView.setMaxScaleMultiplier(scaleMultiplier);

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

        gestureCropImageView.setTargetAspectRatio((float) expectedRatioX / expectedRatioY);

        int maxSizeX = ImageUtils.DEF_WIDTH;
        int maxSizeY = ImageUtils.DEF_HEIGHT;
        gestureCropImageView.setMaxResultImageSizeX(maxSizeX);
        gestureCropImageView.setMaxResultImageSizeY(maxSizeY);
    }

    public void resetRotation() {
        gestureCropImageView.postRotate(-gestureCropImageView.getCurrentAngle());
        gestureCropImageView.setImageToWrapCropBounds();
    }

    public void rotateByAngle(float angle) {
        gestureCropImageView.postRotate(angle);
        gestureCropImageView.setImageToWrapCropBounds();
    }

    public void onStartEditScrolled() {
        gestureCropImageView.cancelAllAnimations();
    }

    public void onEndEditScrolled() {
        gestureCropImageView.setImageToWrapCropBounds();
    }

    public void editRotateScrolled(float delta) {
        gestureCropImageView.postRotate(delta / ROTATE_WIDGET_SENSITIVITY);
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
                overlayView.setShowCropGrid(true);
            } else {
                blockingView.setVisibility(View.VISIBLE);
                overlayView.setShowCropGrid(false);
            }
            overlayView.invalidate();
        }
    }

    public void cancelCropRotateImage() {
        gestureCropImageView.postRotate(-gestureCropImageView.getCurrentAngle());
        gestureCropImageView.zoomOutImage(gestureCropImageView.getMinScale() + 0.1f);
        gestureCropImageView.setImageToWrapCropBounds(false);
    }

    public void cancelBrightness() {
        gestureCropImageView.clearColorFilter();
        brightness = 0;
    }

    public void cancelContrast() {
        gestureCropImageView.clearColorFilter();
        contrast = INITIAL_CONTRAST_VALUE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageEditPreviewPresenter != null) {
            imageEditPreviewPresenter.detachView();
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

    protected void onAttachActivity(Context context) {
        onImageEditPreviewFragmentListener = (OnImageEditPreviewFragmentListener) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat(SAVED_BRIGHTNESS, brightness);
        outState.putFloat(SAVED_CONTRAST, contrast);
    }

}
