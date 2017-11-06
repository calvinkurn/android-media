package com.tokopedia.abstraction.base.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.binder.LoadingDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.NoResultDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.RetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class BaseListV2Adapter<T extends ItemType> extends BaseLinearRecyclerViewAdapter {

    public static final int DEFAULT_ROW_PER_PAGE = 100;

    public static final int VIEW_EMPTY_SEARCH = 666;

    private int rowPerPage;
    private List<T> data = new ArrayList<>();
    protected OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener;

    private int totalItem;
    private String searchQueryString;
    private NoResultDataBinder emptyNoResultDataBinder;
    private LoadingDataBinder loadingDataBinder;

    private int pageToLoad = 1;

    public interface OnBaseListV2AdapterListener<T> {
        void onItemClicked(T t);

        void loadData(int page, int currentDataSize, int rowPerPage);
    }

    public BaseListV2Adapter(OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        this(null, DEFAULT_ROW_PER_PAGE, onBaseListV2AdapterListener);
    }

    public BaseListV2Adapter(@Nullable List<T> data, int rowPerPage,
                             OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        super(false);
        initData(data);
        this.rowPerPage = rowPerPage <= 0 ? DEFAULT_ROW_PER_PAGE : rowPerPage;
        this.onBaseListV2AdapterListener = onBaseListV2AdapterListener;

        setUpBinder();
    }

    private void initData(@Nullable List<T> data) {
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = data;
        }
    }

    private void setUpBinder() {
        setLoadingView(createLoadingDataBinder());
        setEmptyView(createEmptyViewBinder());
        setRetryView(createRetryDataBinder());
    }

    @Override
    public void setLoadingView(LoadingDataBinder loadingView) {
        if (loadingView == null) {
            loadingView = new LoadingDataBinder(this);
        }
        this.loadingDataBinder = loadingView;
        super.setLoadingView(loadingDataBinder);
    }

    @Override
    public void setEmptyView(NoResultDataBinder emptyView) {
        if (emptyView == null) {
            emptyView = new NoResultDataBinder(this);
        }
        super.setEmptyView(emptyView);
    }

    @Override
    public void setRetryView(RetryDataBinder retryView) {
        if (retryView == null) {
            retryView = new RetryDataBinder(this);
        }
        retryView.setIsFullScreen(true);
        retryView.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryClicked() {
                showLoading(true);
                loadStartPage();
            }
        });
        super.setRetryView(retryView);
    }

    @Nullable
    protected abstract LoadingDataBinder createLoadingDataBinder();

    @Nullable
    protected abstract NoResultDataBinder createEmptyViewBinder();

    @Nullable
    protected abstract NoResultDataBinder createEmptyViewSearchBinder();

    @Nullable
    protected abstract RetryDataBinder createRetryDataBinder();

    public void setSearchQueryString(String searchQueryString) {
        this.searchQueryString = searchQueryString;
    }

    public void addData(List<T> data) {
        addData(data, getDataSize() + (data == null ? 0 : data.size()));
    }

    public void addData(List<T> data, int totalItem) {
        this.totalItem = totalItem;
        if (data != null && data.size() > 0) {
            int prevSize = this.data.size();
            int addedSize = data.size();
            this.data.addAll(data);
            if (prevSize == 0) {
                this.notifyDataSetChanged();
            } else {
                this.notifyItemRangeInserted(prevSize, addedSize);
            }
        }
    }

    public void clearData() {
        this.data.clear();
        this.totalItem = 0;
    }

    public List<T> getData() {
        return data;
    }

    public boolean hasNextPage() {
        return getDataSize() < totalItem && totalItem != 0;
    }

    public void loadNextPage() {
        if (onBaseListV2AdapterListener != null) {
            int currentSize = getDataSize();
            pageToLoad = (currentSize / rowPerPage) + 1;
            onBaseListV2AdapterListener.loadData(pageToLoad,
                    getDataSize(), rowPerPage);
        }
    }

    public void loadStartPage() {
        if (onBaseListV2AdapterListener != null) {
            pageToLoad = 1;
            onBaseListV2AdapterListener.loadData(pageToLoad, 0, rowPerPage);
        }
    }

    public boolean isLoadInitialPage() {
        return pageToLoad == 1;
    }

    @Override
    public void showLoading(boolean isLoading) {
        if (isLoading() == isLoading) {
            return;
        }
        if (isLoading) {
            loading = 1;
            retry = 0;
            empty = 0;
        } else {
            loading = 0;
        }
        if (getDataSize() > 0) {
            loadingDataBinder.setIsFullScreen(false);
            if (isLoading()) {
                notifyItemInserted(getDataSize());
            } else {
                notifyItemRemoved(getDataSize());
            }
        } else {
            loadingDataBinder.setIsFullScreen(true);
            notifyDataSetChanged();
        }
    }

    @Override
    public void showLoadingFull(boolean isLoading) {
        // will redirect to showLoading
        showLoading(isLoading);
    }

    @Override
    public void showRetry(boolean isRetry) {
        if (isRetry() == isRetry) {
            return;
        }
        if (isRetry) {
            loading = 0;
            retry = 1;
            empty = 0;
        } else {
            retry = 0;
        }
        notifyDataSetChanged();
    }

    @Override
    public void showRetryFull(boolean isRetry) {
        //always fullscreen
        showRetry(isRetry);
    }

    @Override
    public void showEmpty(boolean isEmpty) {
        if (isEmpty() == isEmpty) {
            return;
        }
        if (isEmpty) {
            loading = 0;
            retry = 0;
            empty = 1;
        } else {
            empty = 0;
        }
        notifyDataSetChanged();
    }

    @Override
    public void showEmptyFull(boolean isEmpty) {
        //always fullscreen
        showEmpty(isEmpty);
    }

    public int getDataSize() {
        return data.size();
    }

    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_LOADING:
            case VIEW_RETRY:
            case VIEW_EMPTY:
                return super.onCreateViewHolder(parent, viewType);
            case VIEW_EMPTY_SEARCH:
                return getEmptyDataWhenSearchBinder().newViewHolder(parent);
            default:
                return onCreateItemViewHolder(parent, viewType);
        }
    }

    protected View getLayoutView(ViewGroup parent, @LayoutRes int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public abstract BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_LOADING:
            case VIEW_RETRY:
            case VIEW_EMPTY:
                super.onBindViewHolder(holder, position);
                break;
            case VIEW_EMPTY_SEARCH:
                getEmptyDataWhenSearchBinder().bindViewHolder((NoResultDataBinder.ViewHolder) holder, position);
                break;
            default:
                bindItemViewHolder(position, holder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return getDataSize() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || position >= data.size()) {
            if (isLoading()) {
                return VIEW_LOADING;
            } else if (isRetry()) {
                return VIEW_RETRY;
            } else {
                if (TextUtils.isEmpty(searchQueryString)) {
                    return VIEW_EMPTY;
                } else {
                    return VIEW_EMPTY_SEARCH;
                }
            }
        } else { // data is not empty
            return data.get(position).getType();
        }
    }

    private void bindItemViewHolder(final int position, RecyclerView.ViewHolder viewHolder) {
        if (data.size() <= position) {
            return;
        }
        bindItemData(position, viewHolder);
    }

    protected void bindItemData(final int position, RecyclerView.ViewHolder viewHolder) {
        final T t = data.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBaseListV2AdapterListener != null) {
                    onBaseListV2AdapterListener.onItemClicked(t);
                }
            }
        });
        if (viewHolder instanceof BaseViewHolder) {
            ((BaseViewHolder<T>) viewHolder).bindObject(t);
        }
    }

    private NoResultDataBinder getEmptyDataWhenSearchBinder() {
        if (emptyNoResultDataBinder == null) {
            emptyNoResultDataBinder = createEmptyViewSearchBinder();
        }
        if (emptyNoResultDataBinder == null) {
            emptyNoResultDataBinder = new NoResultDataBinder(this);
        }
        return emptyNoResultDataBinder;
    }


}