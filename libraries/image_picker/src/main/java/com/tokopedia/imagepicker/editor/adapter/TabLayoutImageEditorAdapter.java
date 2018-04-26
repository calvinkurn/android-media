package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.ImagePickerBuilder;

import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_ROTATE;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_WATERMARK;

/**
 * Created by hendry on 19/04/18.
 */

public class TabLayoutImageEditorAdapter {
    private @ImagePickerBuilder.ImagePickerTabTypeDef
    int[] tabTypeDef;
    private Context context;
    private TabLayout tabLayout;

    public static @ColorRes int SELECTED_COLOR = R.color.font_black_primary_70;
    public static @ColorRes int UNSELECTED_COLOR = R.color.font_black_primary_70;

    public TabLayoutImageEditorAdapter(
            TabLayout tabLayout,
            Context context,
            @ImagePickerBuilder.ImageEditActionTypeDef int[] tabTypeDef) {
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
                case TYPE_CROP:
                    tab.setIcon(R.drawable.ic_crop).setText(context.getString(R.string.crop));
                    unselectTab(tab);
                    tabLayout.addTab(tab);
                    break;
                case TYPE_ROTATE:
                    tab.setIcon(R.drawable.ic_rotate).setText(context.getString(R.string.rotate));
                    unselectTab(tab);
                    tabLayout.addTab(tab);
                    break;
                case TYPE_WATERMARK:
                    tab.setIcon(R.drawable.circle_red).setText(context.getString(R.string.watermark));
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
        setColorFilterForTab(tab, TabLayoutImageEditorAdapter.SELECTED_COLOR);
        if (tabLayout.getSelectedTabPosition()!= position) {
            tab.select();
        }
    }

    public void unselectTab(TabLayout.Tab tab) {
        setColorFilterForTab(tab, TabLayoutImageEditorAdapter.UNSELECTED_COLOR);
    }
}
