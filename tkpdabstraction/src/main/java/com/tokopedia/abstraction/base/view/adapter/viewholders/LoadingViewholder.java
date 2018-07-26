package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;


/**
 * @author Kulomady on 1/25/17.
 */

public class LoadingViewholder extends AbstractViewHolder<LoadingModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.item_shimmering_list;

    public LoadingViewholder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModel element) {
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
