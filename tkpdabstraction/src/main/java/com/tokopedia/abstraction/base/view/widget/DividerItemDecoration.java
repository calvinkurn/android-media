package com.tokopedia.abstraction.base.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.R;

/**
 * Created by Nathaniel on 1/3/2017.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DividerItemDecoration(Context context) {
        this(context, context.getResources().getDrawable(R.drawable.bg_list_separator));
    }

    public DividerItemDecoration(Context context, Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            if (i == (childCount - 1)) {
                continue;
            }

            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + parent.getContext().getResources().getDimensionPixelSize(R.dimen.dp_half);

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
