package com.tokopedia.baselist.adapter.viewholders;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.baselist.R;
import com.tokopedia.baselist.adapter.model.EmptyResultViewModel;


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
