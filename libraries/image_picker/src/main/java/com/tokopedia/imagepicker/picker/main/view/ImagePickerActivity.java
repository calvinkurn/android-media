package com.tokopedia.imagepicker.picker.main.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.Menus;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.GalleryType;
import com.tokopedia.imagepicker.common.ImageEditorBuilder;
import com.tokopedia.imagepicker.common.ImagePickerBuilder;
import com.tokopedia.imagepicker.common.ImagePickerGlobalSettings;
import com.tokopedia.imagepicker.common.exception.FileSizeAboveMaximumException;
import com.tokopedia.imagepicker.common.model.MediaItem;
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.main.adapter.ImagePickerViewPagerAdapter;
import com.tokopedia.imagepicker.picker.main.builder.StateRecorderType;
import com.tokopedia.imagepicker.picker.video.VideoRecorderFragment;
import com.tokopedia.imagepicker.picker.widget.ImagePickerPreviewWidget;
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner;
import com.tokopedia.utils.image.ImageProcessingUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tokopedia.imagepicker.common.BuilderConstantKt.EXTRA_IMAGE_PICKER_BUILDER;
import static com.tokopedia.imagepicker.common.ResultConstantKt.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.common.ResultConstantKt.RESULT_IMAGES_FED_INTO_IMAGE_PICKER;
import static com.tokopedia.imagepicker.common.ResultConstantKt.RESULT_IS_EDITTED;
import static com.tokopedia.imagepicker.common.ResultConstantKt.RESULT_PREVIOUS_IMAGE;

public final class ImagePickerActivity extends BaseSimpleActivity
        implements ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener,
        ImagePickerCameraFragment.OnImagePickerCameraFragmentListener,
        ImagePickerPresenter.ImagePickerView,
        ImagePickerPreviewWidget.OnImagePickerThumbnailListWidgetListener, VideoRecorderFragment.VideoPickerCallback {

    public static final String SAVED_SELECTED_TAB = "saved_sel_tab";
    public static final String SAVED_SELECTED_IMAGES = "saved_sel_img";
    public static final String SAVED_IMAGE_DESCRIPTION = "saved_img_desc";

    protected static final int REQUEST_CAMERA_PERMISSIONS = 932;
    protected static final int REQUEST_CODE_EDITOR = 933;

    private TabLayout tabLayout;
    protected ImagePickerBuilder imagePickerBuilder;

    private int selectedTab = 0;
    private TouchViewPager viewPager;

    private ImagePickerViewPagerAdapter imagePickerViewPagerAdapter;
    private List<String> permissionsToRequest;

    private ProgressDialog progressDialog;

    private ImagePickerPresenter imagePickerPresenter;

    private ArrayList<String> selectedImagePaths;
    protected ArrayList<String> imageDescriptionList;
    private TextView tvDone;
    private boolean isPermissionGotDenied;
    private ImagePickerPreviewWidget imagePickerPreviewWidget;
    private boolean isFinishEditting;
    private String imageTooLargeErrorMessage = "";

    public static Intent getIntent(Context context, ImagePickerBuilder imagePickerBuilder) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_image_picker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_IMAGE_PICKER_BUILDER)) {
            imagePickerBuilder = intent.getParcelableExtra(EXTRA_IMAGE_PICKER_BUILDER);
        } else {
            imagePickerBuilder = ImagePickerBuilder.getSquareImageBuilder(getContext());
        }

        if (savedInstanceState == null) {
            if (imagePickerBuilder.supportMultipleSelection()) {
                selectedImagePaths = imagePickerBuilder.getInitialSelectedImagePathList();
                imageDescriptionList = new ArrayList<>();
                //create empty description for initial images
                if (selectedImagePaths != null && selectedImagePaths.size() > 0) {
                    for (String path : selectedImagePaths) {
                        imageDescriptionList.add(null);
                    }
                }
            } else {
                selectedImagePaths = new ArrayList<>();
                imageDescriptionList = new ArrayList<>();
            }
        } else {
            selectedTab = savedInstanceState.getInt(SAVED_SELECTED_TAB, 0);
            selectedImagePaths = savedInstanceState.getStringArrayList(SAVED_SELECTED_IMAGES);
            imageDescriptionList = savedInstanceState.getStringArrayList(SAVED_IMAGE_DESCRIPTION);
        }

        if (imagePickerBuilder.getImageTooLargeErrorMessage() == null || imagePickerBuilder.getImageTooLargeErrorMessage().isEmpty()) {
            imageTooLargeErrorMessage = getString(R.string.max_file_size_reached);
        } else {
            imageTooLargeErrorMessage = imagePickerBuilder.getImageTooLargeErrorMessage();
        }

        super.onCreate(savedInstanceState);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(imagePickerBuilder.getTitle());
        }

        setupPreview();
        setupViewPager();
        setupTabLayout();

        tvDone = findViewById(R.id.tv_done);
        if (imagePickerBuilder.supportMultipleSelection()) {
            tvDone.setVisibility(View.VISIBLE);
            tvDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDoneClicked();
                }
            });
            handleDoneVisibility();
        } else {
            tvDone.setVisibility(View.GONE);
        }
        trackOpen();
        InternalStorageCleaner.cleanUpInternalStorageIfNeeded(this, ImageProcessingUtil.DEFAULT_DIRECTORY);
    }

    protected void onDoneClicked() {
        if (selectedImagePaths.size() > 0) {
            if (imagePickerBuilder.isContinueToEditAfterPick()) {
                startEditorActivity(selectedImagePaths);
            } else {
                onFinishWithMultipleImageValidateNetworkPath(selectedImagePaths);
            }
        }
    }

    private void setupPreview() {
        imagePickerPreviewWidget = findViewById(R.id.image_picker_preview_widget);
        if (imagePickerBuilder.supportMultipleSelection()) {
            imagePickerPreviewWidget.setData(selectedImagePaths,
                    imagePickerBuilder.getImagePickerMultipleSelectionBuilder().getUsePrimaryImageString(),
                    imagePickerBuilder.getImagePickerMultipleSelectionBuilder().getPlaceholderImagePathResList());
            imagePickerPreviewWidget.setVisibility(View.VISIBLE);
            imagePickerPreviewWidget.setOnImagePickerThumbnailListWidgetListener(this);
            imagePickerPreviewWidget.setMaxAdapterSize(imagePickerBuilder.getMaximumNoPick());
            imagePickerPreviewWidget.setCanReorder(
                    imagePickerBuilder.getImagePickerMultipleSelectionBuilder().getCanReorder());
        } else {
            imagePickerPreviewWidget.setVisibility(View.GONE);
        }
    }

    private void setupViewPager() {
        imagePickerViewPagerAdapter = getImagePickerViewPagerAdapter();
        viewPager.setAdapter(imagePickerViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imagePickerViewPagerOnPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    @NonNull
    protected ImagePickerViewPagerAdapter getImagePickerViewPagerAdapter() {
        return new ImagePickerViewPagerAdapter(this, getSupportFragmentManager(), imagePickerBuilder);
    }

    protected void imagePickerViewPagerOnPageSelected(int position) {
        if (selectedTab != position) {
            Fragment previousFragment = imagePickerViewPagerAdapter.getRegisteredFragment(selectedTab);
            if (previousFragment instanceof ImagePickerCameraFragment) {
                ((ImagePickerCameraFragment) previousFragment).onInvisible();
            }
            Fragment fragment = imagePickerViewPagerAdapter.getRegisteredFragment(position);
            if (fragment instanceof ImagePickerCameraFragment) {
                ((ImagePickerCameraFragment) fragment).onVisible();
            }
        }
        selectedTab = position;
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        if (tabLayout.getTabCount() <= 1) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // there is no way to change style to BOLD in XML and programmatically. we use this trick.
                try {
                    TextView textView = ((TextView) ((ViewGroup) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                } catch (Exception e) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                try {
                    TextView textView = ((TextView) ((ViewGroup) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                    textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.NORMAL));
                } catch (Exception e) {

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsToRequest != null && grantResults.length == permissionsToRequest.size()) {
            int grantCount = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionGotDenied = true;
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsToRequest.get(grantCount))) {
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        Toast.makeText(getContext(), getString(R.string.permission_enabled_needed), Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                grantCount++;
            }
            if (grantCount == grantResults.length) {
                isPermissionGotDenied = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFinishEditting) {
            return;
        }
        if (isPermissionGotDenied) {
            finish();
            return;
        }
        int cameraIndex = imagePickerBuilder.getCameraIndex();
        Set<String> permissions = new HashSet<>();
        if (cameraIndex > -1) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (imagePickerBuilder.getGalleryIndex() > -1) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (imagePickerBuilder.getRecorderIndex() > -1) {
            permissions.add(Manifest.permission.CAMERA);
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
        } else {
            refreshViewPager();
        }
    }

    private void refreshViewPager() {
        imagePickerViewPagerAdapter.destroyAllIndex();
        imagePickerViewPagerAdapter.notifyDataSetChanged();
        setupTabLayout();
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(selectedTab, false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        trackBack();
        super.onBackPressed();
    }

    private void handleDoneVisibility() {
        if (selectedImagePaths.size() == 0) {
            disableDoneView();
        } else {
            enableDoneView();
        }
    }

    @Override
    public boolean isMaxImageReached() {
        boolean isMaxImageReached = selectedImagePaths.size() >= imagePickerBuilder.getMaximumNoPick();
        if (isMaxImageReached) {
            NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.max_no_of_image_reached));
        }
        return isMaxImageReached;
    }

    @Override
    public boolean isFinishEditting() {
        return isFinishEditting;
    }

    @Override
    public boolean needShowCameraPreview() {
        return imagePickerBuilder.supportMultipleSelection() || !imagePickerBuilder.isContinueToEditAfterPick();
    }

    @Override
    public void onPreviewCameraViewVisible() {
        viewPager.setAllowPageSwitching(false);
        tabLayout.setVisibility(View.GONE);
        imagePickerPreviewWidget.setVisibility(View.GONE);
        disableDoneView();
    }

    @Override
    public void onCameraViewVisible() {
        viewPager.setAllowPageSwitching(true);
        if (tabLayout.getTabCount() > 1) {
            tabLayout.setVisibility(View.VISIBLE);
        }
        if (imagePickerBuilder.supportMultipleSelection()) {
            imagePickerPreviewWidget.setVisibility(View.VISIBLE);
        }
        if (selectedImagePaths.size() > 0) {
            enableDoneView();
        }
    }


    @Override
    public ArrayList<String> getImagePath() {
        return selectedImagePaths;
    }

    @Override
    public long getMaxFileSize() {
        return imagePickerBuilder.getMaxFileSizeInKB();
    }

    @Override
    public void onAlbumItemClicked(MediaItem item, boolean isChecked) {
        onImageSelected(item.getPath(), isChecked, null);
    }

    @Override
    public int getRatioY() {
        return imagePickerBuilder.getRatioY();
    }

    @Override
    public int getRatioX() {
        return imagePickerBuilder.getRatioX();
    }

    @Override
    public void onImageTaken(String filePath) {
        onImageSelected(filePath, true, null);
    }

    protected void onImageSelected(String filePathOrUrl, boolean isChecked, String description) {
        if (imagePickerBuilder.supportMultipleSelection()) {
            if (isChecked) {
                imagePickerPreviewWidget.addData(filePathOrUrl);
                imageDescriptionList.add(description);
                enableDoneView();
            } else {
                imagePickerPreviewWidget.removeData(filePathOrUrl);
                if (selectedImagePaths.size() == 0) {
                    disableDoneView();
                }
                //will trigger afterThumbnailRemoved()
            }
        } else {
            imageDescriptionList = new ArrayList<>();
            imageDescriptionList.add(description);
            onSingleImagePicked(filePathOrUrl);
        }
    }

    @Override
    public void onThumbnailItemClicked(String imagePath, int position) {
        //no-op when thumbnail is clicked currently
    }

    @Override
    public void onThumbnailItemLongClicked(String imagePath, int position) {
        if (position == 0) {
            return;
        }
        Menus menus = new Menus(this);
        menus.setItemMenuList(new String[]{getString(R.string.change_to_primary)});
        menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
            @Override
            public void onClick(Menus.ItemMenus itemMenus, int pos) {
                int toPosition = 0;
                //move the image path to 0
                imagePickerPreviewWidget.reorderPosition(position, toPosition);

                String imageDescription = imageDescriptionList.remove(position);
                imageDescriptionList.add(toPosition, imageDescription);
                menus.dismiss();
            }
        });
        menus.show();
    }

    @Override
    public void afterThumbnailRemoved(int index) {
        if (selectedImagePaths.size() == 0) {
            disableDoneView();
        }
        imageDescriptionList.remove(index);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isAdded() && fragment instanceof ImagePickerInterface) {
                ((ImagePickerInterface) fragment).afterThumbnailImageRemoved();
            }
        }
    }

    private void disableDoneView() {
        tvDone.setTextColor(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_32));
        tvDone.setEnabled(false);
    }

    private void enableDoneView() {
        if (!tvDone.isEnabled()) {
            tvDone.setTextColor(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
            tvDone.setEnabled(true);
        }
    }

    private void onSingleImagePicked(String imageUrlOrPath) {
        if (imagePickerBuilder.isContinueToEditAfterPick()) {
            startEditorActivity(imageUrlOrPath);
        } else {
            onFinishWithSingleImage(imageUrlOrPath);
        }
    }

    private void startEditorActivity(String imageUrlOrPath) {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(imageUrlOrPath);
        startEditorActivity(imageUrls);
    }

    protected void startEditorActivity(ArrayList<String> selectedImagePaths) {
        Intent intent = getEditorIntent(selectedImagePaths);
        startActivityForResult(intent, REQUEST_CODE_EDITOR);
    }

    protected Intent getEditorIntent(ArrayList<String> selectedImagePaths) {
        return ImageEditorActivity.getIntent(this,
                new ImageEditorBuilder(
                        selectedImagePaths,
                        imageDescriptionList,
                        imagePickerBuilder.getMinResolution(),
                        imagePickerBuilder.getImageEditActionType(),
                        imagePickerBuilder.getImageRatioType(),
                        imagePickerBuilder.isCirclePreview(),
                        imagePickerBuilder.getMaxFileSizeInKB(),
                        imagePickerBuilder.getRatioOptionList(),
                        imagePickerBuilder.getBelowMinResolutionErrorMessage(),
                        imagePickerBuilder.getImageTooLargeErrorMessage(),
                        imagePickerBuilder.isRecheckSizeAfterResize(),
                        imagePickerBuilder.getConvertToWebp()
                ));
    }

    private void onFinishWithSingleImage(String imageUrlOrPath) {
        ArrayList<String> finalPathList = new ArrayList<>();
        finalPathList.add(imageUrlOrPath);
        onFinishWithMultipleImageValidateNetworkPath(finalPathList);
    }

    private void onFinishWithMultipleImageValidateNetworkPath(ArrayList<String> imageUrlOrPathList) {
        if (imagePickerBuilder.getMoveImageResultToLocal()) {
            //check if there is http url on the list, if any, convert to local.
            showFinishProgressDialog();
            initImagePickerPresenter();
            imagePickerPresenter.convertHttpPathToLocalPath(imageUrlOrPathList);
        } else {
            onFinishWithMultipleImageValidateFileSize(imageUrlOrPathList);
        }
    }

    private void onFinishWithMultipleImageValidateFileSize(ArrayList<String> imagePathList) {
        long maxFileSizeInKB = imagePickerBuilder.getMaxFileSizeInKB();
        showFinishProgressDialog();
        initImagePickerPresenter();
        if (imagePickerBuilder.getGalleryType() == GalleryType.IMAGE_ONLY) {
            imagePickerPresenter.resizeImage(imagePathList, maxFileSizeInKB,
                    imagePickerBuilder.isRecheckSizeAfterResize(), imagePickerBuilder.getConvertToWebp());
        } else {
            onSuccessResizeImage(imagePathList);
        }
    }

    protected void onFinishWithMultipleFinalImage(ArrayList<String> imageUrlOrPathList,
                                                  ArrayList<String> originalImageList,
                                                  ArrayList<String> imageDescriptionList,
                                                  ArrayList<Boolean> isEdittedList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PICKER_RESULT_PATHS, imageUrlOrPathList);
        intent.putStringArrayListExtra(RESULT_PREVIOUS_IMAGE, originalImageList);
        intent.putStringArrayListExtra(RESULT_IMAGES_FED_INTO_IMAGE_PICKER, selectedImagePaths);
        intent.putExtra(RESULT_IS_EDITTED, isEdittedList);
        setResult(Activity.RESULT_OK, intent);

        trackContinue();
        ImagePickerGlobalSettings.clearAllGlobalSettings();
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onErrorDownloadImageToLocal(Throwable e) {
        hideDownloadProgressDialog();
        NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void onSuccessDownloadImageToLocal(ArrayList<String> localPaths) {
        hideDownloadProgressDialog();
        onFinishWithMultipleImageValidateFileSize(localPaths);
    }

    @Override
    public void onErrorResizeImage(Throwable e) {
        hideDownloadProgressDialog();
        if (e instanceof FileSizeAboveMaximumException) {
            NetworkErrorHelper.showRedCloseSnackbar(this, imageTooLargeErrorMessage);
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(getContext(), e));
        }
    }

    @Override
    public void onSuccessResizeImage(ArrayList<String> resultPaths) {
        initImagePickerPresenter();
        imagePickerPresenter.convertFormatImage(resultPaths, imagePickerBuilder.getConvertToWebp());
    }

    @Override
    public void onErrorConvertFormatImage(Throwable e) {
        hideDownloadProgressDialog();
        NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void onSuccessConvertFormatImage(ArrayList<String> resultPaths) {
        hideDownloadProgressDialog();
        onFinishWithMultipleFinalImage(resultPaths, selectedImagePaths, imageDescriptionList,
                new ArrayList<>(selectedImagePaths.size()));
    }

    private void initImagePickerPresenter() {
        if (imagePickerPresenter == null) {
            imagePickerPresenter = new ImagePickerPresenter();
            imagePickerPresenter.attachView(this);
        }
    }

    private void showFinishProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading));
        }
        progressDialog.show();
    }

    private void hideDownloadProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDITOR:
                if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(PICKER_RESULT_PATHS)) {
                    ArrayList<String> finalPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    ArrayList<String> originalImageList = data.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE);
                    ArrayList<Boolean> isEdittedList = (ArrayList<Boolean>) data.getSerializableExtra(RESULT_IS_EDITTED);
                    onFinishWithMultipleFinalImage(finalPathList, originalImageList, imageDescriptionList, isEdittedList);
                    isFinishEditting = true;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SELECTED_TAB, tabLayout.getSelectedTabPosition());
        outState.putStringArrayList(SAVED_SELECTED_IMAGES, selectedImagePaths);
        outState.putStringArrayList(SAVED_IMAGE_DESCRIPTION, imageDescriptionList);
    }

    @Override
    public void onVideoTaken(String filePath) {
        onImageSelected(filePath, true, null);
    }

    @Override
    public void onVideoRecorder(int state) {
        tabLayout.setClickable(state != StateRecorderType.START);
    }

    @Override
    public void onVideoPreviewVisible() {
        onPreviewCameraViewVisible();
    }

    @Override
    public void onVideoRecorderVisible() {
        onCameraViewVisible();
    }

    public void trackOpen() {
        if (ImagePickerGlobalSettings.onImagePickerOpen != null) {
            ImagePickerGlobalSettings.onImagePickerOpen.invoke();
        }
    }

    public void trackBack() {
        if (ImagePickerGlobalSettings.onImagePickerBack != null) {
            ImagePickerGlobalSettings.onImagePickerBack.invoke();
        }
    }

    public void trackContinue() {
        if (ImagePickerGlobalSettings.onImagePickerContinue != null) {
            ImagePickerGlobalSettings.onImagePickerContinue.invoke();
        }
    }

}
