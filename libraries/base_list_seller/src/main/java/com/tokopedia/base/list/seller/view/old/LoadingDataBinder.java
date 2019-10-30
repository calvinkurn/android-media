package com.tokopedia.base.list.seller.view.old;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.tokopedia.base.list.seller.R;
/**
 * Created by Nisie on 2/26/16.
 */

/**
 * Use base adapter with visitor pattern from tkpd abstraction
 */
@Deprecated
public class LoadingDataBinder extends DataBinder<LoadingDataBinder.ViewHolder> {

    boolean isFullScreen = false;

    public LoadingDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public LoadingDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loading_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        View view = holder.itemView;
        if (isFullScreen) {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setIsFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
        notifyDataSetChanged();
    }
}
