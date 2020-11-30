package com.tokopedia.flight.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.flight.R;

public class FullDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public FullDividerItemDecoration(Context context) {
        this(context, context.getResources().getDrawable(R.drawable.flight_bg_line_seperator));
    }

    public FullDividerItemDecoration(Context context, Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
