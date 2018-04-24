package com.tokopedia.imagepicker.picker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.editor.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.adapter.AlbumAdapter;
import com.tokopedia.imagepicker.picker.adapter.ImagePickerViewPagerAdapter;
import com.tokopedia.imagepicker.picker.adapter.TabLayoutImagePickerAdapter;
import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.picker.widget.AlbumsSpinner;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends BaseSimpleActivity
        implements AdapterView.OnItemSelectedListener,
        ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener,
        ImagePickerCameraFragment.OnImagePickerCameraFragmentListener {

    public static final String EXTRA_IMAGE_PICKER_BUILDER = "x_img_pick_builder";

    public static final String SAVED_SELECTED_TAB = "saved_sel_tab";
    private static final int REQUEST_CAMERA_PERMISSIONS = 932;
    private static final int REQUEST_CODE_EDITOR = 933;

    private TabLayout tabLayout;
    private ImagePickerBuilder imagePickerBuilder;

    private int selectedTab = 0;
    private int selectedAlbumPos = 0;
    private ViewPager viewPager;

    private AlbumsSpinner albumSpinner;
    private AlbumAdapter albumAdapter;
    private TextView tvSelectedAlbum;
    private ImagePickerViewPagerAdapter imagePickerViewPagerAdapter;
    private List<String> permissionsToRequest;

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
            imagePickerBuilder = ImagePickerBuilder.ADD_PRODUCT;
        }
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedTab = savedInstanceState.getInt(SAVED_SELECTED_TAB, 0);
        }

        setupPreview();
        setupViewPager();
        setupTabLayout();
        setupAlbumSpinner();
    }

    private void setupPreview() {
        View vgPreviewContainer = findViewById(R.id.vg_preview_container);
        if (imagePickerBuilder.supportMultipleSelection() &&
                imagePickerBuilder.hasThumbnailPreview()) {
            vgPreviewContainer.setVisibility(View.VISIBLE);
        } else {
            vgPreviewContainer.setVisibility(View.GONE);
        }
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.view_pager);
        imagePickerViewPagerAdapter = new ImagePickerViewPagerAdapter(getSupportFragmentManager(), imagePickerBuilder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int cameraIndex = imagePickerBuilder.indexTypeDef(ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA);
            int galleryIndex = imagePickerBuilder.indexTypeDef(ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY);
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
                viewPager.setAdapter(imagePickerViewPagerAdapter);
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
                    viewPager.setAdapter(imagePickerViewPagerAdapter);
                }
            }
        } else { // under jellybean, no need to check runtime permission
            viewPager.setAdapter(imagePickerViewPagerAdapter);
        }
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tab_layout);
        final TabLayoutImagePickerAdapter tabLayoutImagePickerAdapter =
                new TabLayoutImagePickerAdapter(tabLayout, this, imagePickerBuilder.getTabTypeDef());
        tabLayoutImagePickerAdapter.notifyDataSetChanged();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (viewPager.getCurrentItem() != position) {
                    viewPager.setCurrentItem(position);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayoutImagePickerAdapter.unselectTab(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedTab = position;
                tabLayoutImagePickerAdapter.selectTab(position);
                changeSpinnerVisibilityByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayoutImagePickerAdapter.selectTab(selectedTab);
        if (tabLayout.getTabCount() <= 1) {
            tabLayout.setVisibility(View.GONE);
        }
    }

    private void changeSpinnerVisibilityByPosition(int position) {
        switch (imagePickerBuilder.getTabTypeDef(position)) {
            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY: {
                getSupportActionBar().setTitle("");
                if (TextUtils.isEmpty(tvSelectedAlbum.getText())) {
                    tvSelectedAlbum.setVisibility(View.GONE);
                } else {
                    tvSelectedAlbum.setVisibility(View.VISIBLE);
                }
            }
            break;
            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA: {
                getSupportActionBar().setTitle(getString(R.string.take_picture));
                tvSelectedAlbum.setVisibility(View.GONE);
            }
            break;
            default: {
                getSupportActionBar().setTitle(getTitle());
                tvSelectedAlbum.setVisibility(View.GONE);
            }
            break;
        }
    }

    private void setupAlbumSpinner() {
        tvSelectedAlbum = findViewById(R.id.selected_album);
        if (imagePickerBuilder.indexTypeDef(ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY) > -1) {
            albumAdapter = new AlbumAdapter(this, null, false);
            albumSpinner = new AlbumsSpinner(this);
            albumSpinner.setOnItemSelectedListener(this);
            albumSpinner.setSelectedTextView(tvSelectedAlbum);
            albumSpinner.setPopupAnchorView(toolbar);
            albumSpinner.setAdapter(albumAdapter);
        }
        changeSpinnerVisibilityByPosition(selectedTab);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (selectedAlbumPos != position) {
            selectedAlbumPos = position;
            onAlbumLoaded(albumAdapter.getCursor());
        }
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
                viewPager.setAdapter(imagePickerViewPagerAdapter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAlbumLoaded(final Cursor cursor) {
        albumAdapter.swapCursor(cursor);
        if (cursor != null && cursor.getCount() > selectedAlbumPos) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    cursor.moveToPosition(selectedAlbumPos);
                    albumSpinner.setSelection(ImagePickerActivity.this, selectedAlbumPos);
                    AlbumItem albumItem = AlbumItem.valueOf(cursor);
                    if (albumItem.isAll()) {
                        albumItem.addCaptureCount();
                    }
                    ImagePickerGalleryFragment imagePickerGalleryFragment = getGalleryFragment();
                    if (imagePickerGalleryFragment != null) {
                        imagePickerGalleryFragment.selectAlbum(albumItem);
                    }
                    changeSpinnerVisibilityByPosition(selectedTab);
                }
            });
        }
    }

    @Override
    public void onAlbumItemClicked(MediaItem item, boolean isChecked) {
        if (imagePickerBuilder.supportMultipleSelection()) {
            // TODO will do later, currently only support single selection
            if (imagePickerBuilder.hasThumbnailPreview()) {
                // TODO change the UI of selection
            }
        } else {
            //TODO select image; go to image editor
            Toast.makeText(this, "onAlbumClicked " + item.getRealPath(), Toast.LENGTH_SHORT).show();
            startSingleEditImage(item.getRealPath());
        }
    }

    private void startSingleEditImage(String imageUrlOrPath){
        Intent intent = ImageEditorActivity.getIntent(this, imageUrlOrPath);
        startActivityForResult(intent, REQUEST_CODE_EDITOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDITOR:
                if (resultCode == Activity.RESULT_OK && data!= null) {
                    //TODO setresult the final path
                    finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onImageTaken(String filePath) {
        if (imagePickerBuilder.supportMultipleSelection()) {
            //TODO
            if (imagePickerBuilder.hasThumbnailPreview()) {

            }
            //to cater the bug in library.
            ImagePickerCameraFragment fragment = getCameraFragment();
            if (fragment!= null) {
                fragment.onPause();
                fragment.onResume();
            }
        } else {
            Toast.makeText(this, "onPhotoTaken " + filePath, Toast.LENGTH_SHORT).show();
            startSingleEditImage(filePath);
        }
    }

    private ImagePickerGalleryFragment getGalleryFragment() {
        int tabGallery = imagePickerBuilder.indexTypeDef(ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY);
        if (tabGallery > -1) {
            Fragment fragment = imagePickerViewPagerAdapter.getRegisteredFragment(tabGallery);
            if (fragment instanceof ImagePickerGalleryFragment) {
                return (ImagePickerGalleryFragment) fragment;
            } else {
                return null;
            }
        }
        return null;
    }

    private ImagePickerCameraFragment getCameraFragment() {
        int tabCamera = imagePickerBuilder.indexTypeDef(ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA);
        if (tabCamera > -1) {
            Fragment fragment = imagePickerViewPagerAdapter.getRegisteredFragment(tabCamera);
            if (fragment instanceof ImagePickerCameraFragment) {
                return (ImagePickerCameraFragment) fragment;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SELECTED_TAB, tabLayout.getSelectedTabPosition());
    }

}
