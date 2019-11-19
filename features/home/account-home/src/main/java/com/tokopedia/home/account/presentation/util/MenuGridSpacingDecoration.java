package com.tokopedia.home.account.presentation.util;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author okasurya on 7/24/2018
 */
public class MenuGridSpacingDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private int spacingBottom;
    private boolean includeEdge;

    public MenuGridSpacingDecoration(int spanCount, int spacing, int spacingBottom, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.spacingBottom = spacingBottom;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacingBottom; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}