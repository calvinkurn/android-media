package com.tokopedia.content.common.util.sidesheet;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

interface Sheet<C extends SheetCallback> {
    /** The sheet is dragging. */
    int STATE_DRAGGING = 1;

    /** The sheet is settling. */
    int STATE_SETTLING = 2;

    /** The sheet is expanded. */
    int STATE_EXPANDED = 3;

    /** The sheet is hidden. */
    int STATE_HIDDEN = 5;

    /**
     * States that a sheet can be in.
     *
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef({
            STATE_EXPANDED,
            STATE_DRAGGING,
            STATE_SETTLING,
            STATE_HIDDEN,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface SheetState {}

    /**
     * Stable states that can be set by the a sheet's {@code setState(int)} method. These includes all
     * the possible states a sheet can be in when it's settled.
     *
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef({STATE_EXPANDED, STATE_HIDDEN})
    @Retention(RetentionPolicy.SOURCE)
    @interface StableSheetState {}

    /**
     * The sheet is based on the right edge of the screen; it slides from the right edge towards the
     * left.
     */
    int EDGE_RIGHT = 0;

    /**
     * The edge of the screen that a sheet slides out of.
     *
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef({EDGE_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface SheetEdge {}

    /**
     * Gets the current state of the sheet.
     *
     * @return One of {@link #STATE_EXPANDED}, {@link #STATE_DRAGGING}, or {@link #STATE_SETTLING}.
     */
    @SheetState
    int getState();

    /** Sets the current state of the sheet. Must be one of {@link com.google.android.material.sidesheet.Sheet.StableSheetState}. */
    void setState(@Sheet.StableSheetState int state);

    /**
     * Adds a callback to be notified of sheet events.
     *
     * @param callback The callback to notify when sheet events occur.
     */
    void addCallback(C callback);

    /**
     * Removes a callback to be notified of sheet events.
     *
     * @param callback The callback to remove
     */
    void removeCallback(C callback);
}
