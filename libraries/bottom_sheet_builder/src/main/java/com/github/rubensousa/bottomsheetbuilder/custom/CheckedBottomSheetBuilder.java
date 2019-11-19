package com.github.rubensousa.bottomsheetbuilder.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetAdapterBuilder;

/**
 * Created by normansyahputa on 7/17/17.
 */

public class CheckedBottomSheetBuilder extends BottomSheetBuilder {
    public CheckedBottomSheetBuilder(Context context, CoordinatorLayout coordinatorLayout) {
        super(context, coordinatorLayout);
    }

    public CheckedBottomSheetBuilder(Context context) {
        super(context);
    }

    public CheckedBottomSheetBuilder(Context context, @StyleRes int theme) {
        super(context, theme);
    }

    public BottomSheetBuilder addItem(int id, @StringRes int title, @DrawableRes int icon, boolean value) {
        return addItem(id, mContext.getString(title), ContextCompat.getDrawable(mContext, icon), value);
    }

    public BottomSheetBuilder addItem(int id, @StringRes int title, Drawable icon, boolean value) {
        return addItem(id, mContext.getString(title), icon, value);
    }

    public BottomSheetBuilder addItem(int id, String title, @DrawableRes int icon, boolean value) {
        return addItem(id, title, ContextCompat.getDrawable(mContext, icon), value);
    }

    public BottomSheetBuilder addItem(int id, String title, Drawable icon, boolean value) {
        if (mAdapterBuilder != null && mAdapterBuilder instanceof CheckedAdapterBottomSheetBuilder) {
            ((CheckedAdapterBottomSheetBuilder) mAdapterBuilder).addSelection(id, value);
        }
        mAdapterBuilder.addItem(id, title, icon, mItemTextColor, mItemBackground, mIconTintColor);
        return this;
    }

    public BottomSheetBuilder setSelection(String menuTitle) {
        int menuLength = menu != null ? menu.size() : mAdapterBuilder.getItems().size();
        for (int i = 0; i < menuLength; i++) {
            String menuItem = menu != null ? menu.getItem(i).getTitle().toString() : mAdapterBuilder.getItems().get(i).getTitle();
            boolean titleSelected = !TextUtils.isEmpty(menuTitle) && menuTitle.equalsIgnoreCase(menuItem);
            if (mAdapterBuilder != null && mAdapterBuilder instanceof CheckedAdapterBottomSheetBuilder) {
                ((CheckedAdapterBottomSheetBuilder) mAdapterBuilder).addSelection(i, titleSelected);
            }
        }
        return this;
    }

    @NonNull
    @Override
    protected BottomSheetAdapterBuilder getBottomSheetAdapterBuilder() {
        return new CheckedAdapterBottomSheetBuilder(mContext);
    }
}