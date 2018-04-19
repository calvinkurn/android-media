package com.tokopedia.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.imagepicker.adapter.ImagePickerViewPagerAdapter;
import com.tokopedia.imagepicker.adapter.TabLayoutImagePickerAdapter;

public class ImagePickerActivity extends BaseSimpleActivity {

    public static final String EXTRA_IMAGE_PICKER_BUILDER = "x_img_pick_builder";

    public static final String SAVED_SELECTED_TAB = "saved_sel_tab";

    private TabLayout tabLayout;
    private ImagePickerBuilder imagePickerBuilder;

    private int selectedTab = 0;
    private ViewPager viewPager;

    public static Intent getIntent(Context context, ImagePickerBuilder imagePickerBuilder) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder);
        return intent;
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

        setupViewPager();
        setupTabLayout();
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.view_pager);
        ImagePickerViewPagerAdapter imagePickerViewPagerAdapter = new ImagePickerViewPagerAdapter(getSupportFragmentManager(),
                imagePickerBuilder.getTabTypeDef());
        viewPager.setAdapter(imagePickerViewPagerAdapter);
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tab_layout);
        final TabLayoutImagePickerAdapter tabLayoutImagePickerAdapter =
                new TabLayoutImagePickerAdapter( tabLayout, this, imagePickerBuilder.getTabTypeDef());
        tabLayoutImagePickerAdapter.notifyDataSetChanged();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayoutImagePickerAdapter.selectTab(tab);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayoutImagePickerAdapter.unselectTab(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayoutImagePickerAdapter.selectTab(tabLayout.getTabAt(selectedTab));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_image_picker;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SELECTED_TAB, tabLayout.getSelectedTabPosition());
    }
}
