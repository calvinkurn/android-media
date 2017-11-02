package com.tokopedia.design.bottomsheet.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.MenuItem;

import com.tokopedia.design.bottomsheet.adapter.BottomSheetAdapterBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetMenuItem;

/**
 * @author normansyahputa on 7/17/17.
 */
public class CheckedAdapterBottomSheetBuilder extends BottomSheetAdapterBuilder {
    private SparseBooleanArray sparseBooleanArray;

    public CheckedAdapterBottomSheetBuilder(Context context) {
        super(context);
        sparseBooleanArray = new SparseBooleanArray();
    }

    public void addSelection(int id, boolean value) {
        sparseBooleanArray.put(id, value);
    }

    @NonNull
    @Override
    protected BottomSheetMenuItem getBottomSheetMenuItem(int itemTextColor, int itemBackground, int tintColor, MenuItem item, int id) {
        CheckedBottomSheetMenuItem checkedBottomSheetMenuItem = new CheckedBottomSheetMenuItem(item, itemTextColor, itemBackground, tintColor);
        checkedBottomSheetMenuItem.setChecked(sparseBooleanArray.get(id));
        return checkedBottomSheetMenuItem;
    }

    @NonNull
    @Override
    protected CheckedBottomSheetItemAdapter getBottomSheetItemAdapter(BottomSheetItemClickListener itemClickListener) {
        return new CheckedBottomSheetItemAdapter(mItems, mMode, itemClickListener);
    }
}