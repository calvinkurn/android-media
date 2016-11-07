package com.tokopedia.tkpd.selling.presenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.var.TkpdState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Erry on 7/18/2016.
 */
public abstract class BaseSellingAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    Class<T> mModelClass;
    protected int mModelLayout;
    Class<VH> mViewHolderClass;
    private static final String TAG = BaseSellingAdapter.class.getSimpleName();
    private List<T> mListModel;

    private OnRetryListener listener;

    public interface OnRetryListener {
        public void onRetryCliked();
    }

    public static class ViewHolder extends SwappingHolder {
        public ViewHolder(View itemView) {
//            super(itemView);
            super(itemView, new MultiSelector());
        }
    }

    public static class ViewHolderRetry extends SwappingHolder {
        @Bind(R2.id.button_retry)
        TextView retry;

        public ViewHolderRetry(View itemView) {
//            super(itemView);
            super(itemView, new MultiSelector());
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ViewHolderEmpty extends SwappingHolder {
        @Bind(R2.id.no_result_image)
        ImageView emptyImage;

        public ViewHolderEmpty(View itemView) {
//            super(itemView);
            super(itemView, new MultiSelector());
            ButterKnife.bind(this, itemView);
        }
    }

    protected int loading = 0;
    protected int retry = 0;

    public BaseSellingAdapter(Class<T> mModelClass, int mModelLayout, Class<VH> mViewHolderClass) {
        this.mModelClass = mModelClass;
        this.mModelLayout = mModelLayout;
        this.mViewHolderClass = mViewHolderClass;
        this.mListModel = new ArrayList<>();
    }

    public void setListModel(List<T> mListModel) {
        this.mListModel.addAll(mListModel);
        setIsRetry(false);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TkpdState.RecyclerView.VIEW_LOADING) {
            return (VH) createViewLoading(parent);
        } else if (viewType == TkpdState.RecyclerView.VIEW_RETRY) {
            return (VH) createViewRetry(parent);
        } else if (getListData().size() == 0) {
            return (VH) createViewEmpty(parent);
        } else {
            return getViewHolder(mModelLayout, parent);
        }
    }

    public ViewHolderEmpty createViewEmpty(ViewGroup viewGroup) {
        ViewGroup parent = viewGroup;
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_no_result, viewGroup, false);
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolderEmpty(view);
    }

    public ViewHolderRetry createViewRetry(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_error_network, viewGroup, false);
        return new ViewHolderRetry(view);
    }

    public ViewHolder createViewLoading(ViewGroup viewGroup) {
        ViewGroup parent = viewGroup;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (getItemViewType(position)) {
            case TkpdState.RecyclerView.VIEW_RETRY:
                bindRetryHolder((ViewHolderRetry) holder);
                break;
            case TkpdState.RecyclerView.VIEW_EMPTY:
                ImageHandler.loadImageWithId(((ViewHolderEmpty) holder).emptyImage, R.drawable.status_no_result);
                break;
            default:
                if (position < getListData().size()){
                    T model = getItem(position);
                    populateViewHolder(holder, model, position);
                }
        }
    }

    private void bindRetryHolder(ViewHolderRetry viewHolder) {
        viewHolder.retry.setOnClickListener(onRetryListener());
    }

    private View.OnClickListener onRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIsRetry(false);
                setIsLoading(true);
                notifyDataSetChanged();
                if(listener!=null) {
                    listener.onRetryCliked();
                }
            }
        };
    }

    public boolean isEmpty() {
        if (mListModel.size() == 0) return true;
        else return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading() && isLastItemPosition(position)) {
            return TkpdState.RecyclerView.VIEW_LOADING;
        } else if (isRetry() && isLastItemPosition(position)) {
            return TkpdState.RecyclerView.VIEW_RETRY;
        } else if (getListData().size() == 0) {
            return TkpdState.RecyclerView.VIEW_EMPTY;
        } else {
            return mModelLayout;
        }
    }

    private boolean isRetry() {
        if (retry == 1) {
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

    public void setIsRetry(boolean isRetry) {
        if (isRetry) {
            retry = 1;
        } else {
            retry = 0;
        }
        notifyDataSetChanged();
    }

    public boolean isLastItemPosition(int position) {
        if (position == mListModel.size()) return true;
        else return false;
    }

    public boolean isLoading() {
        if (loading == 1) {
            return true;
        } else
            return false;
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.listener = listener;
    }

    public List<T> getListData() {
        return mListModel;
    }

    public void clearData(){
        mListModel.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return 1;
        else return mListModel.size() + loading + retry;
    }

    public T getItem(int position) {
        return mListModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListModel.get(position).hashCode();
    }

    abstract protected void populateViewHolder(VH viewHolder, T model, int position);

    abstract protected VH getViewHolder(int mModelLayout, ViewGroup parent);

}
