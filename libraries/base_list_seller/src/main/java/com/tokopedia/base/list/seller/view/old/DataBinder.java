package com.tokopedia.base.list.seller.view.old;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Nisie on 2/26/16.
 */
@Deprecated
abstract public class DataBinder<T extends RecyclerView.ViewHolder> {

    private DataBindAdapter mDataBindAdapter;

    public DataBinder(DataBindAdapter dataBindAdapter) {
        mDataBindAdapter = dataBindAdapter;
    }

    abstract public T newViewHolder(ViewGroup parent);

    abstract public void bindViewHolder(T holder, int position);

    abstract public int getItemCount();

    public final void notifyDataSetChanged() {
        mDataBindAdapter.notifyDataSetChanged();
    }

}
