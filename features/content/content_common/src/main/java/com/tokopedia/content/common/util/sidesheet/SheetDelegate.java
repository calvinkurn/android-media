package com.tokopedia.content.common.util.sidesheet;

/**
 * @author by astidhiyaa on 13/06/23
 */

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


/**
 * A delegate for {@link com.google.android.material.sidesheet.SideSheetBehavior} to handle logic specific to the sheet's edge position.
 */
abstract class SheetDelegate {

    /**
     * Returns the edge of the screen in which the sheet is positioned. Must be a {@link Sheet.SheetEdge}
     * value.
     */
    @Sheet.SheetEdge
    abstract int getSheetEdge();

    /**
     * Determines whether the sheet is currently settling to a target {@link Sheet.StableSheetState} using
     */
    abstract boolean isSettling(View child, int state, boolean isReleasingView);

    /** Returns the sheet's offset from the origin edge when hidden. */
    abstract int getHiddenOffset();

    /** Returns the sheet's offset from the origin edge when expanded. */
    abstract int getExpandedOffset();

    /**
     * Calculates the target {@link Sheet.StableSheetState} state of the sheet after it's released from a
     * drag, using the x and y velocity of the drag to determine the state.
     *
     * @return the target state
     */
    @Sheet.StableSheetState
    abstract int calculateTargetStateOnViewReleased(
            @NonNull View releasedChild, float xVelocity, float yVelocity);

    /**
     * Whether the sheet should hide, based on the position of child, velocity of the drag event, and
     * {@link SideSheetBehavior#getHideThreshold()}.
     */
    abstract boolean shouldHide(@NonNull View child, float velocity);

    /**
     * Returns the edge of the sheet that the sheet expands towards, calling the child parameter's
     * edge depending on which edge of the screen the sheet is positioned. For a right based sheet,
     * this would return {@code child.getLeft()}.
     */
    abstract <V extends View> int getOutwardEdge(@NonNull V child);

    /**
     * Returns the calculated slide offset based on which edge of the screen the sheet is based on.
     * The offset value increases as the sheet moves towards the outward edge.
     *
     * @return slide offset represented as a float value between 0 and 1. A value of 0 means that the
     *     sheet is hidden and a value of 1 means that the sheet is fully expanded.
     */
    abstract float calculateSlideOffsetBasedOnOutwardEdge(int outwardEdge);

    /** Set the coplanar sheet layout params depending on the screen size. */
    abstract void updateCoplanarSiblingLayoutParams(
            @NonNull ViewGroup.MarginLayoutParams coplanarSiblingLayoutParams, int sheetLeft, int sheetRight);
}
