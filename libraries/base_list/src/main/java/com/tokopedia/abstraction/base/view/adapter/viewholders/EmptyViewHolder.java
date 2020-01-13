package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.baselist.R;


/**
 * @author kulomady on 1/24/17.
 */

public class EmptyViewHolder extends BaseEmptyViewHolder<EmptyModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_view_no_result;

    public EmptyViewHolder(View itemView) {
        super(itemView);
    }

    public EmptyViewHolder(View itemView, BaseEmptyViewHolder.Callback callback) {
        super(itemView, callback);
    }

    @Override
    public void bind(EmptyModel element) {
        super.bind(element);
    }
}
