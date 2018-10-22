package com.tokopedia.core.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 19/06/15.
 */

/**
 * Use base adapter with visitor pattern from tkpd abstraction
 */
@Deprecated
public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnRetryListener listener;


    public interface OnRetryListener {
        public void onRetryCliked();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolderRetry extends RecyclerView.ViewHolder {
        @BindView(R2.id.main_retry)
        TextView retry;

        public ViewHolderRetry(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ViewHolderErrorNetworkState extends RecyclerView.ViewHolder {
        @BindView(R2.id.button_retry)
        TextView retry;

        public ViewHolderErrorNetworkState(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ViewHolderEmpty extends RecyclerView.ViewHolder {
        @BindView(R2.id.no_result_image)
        ImageView emptyImage;

        public ViewHolderEmpty(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    protected Context context;
    protected List<RecyclerViewItem> data;

    public BaseRecyclerViewAdapter(Context context, List<RecyclerViewItem> data) {
        this.context = context;
        this.data = data;
    }

    protected int loading = 0;
    protected int retry = 0;
    protected int errorState = 0;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TkpdState.RecyclerView.VIEW_LOADING) {
            return createViewLoading(viewGroup);
        } else if (viewType == TkpdState.RecyclerView.VIEW_RETRY) {
            return createViewRetry(viewGroup);
        } else if (viewType == TkpdState.RecyclerView.VIEW_ERROR_NETWORK) {
            return createViewErrorNetworkState(viewGroup);
        } else {
            return createViewEmpty(viewGroup);
        }
    }

    private ViewHolderEmpty createViewEmpty(ViewGroup viewGroup) {
        ViewGroup parent = viewGroup;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_no_result, viewGroup, false);
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolderEmpty(view);
    }

    private ViewHolderRetry createViewRetry(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_retry_footer, viewGroup, false);
        return new ViewHolderRetry(view);
    }

    private ViewHolderErrorNetworkState createViewErrorNetworkState(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_error_network, viewGroup, false);
        return new ViewHolderErrorNetworkState(view);
    }

    public ViewHolder createViewLoading(ViewGroup viewGroup) {
        ViewGroup parent = viewGroup;
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.loading_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CommonUtils.dumper("NISIETAGCONNECTION : BIND TYPE : " + getItemViewType(position));
        switch (getItemViewType(position)) {
            case TkpdState.RecyclerView.VIEW_ERROR_NETWORK:
                bindRetryErrorStateHolder((ViewHolderErrorNetworkState) viewHolder);
                break;
            case TkpdState.RecyclerView.VIEW_RETRY:
                bindRetryHolder((ViewHolderRetry) viewHolder);
                break;
            case TkpdState.RecyclerView.VIEW_EMPTY:
                ImageHandler.loadImageWithId(((ViewHolderEmpty) viewHolder).emptyImage, R.drawable.status_no_result);
                break;
        }
    }

    private void bindRetryHolder(ViewHolderRetry viewHolder) {
        CommonUtils.dumper("NISIETAGCONNECTION : BIND RETRY");
        viewHolder.retry.setOnClickListener(onRetryListener());
    }

    private void bindRetryErrorStateHolder(ViewHolderErrorNetworkState viewHolder) {
        viewHolder.retry.setOnClickListener(onRetryListener());
    }

    private View.OnClickListener onRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.dumper("NISIETAGCONNECTION : RETRY CLICKED");
                setIsRetry(false);
                setIsLoading(true);
                notifyDataSetChanged();
                if(listener!=null) {
                    listener.onRetryCliked();
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return 1;
        else return data.size() + loading + retry + errorState;
    }

    public boolean isEmpty() {
        if (data.size() == 0) return true;
        else return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading() && isLastItemPosition(position)) {
            return TkpdState.RecyclerView.VIEW_LOADING;
        } else if (isRetry() && isLastItemPosition(position)) {
            return TkpdState.RecyclerView.VIEW_RETRY;
        } else if (isErrorState()) {
            return TkpdState.RecyclerView.VIEW_ERROR_NETWORK;
        } else {
            return TkpdState.RecyclerView.VIEW_EMPTY;
        }
    }

    public boolean isRetry() {
        if (retry == 1) {
            return true;
        } else
            return false;
    }

    private boolean isErrorState() {
        if (errorState == 1) {
            return true;
        } else
            return false;
    }

    public void setIsLoading(boolean isLoading) {
        if (isLoading) {
            loading = 1;
        } else {
            loading = 0;
        }
        notifyDataSetChanged();
    }

    public void setIsErrorState(boolean isError) {
        if (isError) {
            errorState = 1;
        } else {
            errorState = 0;
        }
        notifyDataSetChanged();
    }

    public void setIsRetry(boolean isRetry) {
        if (isRetry) {
            retry = 1;
        } else {
            retry = 0;
        }
        notifyDataSetChanged();
    }

    public boolean isLastItemPosition(int position) {
        if (position == data.size()) return true;
        else return false;
    }

    public boolean isLoading() {
        if (loading == 1) {
            return true;
        } else
            return false;
    }

    public void setOnRetryListenerRV(BaseRecyclerViewAdapter.OnRetryListener listener) {
        this.listener = listener;
    }

    public List<RecyclerViewItem> getData() {
        return data;
    }
}