package com.tokopedia.core.customadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

/**
 * Created by Nisie on 2/26/16.
 */
@Deprecated
public class RetryDataBinder extends DataBinder<RetryDataBinder.ViewHolder> {
    boolean isFullScreen = false;


    public interface OnRetryListener {
        void onRetryCliked();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView retry;
        TextView retryTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            retry = (TextView) itemView.findViewById(R.id.retry_but);
            retryTitle = (TextView) itemView.findViewById(R.id.retry_text);
        }
    }

    private OnRetryListener listener;

    public RetryDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public RetryDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_retry, null);
        view.setLayoutParams(new AbsListView.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        View view = holder.itemView;
        if (isFullScreen) {
            holder.retryTitle.setVisibility(View.VISIBLE);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            holder.retryTitle.setVisibility(View.GONE);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        holder.retry.setOnClickListener(onRetryListener());


    }

    private View.OnClickListener onRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRetryCliked();
            }
        };
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setOnRetryListenerRV(RetryDataBinder.OnRetryListener listener) {
        this.listener = listener;
    }

    public void setIsFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
        notifyDataSetChanged();
    }
}
