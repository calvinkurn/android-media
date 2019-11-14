package com.tokopedia.groupchat.common.design;

import android.app.Application;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.groupchat.R;

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
        outRect.top = (int) view.getContext().getResources().getDimension(com.tokopedia.design.R.dimen.dp_8);
    }
}