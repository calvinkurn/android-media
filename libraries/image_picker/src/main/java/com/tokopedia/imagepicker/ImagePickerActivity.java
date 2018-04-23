package com.tokopedia.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.imagepicker.adapter.AlbumAdapter;
import com.tokopedia.imagepicker.adapter.ImagePickerViewPagerAdapter;
import com.tokopedia.imagepicker.adapter.TabLayoutImagePickerAdapter;
import com.tokopedia.imagepicker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.gallery.model.MediaItem;

public class ImagePickerActivity extends BaseSimpleActivity
        implements AdapterView.OnItemSelectedListener,
        ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener {

    public static final String EXTRA_IMAGE_PICKER_BUILDER = "x_img_pick_builder";

    public static final String SAVED_SELECTED_TAB = "saved_sel_tab";

    private TabLayout tabLayout;
    private ImagePickerBuilder imagePickerBuilder;

    private int selectedTab = 0;
    private int selectedAlbumPos = 0;
    private ViewPager viewPager;

    private AlbumsSpinner albumSpinner;
    private AlbumAdapter albumAdapter;
    private TextView tvSelectedAlbum;
    private ImagePickerViewPagerAdapter imagePickerViewPagerAdapter;

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

    private void setupPreview(){
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
        viewPager.setAdapter(imagePickerViewPagerAdapter);
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
        if (imagePickerBuilder.isTypeDef(ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY, position)) {
            getSupportActionBar().setTitle("");
            tvSelectedAlbum.setVisibility(View.VISIBLE);
        } else {
            getSupportActionBar().setTitle(getTitle());
            tvSelectedAlbum.setVisibility(View.GONE);
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
        if (selectedAlbumPos!= position) {
            selectedAlbumPos = position;
            onAlbumLoaded(albumAdapter.getCursor());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAlbumLoaded(final Cursor cursor) {
        albumAdapter.swapCursor(cursor);
        if (cursor!= null && cursor.getCount() > selectedAlbumPos) {
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
                    if (imagePickerGalleryFragment!= null) {
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
        }
    }

    private ImagePickerGalleryFragment getGalleryFragment() {
        int tabGallery = imagePickerBuilder.indexTypeDef(ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY);
        if (tabGallery > -1) {
            return (ImagePickerGalleryFragment) imagePickerViewPagerAdapter.getRegisteredFragment(tabGallery);
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SELECTED_TAB, tabLayout.getSelectedTabPosition());
    }

}
