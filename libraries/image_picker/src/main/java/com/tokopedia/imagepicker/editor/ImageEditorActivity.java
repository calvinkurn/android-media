package com.tokopedia.imagepicker.editor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.URLUtil;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.widget.NonSwipeableViewPager;
import com.tokopedia.imagepicker.editor.adapter.ImageEditorViewPagerAdapter;
import com.tokopedia.imagepicker.editor.presenter.ImageDownloadPresenter;
import com.tokopedia.imagepicker.editor.widget.ImageEditActionMainWidget;
import com.tokopedia.imagepicker.editor.widget.ImageEditThumbnailListWidget;
import com.tokopedia.imagepicker.picker.main.util.ImagePickerBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hendry on 9/25/2017.
 */

public class ImageEditorActivity extends BaseSimpleActivity implements ImageDownloadPresenter.ImageDownloadView,
        ImageEditPreviewFragment.OnImageEditPreviewFragmentListener, ImageEditThumbnailListWidget.OnImageEditThumbnailListWidgetListener, ImageEditActionMainWidget.OnImageEditActionMainWidgetListener {

    public static final String EXTRA_IMAGE_URLS = "IMG_URLS";
    public static final String EXTRA_MIN_RESOLUTION = "MIN_IMG_RESOLUTION";
    public static final String EXTRA_EDIT_ACTION_TYPE = "EDIT_ACTION_TYPE";

    public static final String SAVED_IMAGE_INDEX = "IMG_IDX";
    public static final String SAVED_EDITTED_PATHS = "SAVED_EDITTED_PATHS";
    public static final String SAVED_CURRENT_STEP_INDEX = "SAVED_STEP_INDEX";
    public static final String SAVED_LOCAL_IMAGE_PATH = "SAVED_LOCAL_STEP_0";
    public static final String SAVED_IN_EDIT_MODE = "SAVED_IN_EDIT_MODE";
    public static final String SAVED_EDIT_TYPE = "SAVED_EDIT_TYPE";

    public static final String EDIT_RESULT_PATHS = "result_paths";


    private ArrayList<String> extraImageUrls;
    private int minResolution;
    private @ImagePickerBuilder.ImageEditActionTypeDef
    int[] imageEditActionType;

    private ArrayList<String> localStep0ImagePaths;
    private ArrayList<ArrayList<String>> edittedImagePaths;

    // used in the future for undo things
    private ArrayList<Integer> currentEditStepIndexList;

    private int currentImageIndex;
    private boolean isInEditMode;
    private @ImagePickerBuilder.ImageEditActionTypeDef
    int currentEditActionType;

    private View vgDownloadProgressBar;
    private ImageDownloadPresenter imageDownloadPresenter;

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
    private View vCropProgressBar;
    private View blockingView;

    public static Intent getIntent(Context context, ArrayList<String> imageUrls, int minResolution,
                                   @ImagePickerBuilder.ImageEditActionTypeDef int[] imageEditActionType) {
        Intent intent = new Intent(context, ImageEditorActivity.class);
        intent.putExtra(EXTRA_IMAGE_URLS, imageUrls);
        intent.putExtra(EXTRA_MIN_RESOLUTION, minResolution);
        intent.putExtra(EXTRA_EDIT_ACTION_TYPE, imageEditActionType);
        return intent;
    }

    public static Intent getIntent(Context context, String imageUrl, int minResolution,
                                   @ImagePickerBuilder.ImageEditActionTypeDef int[] imageEditActionType) {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(imageUrl);
        return getIntent(context, imageUrls, minResolution, imageEditActionType);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_image_editor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        //TODO for test only
        extraImageUrls = new ArrayList<>();
        extraImageUrls.add("/storage/emulated/0/WhatsApp/Media/WhatsApp Documents/IMG_20180308_181928_HDR.jpg");
        extraImageUrls.add("/storage/emulated/0/DCIM/Camera/IMG_20180418_113022.jpg");
        extraImageUrls.add("/storage/emulated/0/Download/Guitar-PNG-Image-500x556.png");
        extraImageUrls.add("/storage/emulated/0/Download/303836.jpg");

//        if (intent.hasExtra(EXTRA_IMAGE_URLS)) {
//            extraImageUrls = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS);
//        } else {
//            finish();
//            return;
//        }

        minResolution = intent.getIntExtra(EXTRA_MIN_RESOLUTION, 0);
        imageEditActionType = intent.getIntArrayExtra(EXTRA_EDIT_ACTION_TYPE);

        if (savedInstanceState == null) {
            currentImageIndex = 0;
            localStep0ImagePaths = new ArrayList<>();
            edittedImagePaths = new ArrayList<>();
            isInEditMode = false;
            currentEditActionType = ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE;
        } else {
            currentImageIndex = savedInstanceState.getInt(SAVED_IMAGE_INDEX, 0);
            localStep0ImagePaths = savedInstanceState.getStringArrayList(SAVED_LOCAL_IMAGE_PATH);
            //noinspection unchecked
            edittedImagePaths = (ArrayList<ArrayList<String>>) savedInstanceState.getSerializable(SAVED_EDITTED_PATHS);
            isInEditMode = savedInstanceState.getBoolean(SAVED_IN_EDIT_MODE);
            currentEditActionType = savedInstanceState.getInt(SAVED_EDIT_TYPE);
        }

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
        vCropProgressBar = findViewById(R.id.crop_progressbar);
        blockingView = findViewById(R.id.crop_blocking_view);

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
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP:
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_ROTATE:
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE:
                    fragment.cancelCropRotateImage();
                    break;
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_WATERMARK:
                    //TODO undo watermark here
                    break;
            }

        }
        setupEditMode(false, ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE);
    }

    private void onDoneButtonClicked() {
        //TODO on done button clicked
    }

    private void onSaveEditClicked() {
        if (isInEditMode) {
            showCropLoading();
            ImageEditPreviewFragment fragment = getCurrentFragment();
            switch (currentEditActionType) {
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP:
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_ROTATE:
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE:
                    if (fragment != null) {
                        fragment.saveEdittedImage();
                    }
                    break;
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_WATERMARK:
                    break;
            }
        }
    }

    @Override
    public void onSuccessSaveEditImage(Uri resultUri) {
        hideCropLoading();
        File file = new File(resultUri.getPath());
        if (!file.exists()) {
            return;
        }
        String path = file.getAbsolutePath();
        // it is on the last node on the step
        if (getMaxStepForCurrentImage() != getCurrentStepForCurrentImage() + 1) {
            //discard the next file to size and set currentStepIndex to current+1
            //discard unneeded files
            for (int j = getMaxStepForCurrentImage() - 1; j > getCurrentStepForCurrentImage(); j++) {
                String pathToDelete = edittedImagePaths.get(currentImageIndex).get(j);
                deleteUnusedFile(pathToDelete);
                edittedImagePaths.remove(j);
            }
        }
        // append the path to array
        edittedImagePaths.get(currentImageIndex).add(path);
        currentEditStepIndexList.set(currentImageIndex, getCurrentStepForCurrentImage() + 1);

        refreshViewPager();
        imageEditThumbnailListWidget.notifyDataSetChanged();

        setupEditMode(false, ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE);
    }

    private void refreshViewPager(){
        imageEditorViewPagerAdapter.setEdittedImagePaths(edittedImagePaths);
        imageEditorViewPagerAdapter.setCurrentEditStepIndexList(currentEditStepIndexList);
        viewPager.setAdapter(imageEditorViewPagerAdapter);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(currentImageIndex, false);
            }
        });
    }

    private int getCurrentStepForCurrentImage(){
        return currentEditStepIndexList.get(currentImageIndex);
    }

    private int getMaxStepForCurrentImage(){
        return edittedImagePaths.get(currentImageIndex).size();
    }

    private void deleteUnusedFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onErrorSaveEditImage(Throwable throwable) {
        hideCropLoading();
        NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(getContext(), throwable));
        onCancelEditClicked();
    }

    private ImageEditPreviewFragment getCurrentFragment() {
        return (ImageEditPreviewFragment) imageEditorViewPagerAdapter.getRegisteredFragment(currentImageIndex);
    }

    private void showCropLoading() {
        vCropProgressBar.setVisibility(View.VISIBLE);
        blockingView.setVisibility(View.VISIBLE);
    }

    private void hideCropLoading() {
        vCropProgressBar.setVisibility(View.GONE);
        blockingView.setVisibility(View.GONE);
    }

    private void setupEditActionWidget() {
        imageEditActionMainWidget.setOnImageEditActionMainWidgetListener(this);
        imageEditActionMainWidget.setData(imageEditActionType);
    }

    @Override
    public void onEditActionClicked(@ImagePickerBuilder.ImageEditActionTypeDef int editActionType) {
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
            if (fragment != null) {
                fragment.setEditMode(true);
            }
            //TODO show controls
            switch (editActionType) {
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP:
                    //currently not supported.
                    break;
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_ROTATE:
                    //currently not supported.
                    break;
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_WATERMARK:
                    //currently not supported.
                    break;
                case ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE:
                    break;
            }
        } else {
            editorMainView.setVisibility(View.VISIBLE);
            editorControlView.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);

            if (fragment != null) {
                fragment.setEditMode(false);
            }
        }
    }

    private void setUpThumbnailPreview() {
        imageEditThumbnailListWidget.setOnImageEditThumbnailListWidgetListener(this);
        imageEditThumbnailListWidget.setData(edittedImagePaths, currentEditStepIndexList, currentImageIndex);
        imageEditThumbnailListWidget.setVisibility(localStep0ImagePaths.size() <= 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onThumbnailItemClicked(String imagePath, int position) {
        if (viewPager.getCurrentItem() != position) {
            viewPager.setCurrentItem(position);
        }
    }

    private void finishEditImage() {
        Intent intent = new Intent();
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0, sizei = edittedImagePaths.size(); i < sizei; i++) {
            resultList.add(edittedImagePaths.get(i).get(currentEditStepIndexList.get(i)));
        }
        //TODO delete file not in result.
        intent.putStringArrayListExtra(EDIT_RESULT_PATHS, resultList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onErrorDownloadImageToLocal(Throwable e) {
        hideProgressDialog();
    }

    @Override
    public void onSuccessDownloadImageToLocal(ArrayList<String> localPaths) {
        hideProgressDialog();
        copyToLocalUrl(localPaths);
        startEditLocalImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // download network image url if needed
        if (localStep0ImagePaths == null || localStep0ImagePaths.size() == 0) {
            boolean hasNetworkImage = false;
            for (int i = 0, sizei = extraImageUrls.size(); i < sizei; i++) {
                if (URLUtil.isNetworkUrl(extraImageUrls.get(i))) {
                    hasNetworkImage = true;
                    break;
                }
            }
            if (hasNetworkImage) {
                showProgressDialog();
                hideContentView();
                imageDownloadPresenter = new ImageDownloadPresenter();
                imageDownloadPresenter.attachView(this);
                imageDownloadPresenter.convertHttpPathToLocalPath(extraImageUrls);
            } else {
                copyToLocalUrl(extraImageUrls);
                startEditLocalImages();
            }
        } else {
            startEditLocalImages();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (imageDownloadPresenter != null) {
            imageDownloadPresenter.detachView();
        }
    }

    private void startEditLocalImages() {
        showContentView();
        if (imageEditorViewPagerAdapter == null) {
            imageEditorViewPagerAdapter = new ImageEditorViewPagerAdapter(getSupportFragmentManager(),
                    localStep0ImagePaths,
                    edittedImagePaths,
                    currentEditStepIndexList,
                    minResolution);
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

    private void showProgressDialog() {
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
        localStep0ImagePaths = new ArrayList<>();
        edittedImagePaths = new ArrayList<>();
        currentEditStepIndexList = new ArrayList<>(imageUrls.size());
        for (int i = 0, sizei = imageUrls.size(); i < sizei; i++) {
            localStep0ImagePaths.add(imageUrls.get(i));
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.image_edit_backpressed_title))
                    .setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteNotUsedImage();
                            ImageEditorActivity.super.onBackPressed();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        }
    }

    private void deleteNotUsedImage() {
        //TODO to delete all file in editted paths, and localStep0Urls (if it is different with the original imageURLS)
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_IMAGE_INDEX, currentImageIndex);
        outState.putStringArrayList(SAVED_LOCAL_IMAGE_PATH, localStep0ImagePaths);
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
    public boolean isInEditMode() {
        return isInEditMode;
    }


}
