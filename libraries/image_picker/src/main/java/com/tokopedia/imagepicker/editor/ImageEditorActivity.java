package com.tokopedia.imagepicker.editor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.widget.NonSwipeableViewPager;
import com.tokopedia.imagepicker.editor.adapter.ImageEditorEditActionAdapter;
import com.tokopedia.imagepicker.editor.adapter.ImageEditorViewPagerAdapter;
import com.tokopedia.imagepicker.editor.presenter.ImageDownloadPresenter;
import com.tokopedia.imagepicker.editor.widget.ImageEditActionMainWidget;
import com.tokopedia.imagepicker.picker.ImagePickerBuilder;

import java.util.ArrayList;

/**
 * Created by Hendry on 9/25/2017.
 */

public class ImageEditorActivity extends BaseSimpleActivity implements ImageDownloadPresenter.ImageDownloadView {

    public static final String EXTRA_IMAGE_URLS = "IMG_URLS";
    public static final String EXTRA_MIN_RESOLUTION = "MIN_IMG_RESOLUTION";
    public static final String EXTRA_EDIT_ACTION_TYPE = "EDIT_ACTION_TYPE";

    public static final String SAVED_IMAGE_INDEX = "IMG_IDX";
    public static final String SAVED_FINAL_PATHS = "SAVED_CROPPED_PATHS";
    public static final String SAVED_LOCAL_IMAGE_PATH = "RES_PATH";

    public static final String EDIT_RESULT_PATHS = "result_paths";


    private ArrayList<String> extraImageUrls;
    private int minResolution;
    private @ImagePickerBuilder.ImageEditActionTypeDef
    int[] imageEditActionType;

    private ArrayList<String> localImagePaths;
    private ArrayList<String> finalImagePaths;

    private int currentImageIndex;

    private View vgProgressBar;
    private ImageDownloadPresenter imageDownloadPresenter;

    private View vgContentContainer;
    private NonSwipeableViewPager viewPager;

    private ImageEditorViewPagerAdapter imageEditorViewPagerAdapter;

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
//        extraImageUrls = new ArrayList<>();
//        extraImageUrls.add("/storage/emulated/0/Tokopedia/738855.jpg");
//        extraImageUrls.add("/storage/emulated/0/Download/Guitar-PNG-Image-500x556.png");
//        extraImageUrls.add("/storage/emulated/0/WhatsApp/Media/WhatsApp Documents/IMG_20180308_181928_HDR.jpg");
//        extraImageUrls.add("/storage/emulated/0/WhatsApp/Media/WhatsApp Images/IMG-20180111-WA0004.jpg");
//        extraImageUrls.add("/storage/emulated/0/Download/303836.jpg");

        if (intent.hasExtra(EXTRA_IMAGE_URLS)) {
            extraImageUrls = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS);
        } else {
            finish();
            return;
        }

        minResolution = intent.getIntExtra(EXTRA_MIN_RESOLUTION, 0);
        imageEditActionType = intent.getIntArrayExtra(EXTRA_EDIT_ACTION_TYPE);

        vgProgressBar = findViewById(R.id.vg_download_progress_bar);
        vgContentContainer = findViewById(R.id.vg_content_container);
        hideProgressDialog();
        hideContentView();

        viewPager = findViewById(R.id.view_pager);

        if (savedInstanceState == null) {
            currentImageIndex = 0;
            localImagePaths = new ArrayList<>();
            finalImagePaths = new ArrayList<>();
        } else {
            currentImageIndex = savedInstanceState.getInt(SAVED_IMAGE_INDEX, 0);
            localImagePaths = savedInstanceState.getStringArrayList(SAVED_LOCAL_IMAGE_PATH);
            finalImagePaths = savedInstanceState.getStringArrayList(SAVED_FINAL_PATHS);
        }

        setupEditMainLayout();
    }

    private void setupEditMainLayout() {
        ImageEditActionMainWidget imageEditActionMainWidget = findViewById(R.id.image_edit_action_main_widget);
        imageEditActionMainWidget.setData(imageEditActionType);
    }

    private void finishEditImage() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EDIT_RESULT_PATHS, finalImagePaths);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != -1) {
            viewPager.setAdapter(imageEditorViewPagerAdapter);
        }
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
        if (localImagePaths == null || localImagePaths.size() == 0) {
            boolean hasNetworkImage = false;
            for (int i = 0, sizei = extraImageUrls.size(); i < sizei; i++) {
                if (URLUtil.isNetworkUrl(extraImageUrls.get(i))) {
                    hasNetworkImage = true;
                    break;
                }
            }
            if (hasNetworkImage) {
                showProgressDialog();
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
                    localImagePaths,
                    finalImagePaths,
                    minResolution);
        }
        viewPager.setAdapter(imageEditorViewPagerAdapter);
        // viewPager.setCanSwipe(true);

    }

    private void showProgressDialog() {
        vgProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressDialog() {
        vgProgressBar.setVisibility(View.GONE);
    }

    private void hideContentView() {
        vgContentContainer.setVisibility(View.GONE);
    }

    private void showContentView() {
        vgContentContainer.setVisibility(View.VISIBLE);
    }


    private void copyToLocalUrl(ArrayList<String> imageUrls) {
        localImagePaths = new ArrayList<>();
        finalImagePaths = new ArrayList<>();
        for (int i = 0, sizei = imageUrls.size(); i < sizei; i++) {
            localImagePaths.add(imageUrls.get(i));
            finalImagePaths.add(imageUrls.get(i));
        }
    }

//    public void onSuccessCrop(String path){
//        // save the new path
//        if (localImagePaths == null) {
//            return;
//        }
//        if (currentImageIndex >= localImagePaths.size()) {
//            currentImageIndex = localImagePaths.size() - 1;
//        }
//        localImagePaths.set(currentImageIndex, path);
//        addCroppedPath(path);
//        currentImageIndex++;
//        if (currentImageIndex == extraImageUrls.size()) {
//            finishEditing(true);
//        } else {
//            // continue to next image index
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            replaceEditorFragment(fragmentManager);
//            setUpToolbarTitle();
//        }
//    }

//    public void addCroppedPath(String path){
//        finalImagePaths.add(path);
//    }

//    private void finishEditing(boolean isResultOK) {
//        Intent intent = new Intent();
//        if (isResultOK) {
//            setResult(Activity.RESULT_OK, intent);
//            intent.putExtra(SAVED_LOCAL_IMAGE_PATH, localImagePaths);
//            if (getIntent().getBooleanExtra(EXTRA_DELETE_CACHE_WHEN_EXIT, true)) {
//                deleteAllTkpdFilesNotInResult(finalImagePaths, localImagePaths);
//            }
//        } else {
//            setResult(Activity.RESULT_CANCELED, intent);
//            intent.putExtra(SAVED_LOCAL_IMAGE_PATH, getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS));
//            if (getIntent().getBooleanExtra(EXTRA_DELETE_CACHE_WHEN_EXIT, true)) {
//                deleteAllTkpdFilesNotInResult(finalImagePaths, getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS));
//            }
//        }
//        finish();
//    }

//    private void deleteAllTkpdFilesNotInResult(ArrayList<String> savedCroppedPaths, ArrayList<String> resultImageUrls){
//        ArrayList<String> toBeDeletedFiles = new ArrayList<>();
//        for (int i=0, sizei = savedCroppedPaths.size(); i<sizei; i++) {
//            String savedCroppedPath = savedCroppedPaths.get(i);
//            boolean croppedFilesIsInResult = false;
//            for (int j = 0, sizej = resultImageUrls.size(); j<sizej; j++) {
//                if (savedCroppedPath.equals(resultImageUrls.get(j))) {
//                    croppedFilesIsInResult = true;
//                    break;
//                }
//            }
//            if (!croppedFilesIsInResult) {
//                toBeDeletedFiles.add(savedCroppedPath);
//            }
//        }
//        FileUtils.deleteAllCacheTkpdFiles(toBeDeletedFiles);
//    }

//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
//            currentImageIndex--;
//            setUpToolbarTitle();
//            getSupportFragmentManager().popBackStack();
//        } else {
//            finishEditing(false);
//        }
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_IMAGE_INDEX, currentImageIndex);
        outState.putStringArrayList(SAVED_LOCAL_IMAGE_PATH, localImagePaths);
        outState.putStringArrayList(SAVED_FINAL_PATHS, finalImagePaths);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }


}
