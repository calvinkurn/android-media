package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;


/**
 * @author kulomady on 1/24/17.
 */

public class EmptyResultViewHolder extends BaseEmptyViewHolder<EmptyResultViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_empty_list;

    public EmptyResultViewHolder(View itemView) {
        super(itemView);
    }

    public EmptyResultViewHolder(View itemView, Callback callback) {
        super(itemView, callback);
    }
}
