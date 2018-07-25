package com.tokopedia.shop.product.view.adapter.newadapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.shop.R;

public class EmptyWrapViewHolder extends BaseEmptyViewHolder<EmptyModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_view_wrap_no_result;

    public EmptyWrapViewHolder(View itemView) {
        super(itemView);
    }

    public EmptyWrapViewHolder(View itemView, Callback callback) {
        super(itemView, callback);
    }

    @Override
    public void bind(EmptyModel element) {
        super.bind(element);
    }
}
