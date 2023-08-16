package com.tokopedia.content.common.util.sidesheet;

import android.view.View;

import androidx.annotation.NonNull;


interface SheetCallback {

    /**
     * Called when the sheet changes its state.
     *
     * @param sheet The sheet view.
     * @param newState The new state.
     */
    void onStateChanged(@NonNull View sheet, @Sheet.SheetState int newState);

    /**
     * Called when the sheet is being dragged.
     *
     * @param sheet The sheet view.
     * @param slideOffset The new offset of this sheet.
     */
    void onSlide(@NonNull View sheet, float slideOffset);
}
