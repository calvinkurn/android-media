package com.tokopedia.product.manage.item.utils;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hendry on 4/7/2017.
 */

public class HorizontalItemDecoration extends RecyclerView.ItemDecoration {

    private final int padding;

    public HorizontalItemDecoration(int padding) {
        this.padding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = padding;
        }
    }
}