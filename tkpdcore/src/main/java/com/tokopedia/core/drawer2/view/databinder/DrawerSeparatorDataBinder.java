package com.tokopedia.core.drawer2.view.databinder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core2.R;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;


/**
 * Created by nisie on 1/20/17.
 */

public class DrawerSeparatorDataBinder extends DataBinder {

    public DrawerSeparatorDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public RecyclerView.ViewHolder newViewHolder(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_child_separator, parent, false);
        return new RecyclerView.ViewHolder(itemLayoutView) {
        };
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
