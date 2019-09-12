package com.tokopedia.train.passenger.presentation.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.tkpdtrain.R;

/**
 * Created by nabillasabbaha on 16/07/18.
 */
public class TrainFullDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public TrainFullDividerItemDecoration(Context context) {
        this(context, context.getResources().getDrawable(R.drawable.bg_line_separator));
    }

    public TrainFullDividerItemDecoration(Context context, Drawable drawable) {
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