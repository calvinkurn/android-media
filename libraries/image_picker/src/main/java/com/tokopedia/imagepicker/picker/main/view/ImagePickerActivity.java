package com.tokopedia.imagepicker.picker.main.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.main.adapter.ImagePickerViewPagerAdapter;
import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.picker.instagram.view.fragment.ImagePickerInstagramFragment;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageSelectionTypeDef;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends BaseSimpleActivity
        implements ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener,
        ImagePickerCameraFragment.OnImagePickerCameraFragmentListener,
        ImagePickerInstagramFragment.ListenerImagePickerInstagram, ImagePickerPresenter.ImagePickerView {

    public static final String EXTRA_IMAGE_PICKER_BUILDER = "x_img_pick_builder";

    public static final String PICKER_RESULT_PATHS = "result_paths";

    public static final String SAVED_SELECTED_TAB = "saved_sel_tab";
    public static final String SAVED_SELECTED_IMAGES = "saved_sel_img";
    private static final int REQUEST_CAMERA_PERMISSIONS = 932;
    private static final int REQUEST_CODE_EDITOR = 933;

    private TabLayout tabLayout;
    private ImagePickerBuilder imagePickerBuilder;

    private int selectedTab = 0;
    private ViewPager viewPager;

    private ImagePickerViewPagerAdapter imagePickerViewPagerAdapter;
    private List<String> permissionsToRequest;

    private ProgressDialog progressDialog;

    private ImagePickerPresenter imagePickerPresenter;

    private ArrayList<String> selectedImagePaths;
    private TextView tvDone;

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
            imagePickerBuilder = (ImagePickerBuilder) intent.getExtras().get(EXTRA_IMAGE_PICKER_BUILDER);
        } else {
            imagePickerBuilder = ImagePickerBuilder.getDefaultBuilder(getContext());
        }
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            selectedImagePaths = new ArrayList<>();
        } else {
            selectedTab = savedInstanceState.getInt(SAVED_SELECTED_TAB, 0);
            selectedImagePaths = savedInstanceState.getStringArrayList(SAVED_SELECTED_IMAGES);
        }

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        getSupportActionBar().setTitle(imagePickerBuilder.getTitle());

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
    }

    private void onDoneClicked() {
        if (selectedImagePaths.size() > 0) {
            if (imagePickerBuilder.isContinueToEditAfterPick()) {
                Intent intent = ImageEditorActivity.getIntent(this, selectedImagePaths,
                        imagePickerBuilder.getMinResolution(), imagePickerBuilder.getImageEditActionType(),
                        imagePickerBuilder.getRatioX(), imagePickerBuilder.getRatioY(),
                        imagePickerBuilder.isCirclePreview());
                startActivityForResult(intent, REQUEST_CODE_EDITOR);
            } else {
                onFinishWithMultipleImageValidateNetworkPath(selectedImagePaths);
            }
        }
    }

    private void setupPreview() {
        View vgPreviewContainer = findViewById(R.id.vg_preview_container);
        if (imagePickerBuilder.isHasPickerPreview()) {
            vgPreviewContainer.setVisibility(View.VISIBLE);
        } else {
            vgPreviewContainer.setVisibility(View.GONE);
        }
    }

    private void setupViewPager() {
        imagePickerViewPagerAdapter = new ImagePickerViewPagerAdapter(this, getSupportFragmentManager(), imagePickerBuilder);
        viewPager.setAdapter(imagePickerViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        if (tabLayout.getTabCount() <= 1) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == permissionsToRequest.size()) {
            boolean allIsAllowed = true;
            for (int result : grantResults) {
                if (result == -1) {
                    allIsAllowed = false;
                }
            }
            if (allIsAllowed) {
                refreshViewPager();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int cameraIndex = imagePickerBuilder.indexTypeDef(ImagePickerTabTypeDef.TYPE_CAMERA);
            int galleryIndex = imagePickerBuilder.indexTypeDef(ImagePickerTabTypeDef.TYPE_GALLERY);
            String[] permissions = null;
            if (cameraIndex > -1) {
                permissions = new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
            } else if (galleryIndex > -1) {
                permissions = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }
            if (permissions == null) { // it is not camera or gallery; no permission is needed;
                refreshViewPager();
            } else { // check each permission
                permissionsToRequest = new ArrayList<>();
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToRequest.add(permission);
                    }
                }
                if (!permissionsToRequest.isEmpty()) {
                    ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
                } else {
                    refreshViewPager();
                }
            }
        } else { // under jellybean, no need to check runtime permission
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
        //remove any cache file captured by camera
        ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA);
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
        return selectedImagePaths.size() >= imagePickerBuilder.getMaximumNoPick();
    }

    @Override
    public void onAlbumItemClicked(MediaItem item, boolean isChecked) {
        onImageSelected(item.getRealPath(), isChecked);
    }

    @Override
    public void onClickImageInstagram(String url, boolean isChecked) {
        onImageSelected(url, isChecked);
    }

    @Override
    public void onImageTaken(String filePath) {
        onImageSelected(filePath, true);
    }

    private void onImageSelected(String filePathOrUrl, boolean isChecked) {
        switch (imagePickerBuilder.getImageSelectionType()) {
            case ImageSelectionTypeDef.TYPE_SINGLE: {
                onSingleImagePicked(filePathOrUrl);
            }
            break;
            case ImageSelectionTypeDef.TYPE_MULTIPLE: {
                if (isChecked) {
                    selectedImagePaths.add(filePathOrUrl);
                    enableDoneView();
                } else {
                    selectedImagePaths.remove(filePathOrUrl);
                    if (selectedImagePaths.size() == 0) {
                        disableDoneView();
                    }
                }
                if (imagePickerBuilder.isHasPickerPreview()) {
                    // TODO update the preview?
                }
            }
            break;
        }
    }

    private void disableDoneView() {
        tvDone.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
        tvDone.setEnabled(false);
    }

    private void enableDoneView() {
        if (!tvDone.isEnabled()) {
            tvDone.setTextColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
            tvDone.setEnabled(true);
        }
    }

    private void onSingleImagePicked(String imageUrlOrPath) {
        if (imagePickerBuilder.isContinueToEditAfterPick()) {
            Intent intent = ImageEditorActivity.getIntent(this, imageUrlOrPath,
                    imagePickerBuilder.getMinResolution(), imagePickerBuilder.getImageEditActionType(),
                    imagePickerBuilder.getRatioX(), imagePickerBuilder.getRatioY(),
                    imagePickerBuilder.isCirclePreview());
            startActivityForResult(intent, REQUEST_CODE_EDITOR);
        } else {
            onFinishWithSingleImage(imageUrlOrPath);
        }
    }

    private void onFinishWithSingleImage(String imageUrlOrPath) {
        ArrayList<String> finalPathList = new ArrayList<>();
        finalPathList.add(imageUrlOrPath);
        onFinishWithMultipleImageValidateNetworkPath(finalPathList);
    }

    private void onFinishWithMultipleImageValidateNetworkPath(ArrayList<String> imageUrlOrPathList) {
        if (imagePickerBuilder.isMoveImageResultToLocal()) {
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
        imagePickerPresenter.resizeImage(imagePathList, maxFileSizeInKB);
    }

    private void onFinishWithMultipleFinalImage(ArrayList<String> imageUrlOrPathList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PICKER_RESULT_PATHS, imageUrlOrPathList);
        setResult(Activity.RESULT_OK, intent);
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
        if (e instanceof ImagePickerPresenter.FileSizeAboveMaximumException) {
            NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.max_file_size_reached));
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(this, ErrorHandler.getErrorMessage(getContext(), e));
        }
    }

    @Override
    public void onSuccessResizeImage(ArrayList<String> resultPaths) {
        hideDownloadProgressDialog();
        onFinishWithMultipleFinalImage(resultPaths);
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
            progressDialog.setMessage(getString(R.string.title_loading));
        }
        progressDialog.show();
    }

    private void hideDownloadProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDITOR:
                if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(ImageEditorActivity.EDIT_RESULT_PATHS)) {
                    ArrayList<String> finalPathList = data.getStringArrayListExtra(ImageEditorActivity.EDIT_RESULT_PATHS);
                    onFinishWithMultipleImageValidateNetworkPath(finalPathList);
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
    }

}
