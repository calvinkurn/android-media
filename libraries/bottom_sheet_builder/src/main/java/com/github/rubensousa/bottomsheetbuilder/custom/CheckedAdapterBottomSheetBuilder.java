package com.github.rubensousa.bottomsheetbuilder.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.MenuItem;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetAdapterBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetMenuItem;

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