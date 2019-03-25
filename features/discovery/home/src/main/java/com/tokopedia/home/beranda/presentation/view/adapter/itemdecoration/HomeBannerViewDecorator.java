package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HomeBannerViewDecorator extends RecyclerView.ItemDecoration {
    private final int leftFirst;
    private final int left;
    private final int rightLast;
    private final int right;

    public HomeBannerViewDecorator(int leftFirst,
                                   int left,
                                   int rightLast,
                                   int right) {
        this.leftFirst = leftFirst;
        this.left = left;
        this.rightLast = rightLast;
        this.right = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager().getPosition(view) == 0) {
            outRect.left = leftFirst;
        } else {
            outRect.left = left;
        }

        if (parent.getLayoutManager().getPosition(view) ==
                parent.getLayoutManager().getItemCount()-1) {
            outRect.right = rightLast;
        } else {
            outRect.right = right;
        }
    }
}
