package com.tokopedia.groupchat.common.design;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author by StevenFredian on 27/02/18.
 */

public class QuickReplyItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public QuickReplyItemDecoration(int dimension) {
        this.space = dimension;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = space;
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.right = space;
        }
    }
}