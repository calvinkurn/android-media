package com.tokopedia.imagepicker.picker.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import com.tokopedia.imagepicker.picker.ImagePickerBuilder;
import com.tokopedia.imagepicker.R;

import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 19/04/18.
 */

public class TabLayoutImagePickerAdapter {
    private @ImagePickerBuilder.ImagePickerTabTypeDef
    int[] tabTypeDef;
    private Context context;
    private TabLayout tabLayout;

    public static @ColorRes int SELECTED_COLOR = R.color.font_black_primary_70;
    public static @ColorRes int UNSELECTED_COLOR = R.color.grey_500;

    public TabLayoutImagePickerAdapter(
            TabLayout tabLayout,
            Context context,
            @ImagePickerBuilder.ImagePickerTabTypeDef int[] tabTypeDef) {
        this.tabLayout = tabLayout;
        this.tabTypeDef = tabTypeDef;
        this.context = context;
    }

    public void notifyDataSetChanged() {
        if (tabLayout.getTabCount() > 0) {
            tabLayout.removeAllTabs();
        }
        for (int tabTypeDefItem : tabTypeDef) {
            TabLayout.Tab tab = tabLayout.newTab();
            switch (tabTypeDefItem) {
                case TYPE_GALLERY:
                    tab.setIcon(R.drawable.circle_red).setText(context.getString(R.string.gallery));
                    unselectTab(tab);
                    tabLayout.addTab(tab);
                    break;
                case TYPE_CAMERA:
                    tab.setIcon(R.drawable.circle_red).setText(context.getString(R.string.camera));
                    unselectTab(tab);
                    tabLayout.addTab(tab);
                    break;
                case TYPE_INSTAGRAM:
                    tab.setIcon(R.drawable.circle_red).setText(context.getString(R.string.instagram));
                    unselectTab(tab);
                    tabLayout.addTab(tab);
                    break;
            }
        }
    }

    private void setColorFilterForTab(TabLayout.Tab tab, int colorRes) {
        Drawable iconDrawable = tab.getIcon();
        if (iconDrawable!= null) {
            iconDrawable.clearColorFilter();
            int tabIconColor = ContextCompat.getColor(context, colorRes);
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
    }

    public void selectTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        setColorFilterForTab(tab, TabLayoutImagePickerAdapter.SELECTED_COLOR);
        if (tabLayout.getSelectedTabPosition()!= position) {
            tab.select();
        }
    }

    public void unselectTab(TabLayout.Tab tab) {
        setColorFilterForTab(tab, TabLayoutImagePickerAdapter.UNSELECTED_COLOR);
    }
}
