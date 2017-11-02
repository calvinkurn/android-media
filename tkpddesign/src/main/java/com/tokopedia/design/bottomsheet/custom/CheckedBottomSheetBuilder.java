package com.tokopedia.design.bottomsheet.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetAdapterBuilder;

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