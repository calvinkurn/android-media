package com.tokopedia.baselist.adapter.viewholders;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.baselist.R;
import com.tokopedia.baselist.adapter.Visitable;

public class HideViewHolder extends AbstractViewHolder<Visitable> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_hide;

    public HideViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
    }

    @Override
    public void bind(Visitable visitable) {

    }
}