package com.tokopedia.imagepicker.editor.main.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.common.widget.NonSwipeableViewPager;
import com.tokopedia.imagepicker.editor.adapter.ImageEditorViewPagerAdapter;
import com.tokopedia.imagepicker.editor.presenter.ImageEditorPresenter;
import com.tokopedia.imagepicker.editor.widget.ImageEditActionMainWidget;
import com.tokopedia.imagepicker.editor.widget.ImageEditThumbnailListWidget;
import com.tokopedia.imagepicker.editor.widget.TwoLineSeekBar;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;

import java.io.File;
import java.util.ArrayList;

import static com.tokopedia.imagepicker.editor.main.Constant.BRIGHTNESS_PRECISION;
import static com.tokopedia.imagepicker.editor.main.Constant.HALF_BRIGHTNESS_RANGE;
import static com.tokopedia.imagepicker.editor.main.Constant.HALF_CONTRAST_RANGE;
import static com.tokopedia.imagepicker.editor.main.Constant.HALF_ROTATE_RANGE;
import static com.tokopedia.imagepicker.editor.main.Constant.INITIAL_CONTRAST_VALUE;

/**
 * Created by Hendry on 9/25/2017.
 */

public class ImageEditorActivity extends BaseSimpleActivity implements ImageEditorPresenter.ImageEditorView,
        ImageEditPreviewFragment.OnImageEditPreviewFragmentListener, ImageEditThumbnailListWidget.OnImageEditThumbnailListWidgetListener, ImageEditActionMainWidget.OnImageEditActionMainWidgetListener {

    public static final String EXTRA_IMAGE_URLS = "IMG_URLS";
    public static final String EXTRA_MIN_RESOLUTION = "MIN_IMG_RESOLUTION";
    public static final String EXTRA_EDIT_ACTION_TYPE = "EDIT_ACTION_TYPE";
    public static final String EXTRA_RATIO_X = "RATIO_X";
    public static final String EXTRA_RATIO_Y = "RATIO_Y";
    public static final String EXTRA_IS_CIRCLE_PREVIEW = "IS_CIRCLE_PREVIEW";

    public static final String SAVED_IMAGE_INDEX = "IMG_IDX";
    public static final String SAVED_EDITTED_PATHS = "SAVED_EDITTED_PATHS";
    public static final String SAVED_CURRENT_STEP_INDEX = "SAVED_STEP_INDEX";
    public static final String SAVED_IN_EDIT_MODE = "SAVED_IN_EDIT_MODE";
    public static final String SAVED_EDIT_TYPE = "SAVED_EDIT_TYPE";

    public static final String EDIT_RESULT_PATHS = "result_paths";

    public static final int MAX_HISTORY_PER_IMAGE = 5;

    private ArrayList<String> extraImageUrls;
    private int minResolution;
    private @ImageEditActionTypeDef
    int[] imageEditActionType;

    private ArrayList<ArrayList<String>> edittedImagePaths;

    // for undo
    private ArrayList<Integer> currentEditStepIndexList;

    private int currentImageIndex;
    private boolean isInEditMode;
    private @ImageEditActionTypeDef
    int currentEditActionType;
    private int ratioX, ratioY;
    private boolean isCirclePreview;

    private View vgDownloadProgressBar;
    private ImageEditorPresenter imageEditorPresenter;

    private View vgContentContainer;
    private NonSwipeableViewPager viewPager;

    private ImageEditorViewPagerAdapter imageEditorViewPagerAdapter;
    private ImageEditThumbnailListWidget imageEditThumbnailListWidget;
    private ImageEditActionMainWidget imageEditActionMainWidget;
    private View editorMainView;
    private View editorControlView;
    private View editCancelView;
    private View editSaveView;
    private View doneButton;
    private View vEditProgressBar;
    private View blockingView;
    private View layoutBrightness;
    private View layoutContrast;
    private View layoutCrop;
    private View layoutRotate;
    private ProgressDialog progressDialog;
    private TwoLineSeekBar brightnessSeekbar;
    private TwoLineSeekBar contrastSeekbar;
    private TwoLineSeekBar rotateSeekbar;
    private TextView tvBrightness;
    private TextView tvContrast;
    private TextView tvActionTitle;

    public static Intent getIntent(Context context, ArrayList<String> imageUrls, int minResolution,
                                   @ImageEditActionTypeDef int[] imageEditActionType,
                                   int ratioX, int ratioY,
                                   boolean isCirclePreview) {
        Intent intent = new Intent(context, ImageEditorActivity.class);
        intent.putExtra(EXTRA_IMAGE_URLS, imageUrls);
        intent.putExtra(EXTRA_MIN_RESOLUTION, minResolution);
        intent.putExtra(EXTRA_EDIT_ACTION_TYPE, imageEditActionType);
        intent.putExtra(EXTRA_RATIO_X, ratioX);
        intent.putExtra(EXTRA_RATIO_Y, ratioY);
        intent.putExtra(EXTRA_IS_CIRCLE_PREVIEW, isCirclePreview);
        return intent;
    }

    public static Intent getIntent(Context context, String imageUrl, int minResolution,
                                   @ImageEditActionTypeDef int[] imageEditActionType,
                                   int ratioX, int ratioY,
                                   boolean isCirclePreview) {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(imageUrl);
        return getIntent(context, imageUrls, minResolution, imageEditActionType, ratioX, ratioY, isCirclePreview);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_image_editor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        // For test:
        // extraImageUrls = new ArrayList<>();
        // extraImageUrls.add("https://scontent-sit4-1.cdninstagram.com/vp/4d462c7e62452e54862602872a4f2f55/5B772ADA/t51.2885-15/e35/30603662_2044572549200360_6725615414816014336_n.jpg");
        if (intent.hasExtra(EXTRA_IMAGE_URLS)) {
            extraImageUrls = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS);
        } else {
            finish();
            return;
        }
        minResolution = intent.getIntExtra(EXTRA_MIN_RESOLUTION, 0);
        imageEditActionType = intent.getIntArrayExtra(EXTRA_EDIT_ACTION_TYPE);
        ratioX = intent.getIntExtra(EXTRA_RATIO_X, 1);
        ratioY = intent.getIntExtra(EXTRA_RATIO_Y, 1);
        isCirclePreview = intent.getBooleanExtra(EXTRA_IS_CIRCLE_PREVIEW, false);

        if (savedInstanceState == null) {
            currentImageIndex = 0;
            edittedImagePaths = new ArrayList<>();
            isInEditMode = false;
            currentEditActionType = ImageEditActionTypeDef.ACTION_CROP_ROTATE;
            currentEditStepIndexList = new ArrayList<>();
        } else {
            currentImageIndex = savedInstanceState.getInt(SAVED_IMAGE_INDEX, 0);
            //noinspection unchecked
            edittedImagePaths = (ArrayList<ArrayList<String>>) savedInstanceState.getSerializable(SAVED_EDITTED_PATHS);
            isInEditMode = savedInstanceState.getBoolean(SAVED_IN_EDIT_MODE);
            currentEditActionType = savedInstanceState.getInt(SAVED_EDIT_TYPE);
            currentEditStepIndexList = savedInstanceState.getIntegerArrayList(SAVED_CURRENT_STEP_INDEX);
        }

        super.onCreate(savedInstanceState);

        vgDownloadProgressBar = findViewById(R.id.vg_download_progress_bar);
        vgContentContainer = findViewById(R.id.vg_content_container);

        viewPager = findViewById(R.id.view_pager);
        editorMainView = findViewById(R.id.vg_editor_main);
        editorControlView = findViewById(R.id.vg_editor_control);
        editCancelView = findViewById(R.id.tv_edit_cancel);
        editSaveView = findViewById(R.id.tv_edit_save);
        imageEditActionMainWidget = findViewById(R.id.image_edit_action_main_widget);
        imageEditThumbnailListWidget = findViewById(R.id.image_edit_thumbnail_list_widget);
        doneButton = findViewById(R.id.tv_done);
        vEditProgressBar = findViewById(R.id.crop_progressbar);
        blockingView = findViewById(R.id.crop_blocking_view);
        layoutCrop = findViewById(R.id.layout_crop);
        layoutRotate = findViewById(R.id.layout_rotate);
        layoutBrightness = findViewById(R.id.layout_brightness);
        layoutContrast = findViewById(R.id.layout_contrast);
        tvActionTitle = findViewById(R.id.tv_action_title);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentImageIndex = position;
                imageEditThumbnailListWidget.setIndex(currentImageIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        editCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelEditClicked();
            }
        });
        editSaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveEditClicked();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneButtonClicked();
            }
        });
    }

    private void onCancelEditClicked() {
        ImageEditPreviewFragment fragment = getCurrentFragment();
        if (fragment != null) {
            switch (currentEditActionType) {
                case ImageEditActionTypeDef.ACTION_CROP:
                case ImageEditActionTypeDef.ACTION_ROTATE:
                case ImageEditActionTypeDef.ACTION_CROP_ROTATE:
                    fragment.cancelCropRotateImage();
                    break;
                case ImageEditActionTypeDef.ACTION_WATERMARK:
                    //TODO undo watermark here
                    break;
                case ImageEditActionTypeDef.ACTION_BRIGHTNESS:
                    fragment.cancelBrightness();
                    break;
                case ImageEditActionTypeDef.ACTION_CONTRAST:
                    fragment.cancelContrast();
                    break;
            }

        }
        setupEditMode(false, ImageEditActionTypeDef.ACTION_CROP_ROTATE);
    }

    private void onSaveEditClicked() {
        if (isInEditMode) {
            showEditLoading();
            ImageEditPreviewFragment fragment = getCurrentFragment();
            switch (currentEditActionType) {
                case ImageEditActionTypeDef.ACTION_ROTATE:
                    if (fragment != null) {
                        fragment.rotateAndSaveImage();
                    }
                    break;
                case ImageEditActionTypeDef.ACTION_CROP:
                case ImageEditActionTypeDef.ACTION_CROP_ROTATE:
                    if (fragment != null) {
                        fragment.cropAndSaveImage();
                    }
                    break;
                case ImageEditActionTypeDef.ACTION_WATERMARK:
                    // currently not supported
                    break;
                case ImageEditActionTypeDef.ACTION_BRIGHTNESS:
                    if (fragment != null) {
                        fragment.saveBrightnessImage();
                    }
                    break;
                case ImageEditActionTypeDef.ACTION_CONTRAST:
                    if (fragment != null) {
                        fragment.saveContrastImage();
                    }
                    break;
            }
        }
    }

    @Override
    public void onSuccessSaveEditImage(String path) {
        hideEditLoading();
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        // check the size is must be higher than the minimum resolution
        // we need to recheck this even though the maxScale has been set (in case there is OOM)
        int resultMinResolution = ImageUtils.getMinResolution(file);
        if (resultMinResolution < minResolution) {
            file.delete();
            NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.image_under_x_resolution, minResolution));
            return;
        }

        // it is not on the last node on the step
        if (getMaxStepForCurrentImage() != getCurrentStepForCurrentImage() + 1) {
            //discard the next file to size
            for (int j = getMaxStepForCurrentImage() - 1; j > getCurrentStepForCurrentImage(); j--) {
                //delete the file, so we can reserve space more
                String pathToDelete = edittedImagePaths.get(currentImageIndex).get(j);
                ImageUtils.deleteFile(pathToDelete);
                edittedImagePaths.get(currentImageIndex).remove(j);
            }
        }
        // append the path to array
        int lastEmptyStep = getCurrentStepForCurrentImage() + 1;
        edittedImagePaths.get(currentImageIndex).add(path);
        currentEditStepIndexList.set(currentImageIndex, lastEmptyStep);

        // if already 5 steps or more, delete the step no 1, we don't want to spam the history.
        // step no 0 should not be deleted. Perhaps someday it is used for reset to very first node.
        if (lastEmptyStep > MAX_HISTORY_PER_IMAGE) {
            //delete the file, so we can reserve space more
            String stepNo1Path = edittedImagePaths.get(currentImageIndex).get(1);
            ImageUtils.deleteFile(stepNo1Path);

            edittedImagePaths.get(currentImageIndex).remove(1);
            //since the paths is removed by 1, decrease the lastStep by 1.
            currentEditStepIndexList.set(currentImageIndex, lastEmptyStep - 1);
        }

        refreshCurrentPage();
        imageEditThumbnailListWidget.notifyDataSetChanged();

        setupEditMode(false, ImageEditActionTypeDef.ACTION_CROP_ROTATE);
    }

    @Override
    public void onEditDoNothing() {
        hideEditLoading();
        setupEditMode(false, ImageEditActionTypeDef.ACTION_CROP_ROTATE);
    }

    private void refreshCurrentPage() {
        refreshPage(currentImageIndex);
    }

    private void refreshPage(int imageIndex) {
        imageEditorViewPagerAdapter.setCurrentEditStepIndexList(currentEditStepIndexList);
        imageEditorViewPagerAdapter.setEdittedImagePaths(edittedImagePaths);
        ImageEditPreviewFragment fragment = getFragment(imageIndex);
        if (fragment != null) {
            imageEditorViewPagerAdapter.destroyIndex(imageIndex);
            imageEditorViewPagerAdapter.notifyDataSetChanged();
        }
    }

    private int getCurrentStepForCurrentImage() {
        return currentEditStepIndexList.get(currentImageIndex);
    }

    private int getMaxStepForCurrentImage() {
        return edittedImagePaths.get(currentImageIndex).size();
    }

    @Override
    public void onErrorSaveEditImage(Throwable throwable) {
        hideEditLoading();
        NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(getContext(), throwable));
        onCancelEditClicked();
    }

    private ImageEditPreviewFragment getCurrentFragment() {
        return (ImageEditPreviewFragment) imageEditorViewPagerAdapter.getRegisteredFragment(currentImageIndex);
    }

    private ImageEditPreviewFragment getFragment(int imageIndex) {
        return (ImageEditPreviewFragment) imageEditorViewPagerAdapter.getRegisteredFragment(imageIndex);
    }

    private void showEditLoading() {
        vEditProgressBar.setVisibility(View.VISIBLE);
        blockingView.setVisibility(View.VISIBLE);
    }

    private void hideEditLoading() {
        vEditProgressBar.setVisibility(View.GONE);
        blockingView.setVisibility(View.GONE);
    }

    private void setupEditActionWidget() {
        imageEditActionMainWidget.setOnImageEditActionMainWidgetListener(this);
        imageEditActionMainWidget.setData(imageEditActionType);
    }

    @Override
    public void onEditActionClicked(@ImageEditActionTypeDef int editActionType) {
        setupEditMode(true, editActionType);
    }

    private void setupEditMode(boolean isInEditMode, int editActionType) {
        this.isInEditMode = isInEditMode;
        this.currentEditActionType = editActionType;

        viewPager.setCanSwipe(!isInEditMode);
        ImageEditPreviewFragment fragment = getCurrentFragment();
        if (isInEditMode) {
            editorMainView.setVisibility(View.GONE);
            editorControlView.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.GONE);

            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("");

            switch (editActionType) {
                case ImageEditActionTypeDef.ACTION_CROP:
                    if (fragment != null) {
                        fragment.setEditCropMode(true);
                    }
                    hideAllControls();
                    layoutCrop.setVisibility(View.VISIBLE);
                    tvActionTitle.setText(getString(R.string.crop));
                    break;
                case ImageEditActionTypeDef.ACTION_ROTATE:
                    hideAllControls();
                    setupRotateWidget();
                    layoutRotate.setVisibility(View.VISIBLE);
                    tvActionTitle.setText(getString(R.string.rotate));
                    break;
                case ImageEditActionTypeDef.ACTION_WATERMARK:
                    //currently not supported.
                    break;
                case ImageEditActionTypeDef.ACTION_CROP_ROTATE:
                    //currently not supported.
                    break;
                case ImageEditActionTypeDef.ACTION_BRIGHTNESS:
                    hideAllControls();
                    setupBrightnessWidget();
                    setUIBrightnessValue(0);
                    layoutBrightness.setVisibility(View.VISIBLE);
                    tvActionTitle.setText(getString(R.string.brightness));
                    break;
                case ImageEditActionTypeDef.ACTION_CONTRAST:
                    hideAllControls();
                    setupContrastWidget();
                    setUIContrastValue(INITIAL_CONTRAST_VALUE);
                    layoutContrast.setVisibility(View.VISIBLE);
                    tvActionTitle.setText(getString(R.string.contrast));
                    break;
            }
            tvActionTitle.setVisibility(View.VISIBLE);

            if (fragment != null) {
                fragment.renderUndoRedo();
            }

        } else {
            editorMainView.setVisibility(View.VISIBLE);
            editorControlView.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getTitle());
            tvActionTitle.setVisibility(View.GONE);
            if (fragment != null) {
                fragment.setEditCropMode(false);
                fragment.renderUndoRedo();
            }
        }
    }

    private void hideAllControls() {
        layoutCrop.setVisibility(View.GONE);
        layoutRotate.setVisibility(View.GONE);
        layoutBrightness.setVisibility(View.GONE);
        layoutContrast.setVisibility(View.GONE);
    }

    private void setupBrightnessWidget() {
        if (brightnessSeekbar == null) {
            brightnessSeekbar = findViewById(R.id.seekBar_brightness);
            brightnessSeekbar.reset();
            brightnessSeekbar.setSeekLength(-HALF_BRIGHTNESS_RANGE, HALF_BRIGHTNESS_RANGE, 0, 1f);
            brightnessSeekbar.setOnSeekChangeListener(new TwoLineSeekBar.OnSeekChangeListener() {
                @Override
                public void onSeekChanged(float previousValue, float value, float step) {
                    setUIBrightnessValue(value);
                    ImageEditPreviewFragment imageEditPreviewFragment = getCurrentFragment();
                    if (imageEditPreviewFragment != null) {
                        imageEditPreviewFragment.setBrightness(value);
                    }
                }

                @Override
                public void onSeekStopped(float value, float step) {
                    // no need to hide loading, etc.
                }
            });
            tvBrightness = findViewById(R.id.tv_brightness);
        }
    }

    private void setupRotateWidget() {
        if (rotateSeekbar == null) {
            rotateSeekbar = findViewById(R.id.seekBar_rotate);
            rotateSeekbar.reset();
            rotateSeekbar.setSeekLength(-HALF_ROTATE_RANGE, HALF_ROTATE_RANGE, 0, 1f);

            rotateSeekbar.setOnSeekChangeListener(new TwoLineSeekBar.OnSeekChangeListener() {
                @Override
                public void onSeekChanged(float previousValue, float value, float step) {
                    ImageEditPreviewFragment imageEditPreviewFragment = getCurrentFragment();
                    if (imageEditPreviewFragment != null) {
                        imageEditPreviewFragment.onStartEditScrolled();
                        imageEditPreviewFragment.editRotateScrolled(value - previousValue);
                        imageEditPreviewFragment.onEndEditScrolled();
                    }
                }

                @Override
                public void onSeekStopped(float value, float step) {
                    // no need to hide loading, etc.
                }
            });
            View vReset = findViewById(R.id.tv_reset);
            vReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageEditPreviewFragment imageEditPreviewFragment = getCurrentFragment();
                    if (imageEditPreviewFragment != null) {
                        imageEditPreviewFragment.cancelCropRotateImage();
                    }
                    rotateSeekbar.setValue(0);
                }
            });

            View vRotate90 = findViewById(R.id.iv_rotate_90);
            vRotate90.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageEditPreviewFragment imageEditPreviewFragment = getCurrentFragment();
                    if (imageEditPreviewFragment != null) {
                        imageEditPreviewFragment.rotateByAngle(90);
                    }
                }
            });
        }
        rotateSeekbar.setValue(0);
    }

    private void setupContrastWidget() {
        if (contrastSeekbar == null) {
            contrastSeekbar = findViewById(R.id.seekBar_contrast);
            contrastSeekbar.reset();
            contrastSeekbar.setSeekLength(INITIAL_CONTRAST_VALUE - HALF_CONTRAST_RANGE,
                    INITIAL_CONTRAST_VALUE + HALF_CONTRAST_RANGE,
                    INITIAL_CONTRAST_VALUE, 1f);
            contrastSeekbar.setOnSeekChangeListener(new TwoLineSeekBar.OnSeekChangeListener() {
                @Override
                public void onSeekChanged(float previousValue, float value, float step) {
                    setUIContrastValue(value);
                    ImageEditPreviewFragment imageEditPreviewFragment = getCurrentFragment();
                    if (imageEditPreviewFragment != null) {
                        imageEditPreviewFragment.setContrast(value);
                    }
                }

                @Override
                public void onSeekStopped(float value, float step) {
                    // no need to hide loading, etc.
                }
            });

            tvContrast = findViewById(R.id.tv_contrast);
        }
    }

    @Override
    public void setRotateAngle(float angle) {
        // update view when the angle is changed by pinching
        // currently no operation.
    }

    public void undoToPrevImage(int imageIndex) {
        int currentStep = currentEditStepIndexList.get(imageIndex);
        // check if can undo
        if (currentStep > 0) {
            currentEditStepIndexList.set(imageIndex, currentStep - 1);
            refreshPage(imageIndex);
            imageEditThumbnailListWidget.notifyDataSetChanged();
        }
    }

    @Override
    public void redoToPrevImage(int imageIndex) {
        int currentStep = currentEditStepIndexList.get(imageIndex);
        if (canRedo(imageIndex)) {
            currentEditStepIndexList.set(imageIndex, currentStep + 1);
            refreshPage(imageIndex);
            imageEditThumbnailListWidget.notifyDataSetChanged();
        }
    }

    @Override
    public boolean hasHistory(int imageIndex) {
        return edittedImagePaths.get(imageIndex).size() > 1;
    }

    @Override
    public boolean canUndo(int imageIndex) {
        return currentEditStepIndexList.get(imageIndex) > 0;
    }

    @Override
    public boolean canRedo(int imageIndex) {
        ArrayList<String> currentImagePaths = edittedImagePaths.get(imageIndex);
        int size = currentImagePaths.size();
        int currentStep = currentEditStepIndexList.get(imageIndex);
        return currentStep < size - 1;
    }

    public void setUIBrightnessValue(float brightnessValue) {
        tvBrightness.setText(String.valueOf((int) brightnessValue / BRIGHTNESS_PRECISION));
        if (brightnessSeekbar != null && brightnessSeekbar.getValue() != brightnessValue) {
            brightnessSeekbar.setValue(brightnessValue);
        }
    }

    public void setUIContrastValue(float contrastValue) {
        tvContrast.setText(String.valueOf((int) contrastValue - INITIAL_CONTRAST_VALUE));
        if (contrastSeekbar != null && contrastSeekbar.getValue() != contrastValue) {
            contrastSeekbar.setValue(contrastValue);
        }
    }

    private void setUpThumbnailPreview() {
        imageEditThumbnailListWidget.setOnImageEditThumbnailListWidgetListener(this);
        imageEditThumbnailListWidget.setData(edittedImagePaths, currentEditStepIndexList, currentImageIndex);
        imageEditThumbnailListWidget.setVisibility(edittedImagePaths.size() <= 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onThumbnailItemClicked(String imagePath, int position) {
        if (viewPager.getCurrentItem() != position) {
            viewPager.setCurrentItem(position);
        }
    }

    private void onDoneButtonClicked() {
        blockingView.setVisibility(View.VISIBLE);

        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0, sizei = edittedImagePaths.size(); i < sizei; i++) {
            resultList.add(edittedImagePaths.get(i).get(currentEditStepIndexList.get(i)));
        }

        showDoneLoading();
        initImageEditorPresenter();
        imageEditorPresenter.cropBitmapToExpectedRatio(resultList, ratioX, ratioY);
    }

    @Override
    public void onSuccessCropImageToRatio(ArrayList<String> cropppedImagePaths) {
        hideDoneLoading();
        ArrayList<String> resultList;
        try {
            resultList = ImageUtils.copyFiles(cropppedImagePaths, ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_EDIT_RESULT);
            ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE);
            ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA);
            Intent intent = new Intent();
            intent.putStringArrayListExtra(EDIT_RESULT_PATHS, resultList);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(this, e));
        }
    }

    @Override
    public void onErrorCropImageToRatio(Throwable e) {
        NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(this, e));
        hideDoneLoading();
    }

    private void showDoneLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.title_loading));
        }
        progressDialog.show();
    }

    private void hideDoneLoading() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onErrorDownloadImageToLocal(Throwable e) {
        hideProgressDialog();
        NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void onSuccessDownloadImageToLocal(ArrayList<String> localPaths) {
        hideProgressDialog();
        if (isResolutionValid(localPaths)) {
            copyToLocalUrl(localPaths);
            startEditLocalImages();
        } else {
            Toast.makeText(getContext(), getString(R.string.image_under_x_resolution, minResolution), Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    /**
     * to cater instagram bugs: resolution in api is not correct.
     */
    private boolean isResolutionValid(ArrayList<String> localPaths){
        for (String localPath: localPaths) {
            if (ImageUtils.getMinResolution(localPath) < minResolution) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // download network image url if needed
        if (edittedImagePaths == null || edittedImagePaths.size() == 0) {
            boolean hasNetworkImage = false;
            for (int i = 0, sizei = extraImageUrls.size(); i < sizei; i++) {
                if (URLUtil.isNetworkUrl(extraImageUrls.get(i))) {
                    hasNetworkImage = true;
                    break;
                }
            }
            if (hasNetworkImage) {
                showDownloadProgressDialog();
                hideContentView();
                initImageEditorPresenter();
                imageEditorPresenter.convertHttpPathToLocalPath(extraImageUrls);
            } else {
                copyToLocalUrl(extraImageUrls);
                startEditLocalImages();
            }
        } else {
            startEditLocalImages();
        }
    }

    private void initImageEditorPresenter() {
        if (imageEditorPresenter == null) {
            imageEditorPresenter = new ImageEditorPresenter();
            imageEditorPresenter.attachView(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (imageEditorPresenter != null) {
            imageEditorPresenter.detachView();
        }
    }

    private void startEditLocalImages() {
        showContentView();
        if (imageEditorViewPagerAdapter == null) {
            imageEditorViewPagerAdapter = new ImageEditorViewPagerAdapter(getSupportFragmentManager(),
                    edittedImagePaths,
                    currentEditStepIndexList,
                    minResolution,
                    ratioX, ratioY,
                    isCirclePreview);
            viewPager.setAdapter(imageEditorViewPagerAdapter);
        }
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(currentImageIndex);
            }
        });

        setupEditActionWidget();
        setupEditMode(isInEditMode, currentEditActionType);

        setUpThumbnailPreview();

    }

    private void showDownloadProgressDialog() {
        vgDownloadProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressDialog() {
        vgDownloadProgressBar.setVisibility(View.GONE);
    }

    private void hideContentView() {
        vgContentContainer.setVisibility(View.GONE);
        imageEditThumbnailListWidget.setVisibility(View.GONE);
        imageEditActionMainWidget.setVisibility(View.GONE);
    }

    private void showContentView() {
        vgContentContainer.setVisibility(View.VISIBLE);
        imageEditThumbnailListWidget.setVisibility(View.VISIBLE);
        imageEditActionMainWidget.setVisibility(View.VISIBLE);
    }


    private void copyToLocalUrl(ArrayList<String> imageUrls) {
        edittedImagePaths = new ArrayList<>();
        currentEditStepIndexList = new ArrayList<>(imageUrls.size());
        for (int i = 0, sizei = imageUrls.size(); i < sizei; i++) {
            ArrayList<String> stepArrayList = new ArrayList<>();
            stepArrayList.add(imageUrls.get(i));
            edittedImagePaths.add(stepArrayList);
            currentEditStepIndexList.add(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (isInEditMode) { //backpressed will cancel the edit mode
            onCancelEditClicked();
        } else {
            if (anyEditChanges()) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setMessage(getString(R.string.image_edit_backpressed_title))
                        .setPositiveButton(getString(R.string.label_return), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE);
                                ImageEditorActivity.super.onBackPressed();
                            }
                        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // no op, just dismiss
                            }
                        });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            } else {
                ImageEditorActivity.super.onBackPressed();
            }
        }
    }

    private boolean anyEditChanges() {
        if (edittedImagePaths!= null) {
            for (int i = 0, sizei = edittedImagePaths.size(); i < sizei; i++) {
                if (edittedImagePaths.get(i)!= null && edittedImagePaths.get(i).size() > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_IMAGE_INDEX, currentImageIndex);
        outState.putSerializable(SAVED_EDITTED_PATHS, edittedImagePaths);
        outState.putIntegerArrayList(SAVED_CURRENT_STEP_INDEX, currentEditStepIndexList);
        outState.putBoolean(SAVED_IN_EDIT_MODE, isInEditMode);
        outState.putInt(SAVED_EDIT_TYPE, currentEditActionType);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public boolean isInEditCropMode() {
        return isInEditMode && (currentEditActionType == ImageEditActionTypeDef.ACTION_CROP
                || currentEditActionType == ImageEditActionTypeDef.ACTION_CROP_ROTATE);
    }


}
