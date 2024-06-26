package com.tokopedia.imagepicker.editor.main.view;

import static com.tokopedia.imagepicker.editor.main.Constant.BRIGHTNESS_PRECISION;
import static com.tokopedia.imagepicker.editor.main.Constant.CONTRAST_PRECISION;
import static com.tokopedia.imagepicker.editor.main.Constant.INITIAL_CONTRAST_VALUE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImageRatioType;
import com.tokopedia.imagepicker.editor.di.DaggerImageEditorComponent;
import com.tokopedia.imagepicker.editor.di.module.ImageEditorModule;
import com.tokopedia.imagepicker.editor.presenter.ImageEditPreviewPresenter;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.image.ImageProcessingUtil;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.inject.Inject;

import kotlin.Pair;

/**
 * Created by hendry on 25/04/18.
 */

public class ImageEditPreviewFragment extends Fragment implements ImageEditPreviewPresenter.ImageEditPreviewView {

    public static final String ARG_IMAGE_INDEX = "arg_img_index";
    public static final String ARG_IMAGE_PATH = "arg_img_path";
    public static final String ARG_MIN_RESOLUTION = "arg_min_resolution";
    public static final String ARG_CIRCLE_PREVIEW = "arg_circle_preview";
    public static final int ROTATE_WIDGET_SENSITIVITY = 1;

    private String edittedImagePath;
    private int minResolution = 0, removeBackgroundColor = 0;
    private boolean isCirclePreview, hasRequestRemoveBackground = false;
    private float brightness = 0, contrast = INITIAL_CONTRAST_VALUE;

    private View progressBar;
    private View bgShadow;

    private UCropView uCropView;
    private View blockingView;

    private OnImageEditPreviewFragmentListener onImageEditPreviewFragmentListener;
    private boolean loadCompleted;
    private GestureCropImageView gestureCropImageView;
    private OverlayView overlayView;

    private ImageView ivUndo;
    private ImageView ivRedo;

    @Inject ImageEditPreviewPresenter imageEditPreviewPresenter;

    private int imageIndex;
    private Pair<Integer, Integer> widthHeight;
    private Bitmap[] listOutputWatermark;

    private UserSessionInterface userSession;
    private Bitmap lastStateImage;

    private String removeBackgroundResultPath = "";
    private int maxRetry = 3;

    public interface OnImageEditPreviewFragmentListener {
        boolean isInEditCropMode();

        void onEditDoNothing();

        void onSuccessSaveEditImage(String path);

        void onSuccessSaveWatermarkImage();

        void onErrorSaveEditImage(Throwable throwable);

        void setRotateAngle(float angle);

        void undoToPrevImage(int imageIndex);

        void redoToPrevImage(int imageIndex);

        boolean hasHistory(int imageIndex);

        boolean canUndo(int imageIndex);

        boolean canRedo(int imageIndex);

        ImageRatioType getCurrentRatio();

        void itemSelectionWidgetPreview(Bitmap[] bitmap);
    }

    public static ImageEditPreviewFragment newInstance(
            int imageIndex,
            String imagePath,
            int minResolution,
            boolean isCirclePreview,
            UserSessionInterface userSession
    ) {
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_INDEX, imageIndex);
        args.putString(ARG_IMAGE_PATH, imagePath);
        args.putInt(ARG_MIN_RESOLUTION, minResolution);
        args.putBoolean(ARG_CIRCLE_PREVIEW, isCirclePreview);
        ImageEditPreviewFragment fragment = new ImageEditPreviewFragment();
        fragment.userSession = userSession;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        brightness = 0;
        contrast = INITIAL_CONTRAST_VALUE;

        super.onCreate(savedInstanceState);
        initInjector();
        imageEditPreviewPresenter.attachView(this);
        imageEditPreviewPresenter.subscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_edit_preview, container, false);

        Bundle bundle = getArguments();
        imageIndex = bundle.getInt(ARG_IMAGE_INDEX);
        minResolution = bundle.getInt(ARG_MIN_RESOLUTION);
        edittedImagePath = bundle.getString(ARG_IMAGE_PATH);
        isCirclePreview = bundle.getBoolean(ARG_CIRCLE_PREVIEW);

        initUCrop(view);
        initLoader(view);

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

    public void renderUndoRedo() {
        if (onImageEditPreviewFragmentListener.hasHistory(imageIndex) && !onImageEditPreviewFragmentListener.isInEditCropMode()) {

            if (onImageEditPreviewFragmentListener.canUndo(imageIndex)) {
                ivUndo.getDrawable().clearColorFilter();
            } else {
                ivUndo.getDrawable().setColorFilter(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN600),
                        PorterDuff.Mode.MULTIPLY);
            }
            ivUndo.setVisibility(View.VISIBLE);

            if (onImageEditPreviewFragmentListener.canRedo(imageIndex)) {
                ivRedo.getDrawable().clearColorFilter();
            } else {
                ivRedo.getDrawable().setColorFilter(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN600),
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

    public void saveLastStateBitmap() {
        lastStateImage = gestureCropImageView.getViewBitmap();
    }

    public void setWatermark() {
        cancelWatermark();

        Bitmap bitmap = gestureCropImageView.getViewBitmap();
        String userInfo;

        try {
            userInfo = userSession.hasShop() ? userSession.getShopName() : userSession.getName();
        } catch (Exception e) {
            userInfo = "";
        }

        imageEditPreviewPresenter.setTokopediaWatermark(userInfo, bitmap);
    }

    private void setRemoveBackgroundCurrentBitmap() {
        showLoadingAndHidePreview();

        Bitmap bitmap = gestureCropImageView.getViewBitmap();
        Bitmap.CompressFormat compressFormat = ImageProcessingUtil.getCompressFormat(edittedImagePath);

        imageEditPreviewPresenter.setRemoveBackground(bitmap, compressFormat);
    }

    public void setRemoveBackground(int color) {
        removeBackgroundColor = color;

        if (hasRequestRemoveBackground && !removeBackgroundResultPath.isEmpty()) {
            setImageData(removeBackgroundResultPath);
            gestureCropImageView.setBackgroundColor(removeBackgroundColor);
            return;
        }

        setRemoveBackgroundCurrentBitmap();
    }

    public void saveRemoveBackground() {
        if (removeBackgroundColor == 0) {
            onImageEditPreviewFragmentListener.onEditDoNothing();
            return;
        }

        Bitmap.CompressFormat compressFormat = ImageProcessingUtil.getCompressFormat(edittedImagePath);
        imageEditPreviewPresenter.saveRemoveBackground(gestureCropImageView, compressFormat, 0);
    }

    public void cancelRemoveBackground() {
        imageEditPreviewPresenter.unsubscribe();
        hasRequestRemoveBackground = false;

        resetRemoveBackgroundBitmap();
        hideLoadingAndShowPreview();
    }

    public void resetRemoveBackgroundBitmap() {
        removeBackgroundColor = 0;

        if (lastStateImage == null) return;
        gestureCropImageView.setImageBitmap(lastStateImage);
    }

    @Override
    public void onSuccessSaveRemoveBackground(String filePath) {
        onImageEditPreviewFragmentListener.onSuccessSaveEditImage(filePath);
    }

    @Override
    public void onErrorSaveRemoveBackground(Throwable e) {
        NetworkErrorHelper.showRedCloseSnackbar(
                getActivity(),
                ErrorHandler.getErrorMessage(getContext(), e)
        );
    }

    @Override
    public void onSuccessGetRemoveBackground(String filePath) {
        hideLoadingAndShowPreview();

        hasRequestRemoveBackground = true;
        removeBackgroundResultPath = filePath;

        gestureCropImageView.setBackgroundColor(removeBackgroundColor);

        setImageData(filePath);
    }

    @Override
    public void onErrorGetRemoveBackground(Throwable e) {
        if (maxRetry == 0) {
            onImageEditPreviewFragmentListener.onErrorSaveEditImage(e);
            return;
        }

        View rootView = requireView().getRootView();

        if (rootView != null) {
            hideLoadingAndShowPreview();

            Toaster.build(
                    rootView,
                    getString(com.tokopedia.imagepicker.R.string.editor_remove_bg_error_message),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.imagepicker.R.string.editor_retry_button),
                    v -> {
                        setRemoveBackgroundCurrentBitmap();
                        maxRetry--;
                    }
            ).show();
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
                ImageProcessingUtil.isPng(edittedImagePath) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, new BitmapCropCallback() {
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

    public void rotateAndSaveImage() {
        if (gestureCropImageView.getCurrentAngle() == 0) {
            onImageEditPreviewFragmentListener.onEditDoNothing();
            return;
        }
        // when rotate 90/180/270, we just rotate and do not crop the image.
        if (gestureCropImageView.getCurrentAngle() % 90 == 0) {
            imageEditPreviewPresenter.rotateImage(gestureCropImageView.getViewBitmap(),
                    gestureCropImageView.getCurrentAngle(),
                    ImageProcessingUtil.getCompressFormat(edittedImagePath));
            return;
        }
        cropAndSaveImage();
    }

    public void saveBrightnessImage() {
        if (brightness == 0) {
            onImageEditPreviewFragmentListener.onEditDoNothing();
            return;
        }
        Bitmap bitmap = gestureCropImageView.getViewBitmap();
        imageEditPreviewPresenter.saveBrightnessImage(bitmap, brightness / BRIGHTNESS_PRECISION, ImageProcessingUtil.getCompressFormat(edittedImagePath));
    }

    public void saveContrastImage() {
        if (contrast == INITIAL_CONTRAST_VALUE) {
            onImageEditPreviewFragmentListener.onEditDoNothing();
            return;
        }
        Bitmap bitmap = gestureCropImageView.getViewBitmap();
        imageEditPreviewPresenter.saveContrastImage(bitmap, contrast / CONTRAST_PRECISION, ImageProcessingUtil.getCompressFormat(edittedImagePath));
    }

    public void saveWatermarkImage() {
        Bitmap bitmap = gestureCropImageView.getViewBitmap();
        imageEditPreviewPresenter.saveCurrentBitmapImage(bitmap, ImageProcessingUtil.getCompressFormat(edittedImagePath));
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

    @Override
    public void onErrorWatermarkImage(Throwable e) {
        onImageEditPreviewFragmentListener.onErrorSaveEditImage(e);
    }

    @Override
    public void onSuccessSaveWatermarkImage(String filePath) {
        onImageEditPreviewFragmentListener.onSuccessSaveEditImage(filePath);
        onImageEditPreviewFragmentListener.onSuccessSaveWatermarkImage();
        if (listOutputWatermark != null)
        for(Bitmap bitmap: listOutputWatermark) {
            if (!bitmap.isRecycled()) bitmap.recycle();
            listOutputWatermark = null;
        }
    }

    @Override
    public void onSuccessGetWatermarkImage(Bitmap[] bitmap) {
        listOutputWatermark = bitmap.clone();
        Arrays.fill(bitmap, null);
        gestureCropImageView.setImageBitmap(listOutputWatermark[0]);
        onImageEditPreviewFragmentListener.itemSelectionWidgetPreview(listOutputWatermark);
    }

    void setPreviewImageWatermark(Bitmap bitmap) {
        gestureCropImageView.setImageBitmap(bitmap);
    }

    private void initLoader(View view) {
        progressBar = view.findViewById(R.id.progressbar);
        bgShadow = view.findViewById(R.id.bg_shadow);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoadingAndHidePreview();
        processOptions();
        setImageData(edittedImagePath);
    }

    void setImageData(String filePath) {
        Uri inputUri = Uri.fromFile(new File(filePath));
        Uri outputUri = Uri.parse(ImageProcessingUtil.getTokopediaPhotoPath(filePath).toString());

        try {
            gestureCropImageView.setImageUri(inputUri, outputUri);
        } catch (Exception ignored) { }
    }

    private void processOptions() {

        gestureCropImageView = uCropView.getCropImageView();
        overlayView = uCropView.getOverlayView();

        // Crop image view options
        gestureCropImageView.setRotateEnabled(false);

        // set preview max bitmap, so it will fit the screen can also be zoomed.
        // default: BitmapLoadUtils.calculateMaxBitmapSize(getContext());
        int maxPreviewWidth = ImageProcessingUtil.DEF_WIDTH;
        gestureCropImageView.setMaxBitmapSize(maxPreviewWidth);

        // set max scale so it cannnot be zoomed under min resolution
        // same logic with the calculateInSampleSize;
        widthHeight = ImageProcessingUtil.getWidthAndHeight(edittedImagePath);
        int maxWidthHeight = Math.max(widthHeight.getFirst(), widthHeight.getSecond());
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

        overlayView.setDimmedColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950_32));
        overlayView.setCircleDimmedLayer(isCirclePreview);

        overlayView.setShowCropFrame(OverlayView.DEFAULT_SHOW_CROP_FRAME);
        overlayView.setCropFrameColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0));
        overlayView.setCropFrameStrokeWidth(getContext().getResources().getDimensionPixelSize(R.dimen.image_picker_dp_1));

        overlayView.setShowCropGrid(OverlayView.DEFAULT_SHOW_CROP_GRID);
        overlayView.setCropGridRowCount(OverlayView.DEFAULT_CROP_GRID_ROW_COUNT);
        overlayView.setCropGridColumnCount(OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT);
        overlayView.setCropGridColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0_68));
        overlayView.setCropGridStrokeWidth(getContext().getResources().getDimensionPixelSize(R.dimen.image_picker_dp_1));

        setToInitialRatio();

        int maxSizeX = ImageProcessingUtil.DEF_WIDTH;
        int maxSizeY = ImageProcessingUtil.DEF_HEIGHT;
        gestureCropImageView.setMaxResultImageSizeX(maxSizeX);
        gestureCropImageView.setMaxResultImageSizeY(maxSizeY);
    }

    private void setToInitialRatio(){
        ImageRatioType imageRatioTypeDef = onImageEditPreviewFragmentListener.getCurrentRatio();
        setPreviewCropTo(imageRatioTypeDef);
    }

    public void rotateByAngle(float angle) {
        try {
            gestureCropImageView.postRotate(angle);
            gestureCropImageView.setImageToWrapCropBounds();
        } catch (Exception e) {
            // Do nothing
        }
    }

    public void onStartEditScrolled() {
        gestureCropImageView.cancelAllAnimations();
    }

    public void onEndEditScrolled() {
        resetZoom();
        gestureCropImageView.setImageToWrapCropBounds(false);
    }

    public void editRotateScrolled(float delta) {
        gestureCropImageView.postRotate(delta / ROTATE_WIDGET_SENSITIVITY);
    }

    public void setPreviewCropTo(ImageRatioType imageRatioTypeDef){
        int ratioX = imageRatioTypeDef.getRatioX();
        int ratioY = imageRatioTypeDef.getRatioY();
        if (ratioX <= 0 || ratioY <= 0) { // original ratio
            gestureCropImageView.setTargetAspectRatio((float) widthHeight.getFirst() / widthHeight.getSecond());
        } else {
            gestureCropImageView.setTargetAspectRatio((float) ratioX / ratioY);
        }
        resetZoom();
        gestureCropImageView.setImageToWrapCropBounds(false);
    }

    private void showLoadingAndHidePreview() {
        bgShadow.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        blockingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingAndShowPreview() {
        bgShadow.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        setEditCropMode(onImageEditPreviewFragmentListener.isInEditCropMode());
    }

    public void setEditCropMode(boolean isEditMode) {
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
        resetZoom();
        gestureCropImageView.setImageToWrapCropBounds(false);
    }

    public void cancelCropImage() {
        setToInitialRatio();
    }

    public void resetZoom(){
        if (gestureCropImageView.getMinScale() > 0) {
            gestureCropImageView.zoomOutImage(gestureCropImageView.getMinScale() + 0.01f);
        }
    }

    public void cancelWatermark() {
        if (listOutputWatermark != null) {
            for(Bitmap bitmap: listOutputWatermark) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                listOutputWatermark = null;
            }
        }
        if (lastStateImage == null) return;
        gestureCropImageView.setImageBitmap(lastStateImage);
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

    private void initInjector() {
        DaggerImageEditorComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .imageEditorModule(new ImageEditorModule())
                .build()
                .inject(this);
    }

    protected void onAttachActivity(Context context) {
        onImageEditPreviewFragmentListener = (OnImageEditPreviewFragmentListener) context;
    }

}
