package com.tokopedia.filter.newdynamicfilter.adapter.viewholder.decoration;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class LinearHorizontalSpacingDecoration extends RecyclerView.ItemDecoration {
    private final int spacingBetween;
    private final int edgeMargin;

    public LinearHorizontalSpacingDecoration(int spacingBetween, int edgeMargin) {
        this.spacingBetween = spacingBetween;
        this.edgeMargin = edgeMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {

        final int pos = parent.getChildAdapterPosition(view);

        outRect.top = 0;
        outRect.left = pos == 0 ? edgeMargin : spacingBetween / 2;
        outRect.right = pos == parent.getAdapter().getItemCount() - 1 ? edgeMargin : spacingBetween / 2;
        outRect.bottom = 0;
    }
}
