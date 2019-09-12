package com.tokopedia.tkpd.home.util;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;

/**
 * Created by Nisie on 15/07/15.
 */
public class ItemDecorator extends RecyclerView.ItemDecoration {

    private int mItemOffset;
    private int rightMostView;

    public void setRightMostView(int viewType) {
        rightMostView = viewType;
    }

    public ItemDecorator(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemDecorator(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildPosition(view);
        int viewType = parent.getAdapter().getItemViewType(position);
        if (viewType == rightMostView) {
            CommonUtils.dumper("NISIETAG : Masuk sini");
            outRect.set(0, 0, mItemOffset, 0);
        }


    }


}