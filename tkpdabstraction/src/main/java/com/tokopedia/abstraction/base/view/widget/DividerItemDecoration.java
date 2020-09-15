package com.tokopedia.abstraction.base.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.DimenRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.R;

/**
 * Created by Nathaniel on 1/3/2017.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int VERTICAL = LinearLayout.VERTICAL;
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;

    private int mOrientation;
    private Drawable mDivider;
    private boolean usePaddingLeft = true;

    public DividerItemDecoration(Context context) {
        this(VERTICAL, context.getResources().getDrawable(R.drawable.bg_line_separator_thin));
    }

    public DividerItemDecoration(Context context, int orientation) {
        this(orientation, context.getResources().getDrawable(R.drawable.bg_line_separator_thin));
    }

    public DividerItemDecoration(Drawable drawable) {
        this(VERTICAL, drawable);
    }

    public DividerItemDecoration(int orientation, Drawable drawable) {
        mOrientation = orientation;
        mDivider = drawable;
    }

    public void setUsePaddingLeft(boolean usePaddingLeft) {
        this.usePaddingLeft = usePaddingLeft;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int left = usePaddingLeft?
                parent.getContext().getResources().getDimensionPixelOffset(getDimenPaddingLeft())
                : 0;
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            if (i == (childCount - 1) && !shouldDrawOnLastItem()) {
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

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int top;
        int bottom;

        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            c.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            if (i == (childCount - 1) && !shouldDrawOnLastItem()) {
                continue;
            }

            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getRight() + params.rightMargin;
            int right = left + parent.getContext().getResources().getDimensionPixelSize(R.dimen.dp_half);

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    protected boolean shouldDrawOnLastItem() {
        return false;
    }

    @DimenRes
    protected int getDimenPaddingLeft() {
        return R.dimen.dp_16;
    }
}
