package com.tokopedia.core.customadapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.shopinfo.adapter.ExtrasDelegate;

/**
 * Created by Tkpd_Eka on 11/2/2015.
 */
public abstract class AbstractRecyclerAdapter extends RecyclerView.Adapter {

    public abstract RecyclerView.ViewHolder onCreateVHolder(ViewGroup parent, int viewType);

    public abstract void onBindVHolder(RecyclerView.ViewHolder holder, int position);

    public abstract int getChildItemCount();

    public abstract int getItemType(int pos);

    public abstract View.OnClickListener getRetryClickListener();

    protected static final int EXTRA_LOADING = -101;
    protected static final int EXTRA_NO_RESULT = -102;
    protected static final int EXTRA_RETRY = -103;

    protected ExtrasDelegate extrasDelegate = new ExtrasDelegate();

    protected int extra = 0;
    protected int extraState = EXTRA_LOADING;

    public void setLoading() {
        extraState = EXTRA_LOADING;
        addExtra();
    }

    public void setNoResult() {
        extraState = EXTRA_NO_RESULT;
        addExtra();
    }

    public void setRetry() {
        extraState = EXTRA_RETRY;
        addExtra();
    }

    public void removeLoading() {
        removeExtra();
    }

    public void removeNoResult() {
        removeExtra();
    }

    public void removeRetry() {
        removeExtra();
    }

    public void addExtra() {
        extra = 1;
        notifyDataSetChanged();
    }

    public void removeExtra() {
        extraState = 0;
        extra = 0;
        notifyDataSetChanged();
    }

    public boolean hasNoResult(){
        return (extra == 1 && extraState == EXTRA_NO_RESULT);
    }

    public boolean hasLoading(){ return (extra == 1 && extraState == EXTRA_LOADING);}

    public boolean hasRetry(){
        return (extra == 1 && extraState == EXTRA_RETRY);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType != extraState || extraState==0)
            return onCreateVHolder(parent, viewType);
        else
            return onCreateExtraVH(parent);
    }

    private RecyclerView.ViewHolder onCreateExtraVH(ViewGroup parent){
        switch (extraState) {
            case EXTRA_LOADING:
                return extrasDelegate.onCreateViewHolderLoading(parent);
            case EXTRA_NO_RESULT:
                return extrasDelegate.onCreateViewHolderNoResult(parent);
            case EXTRA_RETRY:
                return extrasDelegate.onCreateViewHolderRetry(parent);
            default:
                return null;
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLastItem(position) && extra == 1) {
            bindExtras(holder);
        } else
            onBindVHolder(holder, position);
    }

    private void bindExtras(RecyclerView.ViewHolder holder) {
        switch (extraState) {
            case EXTRA_RETRY:
                extrasDelegate.onBindRetry(holder, getRetryClickListener());
                break;
            default:
                break;
        }
    }

    @Override
    public final int getItemCount() {
        return getChildItemCount() + extra;
    }

    protected boolean isLastItem(int pos) {
        return pos == getChildItemCount();
    }

    @Override
    public final int getItemViewType(int position) {
        return (isLastItem(position) && extra == 1) ? extraState : getItemType(position);
    }
}
