package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.baselist.R;


/**
 * @author Kulomady on 1/25/17.
 */

public class LoadingMoreViewHolder extends AbstractViewHolder<LoadingMoreModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.loading_layout;

    public LoadingMoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingMoreModel element) {
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

}
