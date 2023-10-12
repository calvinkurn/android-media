package com.tokopedia.content.common.util.sidesheet;

import android.view.View;

import androidx.annotation.NonNull;

public abstract class SideSheetCallback implements SheetCallback {

    /**
     * Called when the sheet changes its state.
     *
     * @param sheet The sheet view.
     * @param newState The new state. This should be one of {@link com.google.android.material.sidesheet.SideSheetBehavior#STATE_DRAGGING},
     *     {@link SideSheetBehavior#STATE_HIDDEN}.
     */
    @Override
    public abstract void onStateChanged(@NonNull View sheet, @Sheet.SheetState int newState);

    /**
     * Called when the sheet is being dragged.
     *
     * @param sheet The sheet view.
     * @param slideOffset The new offset of this sheet within [0,1] range. Offset increases as this
     *     sheet is moving towards the outward edge. A value of 0 means that the sheet is hidden, and
     *     a value of 1 means that the sheet is fully expanded.
     */
    @Override
    public abstract void onSlide(@NonNull View sheet, float slideOffset);

    void onLayout(@NonNull View sheet) {}
}

