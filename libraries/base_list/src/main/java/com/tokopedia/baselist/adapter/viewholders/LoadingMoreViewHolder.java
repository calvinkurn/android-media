package com.tokopedia.baselist.adapter.viewholders;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.tokopedia.abstraction.R;
import com.tokopedia.baselist.adapter.model.LoadingMoreModel;


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
