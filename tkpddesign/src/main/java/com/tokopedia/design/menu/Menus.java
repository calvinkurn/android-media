package com.tokopedia.design.menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.design.R;
import com.tokopedia.design.bottomsheet.BaseBottomSheetView;

/**
 * @author okasurya on 2/13/18.
 */

public class Menus extends BaseBottomSheetView {

    public Menus(@NonNull Context context) {
        super(context);
    }

    public Menus(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected Menus(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.widget_menu;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.widget_menu);

        // set item view
        // add by menu.xml
        // add by object
        // get size list
        // init listener
    }

    private class MenuItem {

        private int icon;
        private String title;

        public MenuItem(int icon, String title) {
            this.icon = icon;
            this.title = title;
        }
    }

    private class ListAdapter {

    }
}
