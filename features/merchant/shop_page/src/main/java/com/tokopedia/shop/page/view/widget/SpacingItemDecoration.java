package com.tokopedia.shop.page.view.widget;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private int displayMode;
    private int ignoreOffset = -1; // if set to 3, position 0 to 2 will be ignored.

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    public SpacingItemDecoration(int spacing) {
        this(spacing, -1);
    }

    public SpacingItemDecoration(int spacing, int displayMode) {
        this.spacing = spacing;
        this.displayMode = displayMode;
    }

    public void setIgnoreOffset(int ignoreOffset) {
        this.ignoreOffset = ignoreOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }

    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }
        if (ignoreOffset > position) {
            return;
        }

        switch (displayMode) {
            case HORIZONTAL:
                outRect.left = position == 0 ? spacing * 2: spacing;
                outRect.right = position == itemCount - 1 ? spacing * 2 : spacing;
                outRect.top = spacing;
                outRect.bottom = spacing;
                break;
            case VERTICAL:
                outRect.left = spacing;
                outRect.right = spacing;
                outRect.top = position == 0 ? spacing * 2: spacing;
                outRect.bottom = position == itemCount - 1 ? spacing * 2: spacing;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int totalCol = gridLayoutManager.getSpanCount();
                    int totalRow = itemCount / totalCol;

                    int colPosition = position % totalCol;
                    int rowPosition = position / totalCol;
                    boolean isRightMost = colPosition == totalCol - 1;
                    boolean isLeftMost = colPosition == 0;
                    boolean isBottomMost = rowPosition == totalRow - 1;
                    boolean isTopMost = rowPosition == 0;

                    outRect.left = isLeftMost ? spacing * 2 : spacing;
                    outRect.right = isRightMost ? spacing * 2 : spacing;
                    outRect.top = isTopMost ? spacing * 2 : spacing;
                    outRect.bottom = isBottomMost ? spacing * 2 : spacing;
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}