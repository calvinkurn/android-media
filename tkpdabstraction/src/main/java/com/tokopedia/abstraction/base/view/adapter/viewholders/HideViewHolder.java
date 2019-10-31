package com.tokopedia.abstraction.base.view.adapter.viewholders;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.Visitable;

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