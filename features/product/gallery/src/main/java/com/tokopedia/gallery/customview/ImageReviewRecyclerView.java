package com.tokopedia.gallery.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.gallery.R;
import com.tokopedia.gallery.adapter.viewholder.GalleryItemViewHolder;

public class ImageReviewRecyclerView extends RecyclerView {
    private static final int SPAN_COUNT = 3;

    public ImageReviewRecyclerView(Context context) {
        super(context);
        init();
    }

    public ImageReviewRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageReviewRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (getAdapter().getItemViewType(position) == GalleryItemViewHolder.LAYOUT) {
                    return 1;
                } else {
                    return SPAN_COUNT;
                }
            }
        });
        this.setLayoutManager(gridLayoutManager);
        this.addItemDecoration(new GalleryItemDecoration(getResources().getDimensionPixelSize(R.dimen.gallery_item_spacing)));
    }
}
