package com.tokopedia.train.seat.presentation.widget;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class TrainSeatItemDivider extends RecyclerView.ItemDecoration {
    private int displayMode;
    private int maxColumn;

    public TrainSeatItemDivider(int maxColumn) {
        this.maxColumn = maxColumn;
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
        if (maxColumn == 5 && position + 1 % 5 == 2) {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        } else if (maxColumn == 4 && position == 1) {
            outRect.left = 0;
            outRect.right = 20;
            outRect.top = 0;
            outRect.bottom = 0;
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}