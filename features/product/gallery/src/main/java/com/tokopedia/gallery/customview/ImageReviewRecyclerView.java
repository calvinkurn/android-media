package com.tokopedia.gallery.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.gallery.R;

public class ImageReviewRecyclerView extends VerticalRecyclerView {

    public ImageReviewRecyclerView(Context context) {
        super(context);
    }

    public ImageReviewRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageReviewRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ItemDecoration getItemDecoration() {
        return new GalleryItemDecoration(getResources().getDimensionPixelSize(R.dimen.gallery_item_spacing));
    }
}
