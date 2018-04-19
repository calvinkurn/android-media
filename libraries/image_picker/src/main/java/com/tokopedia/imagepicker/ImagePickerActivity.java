package com.tokopedia.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

public class ImagePickerActivity extends BaseSimpleActivity {

    public static final String EXTRA_IMAGE_PICKER_BUILDER = "x_img_pick_builder";

    public static final String SAVED_SELECTED_TAB = "saved_sel_tab";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImagePickerBuilder imagePickerBuilder;

    private int selectedTab = 0;

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
            savedInstanceState.getInt(SAVED_SELECTED_TAB, 0);
        }

        initViewPager();
        initTabLayout();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.view_pager);

    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tab_layout);
        if (tabLayout.getTabCount() > 0) {
            return;
        }
        int[] tabTypeDef = imagePickerBuilder.getTabTypeDef();
        for (int tabTypeDefItem : tabTypeDef) {
            TabLayout.Tab tab = tabLayout.newTab();
            switch (tabTypeDefItem) {
                case TYPE_GALLERY:
                    tabLayout.addTab(tab.setIcon(R.drawable.a).setText(getString(R.string.gallery)));
                    break;
                case TYPE_CAMERA:
                    tabLayout.addTab(tab.setIcon(R.drawable.a).setText(getString(R.string.camera)));
                    break;
                case TYPE_INSTAGRAM:
                    tabLayout.addTab(tab.setIcon(R.drawable.a).setText(getString(R.string.instagram)));
                    break;
            }
        }
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
