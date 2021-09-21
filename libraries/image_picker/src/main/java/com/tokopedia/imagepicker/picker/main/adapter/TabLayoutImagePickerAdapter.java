package com.tokopedia.imagepicker.picker.main.adapter;

import android.content.Context;
import androidx.annotation.ColorRes;
import com.google.android.material.tabs.TabLayout;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImagePickerTab;

/**
 * Created by hendry on 19/04/18.
 */

public class TabLayoutImagePickerAdapter {
    private ImagePickerTab[] tabTypeDef;
    private Context context;
    private TabLayout tabLayout;

    public static @ColorRes
    int SELECTED_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N700_68;
    public static @ColorRes
    int UNSELECTED_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N200;

    public TabLayoutImagePickerAdapter(
            TabLayout tabLayout,
            Context context,
            ImagePickerTab[] tabTypeDef) {
        this.tabLayout = tabLayout;
        this.tabTypeDef = tabTypeDef;
        this.context = context;
    }

    public void notifyDataSetChanged() {
        if (tabLayout.getTabCount() > 0) {
            tabLayout.removeAllTabs();
        }
        for (ImagePickerTab tabTypeDefItem : tabTypeDef) {
            TabLayout.Tab tab = tabLayout.newTab();
            switch (tabTypeDefItem) {
                case TYPE_GALLERY:
                    tab.setText(context.getString(R.string.gallery));
                    tabLayout.addTab(tab);
                    break;
                case TYPE_CAMERA:
                    tab.setText(context.getString(R.string.camera));
                    tabLayout.addTab(tab);
                    break;
            }
        }
    }
}
