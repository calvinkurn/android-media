package com.tokopedia.library.pagination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginationAdapter<T extends PaginationItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int ERROR_ITEM_COUNT = -1;
    public static final String TAG = "PaginationAdapter";

    /*For detecting if loader view added or not*/
    private boolean mIsLoadingAdded;

    /*For detecting if loading for current is already ongoing*/
    private boolean mIsLoading;

    /*To know for last page in pagination*/
    private boolean mIsLastPage;

    //library always start from second page as first page will be automatically loaded.
    private int mCurrentPageIndex = 1;

    /*For handling any during loading next page*/
    private boolean mIsError = false;

    /*Callback method for library consumer*/
    private PaginationAdapterCallback mCallback;

    private List<T> mItems;

    protected PaginationAdapter(PaginationAdapterCallback callback) {
        this.mItems = new ArrayList<>();
        this.mCallback = callback;
    }

    /**
     * Return list for current adapter
     *
     * @return
     */
    public List<T> getItems() {
        return mItems;
    }

    /**
     * Must have
     * To get itemviewholder this method must be implement by consumer
     *
     * @param parent
     * @param inflater
     * @return
     */
    protected abstract RecyclerView.ViewHolder getItemViewHolder(ViewGroup parent, LayoutInflater inflater);

    /**
     * Must have
     * Instead of using <code>onBindViewHolder()</code>, Consumer should use this method to set data on item view
     * <p>
     * See <code>R.layout.pg_progress.xml<code/>
     *
     * @param holder
     * @param item
     * @param position
     */
    protected abstract void bindView(@NonNull RecyclerView.ViewHolder holder, T item, int position);

    /**
     * To provide error view(If error occurred during load). by default library will provide green "coba lagi" button with one error info text.
     * user can also customize this method as per need.
     * <p>
     * See <code>R.layout.pg_retry.xml<code/>
     *
     * @param context
     * @return View
     */
    protected View getRetryView(Context context) {
        return View.inflate(context, R.layout.pg_retry, null);
    }

    /**
     * To provide loader view. by default library will provide simple circular progressbar during next page load.
     * user can also customize this method as per need.
     *
     * @param context
     * @return View
     */
    protected View getLoaderView(Context context) {
        return View.inflate(context, R.layout.pg_progress, null);
    }

    /**
     * Utility method to getting object for footer row
     *
     * @return
     */
    protected T getFooterObject() {
        return (T) new PaginationItem();
    }

    protected String getRetryButtonLabel(Context context) {
        return context.getString(R.string.pg_label_retry);
    }

    public T getItem(int position) {
        return getItems().get(position);
    }

    public void remove(T t) {
        int position = getItems().indexOf(t);
        if (position > -1) {
            getItems().remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mIsLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void add(T t) {
        getItems().add(t);
        notifyItemInserted(getItems().size() - 1);
    }

    public void addAll(List<T> items) {
        for (T t : items) {
            add(t);
        }
    }

    /**
     * To get VH for footer
     *
     * @param parent
     * @param inflater
     * @return
     */
    private RecyclerView.ViewHolder getLoaderViewHolder(ViewGroup parent, LayoutInflater inflater) {
        return new LoadingVH(inflater.inflate(R.layout.pg_loader_container, parent, false));
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return (position == getItems().size() - 1 && mIsLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return getItems() == null ? 0 : getItems().size();
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     * This one the magic to detect scroll and invoke <code>loadMore(int)<code/>
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        startDataLoading();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (!isLoading() && !isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition > 0) {
                        startDataLoading();
                    }
                }
            }
        });
    }

    /**
     * Method to invoke page loading
     */
    public void startDataLoading() {
        if (mCallback != null) {
            if (mCurrentPageIndex == 1) {
                mCallback.onStartFirstPageLoad();
            } else {
                mCallback.onStartPageLoad(mCurrentPageIndex);
            }
        }

        loadMore(mCurrentPageIndex);
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Nullable
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LOADING:
                viewHolder = getLoaderViewHolder(parent, inflater);
                break;
            case ITEM:

            default:
                viewHolder = getItemViewHolder(parent, inflater);
                break;
        }

        return viewHolder;
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final T item = getItems().get(position);

        switch (getItemViewType(position)) {
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (mIsError) {
                    loadingVH.container.removeAllViews();
                    loadingVH.container.addView(getRetryView(loadingVH.container.getContext()));
                    if (item.getRetryMessage() == null || item.getRetryMessage().isEmpty()) {
                        ((TextView) loadingVH.container.findViewById(R.id.text_error_info)).setText(R.string.pg_error_info);
                    } else {
                        ((TextView) loadingVH.container.findViewById(R.id.text_error_info)).setText(item.getRetryMessage());
                    }

                    if (getRetryButtonLabel(loadingVH.container.getContext()).isEmpty()) {
                        ((TextView) loadingVH.container.findViewById(R.id.text_btn_retry)).setText(R.string.pg_label_retry);
                    } else {
                        ((TextView) loadingVH.container.findViewById(R.id.text_btn_retry)).setText(getRetryButtonLabel(loadingVH.container.getContext()));
                    }
                } else {
                    loadingVH.container.removeAllViews();
                    loadingVH.container.addView(getLoaderView(loadingVH.container.getContext()));
                }
                break;

            case ITEM:
            default:
                bindView(holder, item, position);
                break;
        }
    }

    protected void addLoadingFooter() {
        mIsLoadingAdded = true;
        add(getFooterObject());
    }

    protected void removeLoadingFooter() {
        if (!mIsLoadingAdded) {
            return;
        }

        mIsLoadingAdded = false;

        int position = getItems().size() - 1;
        T item = getItem(position);

        if (item != null) {
            getItems().remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * <b>Implementation Require<b/>
     * <p>
     * Provide logic for getting data from source either network on local db
     *
     * @param currentPageIndex
     */
    protected void loadMore(int currentPageIndex) {
        setLoading(true);
        Log.d(TAG, "loadMore() page+" + currentPageIndex);
    }

    /**
     * <b>Must invoked by consumer with response items to that library can add them over ui<b/>
     *
     * @param moreItems
     */
    protected void loadCompleted(@NonNull List<T> moreItems, Object rawObject) {
        if (mCallback != null) {
            if (mCurrentPageIndex == 1 && moreItems.isEmpty()) {
                mCallback.onFinishFirstPageLoad(moreItems == null ? ERROR_ITEM_COUNT : moreItems.size(), rawObject);
                mCallback.onEmptyList(rawObject);
            } else if (mCurrentPageIndex == 1) {
                mCallback.onFinishFirstPageLoad(moreItems == null ? ERROR_ITEM_COUNT : moreItems.size(), rawObject);
            } else {
                mCallback.onFinishPageLoad(moreItems == null ? ERROR_ITEM_COUNT : moreItems.size(), mCurrentPageIndex, rawObject);
            }
        }

        mCurrentPageIndex++;
        mIsError = false;
        setLoading(false);
        removeLoadingFooter();
        addAll(moreItems);
    }

    /**
     * Must invoked by consumer with error message to show retry node
     */
    protected void loadCompletedWithError() {
        if (mCallback != null) {
            mCallback.onError(mCurrentPageIndex);
        }

        mIsError = true;
        setLoading(false);
        notifyItemChanged(getItems().size() - 1);
    }

    /**
     * Must invoked by consumer with error message to show retry node
     */
    protected void loadCompletedWithError(String errorMessage) {
        getItem(getItems().size() - 1).setRetryMessage(errorMessage);
        loadCompletedWithError();
    }

    public void setLastPage(boolean isLastPage) {
        this.mIsLastPage = isLastPage;

        if (isLastPage) {
            removeLoadingFooter();
        } else {
            addLoadingFooter();
        }
    }

    public int getCurrentPageIndex() {
        return mCurrentPageIndex;
    }

    protected boolean isLoading() {
        return this.mIsLoading;
    }

    protected void setLoading(boolean isLoading) {
        this.mIsLoading = isLoading;
    }

    protected boolean isLastPage() {
        return this.mIsLastPage;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout container;

        public LoadingVH(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.pagination_loader_container);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            loadMore(mCurrentPageIndex);
            if (mCallback != null) {
                mCallback.onRetryPageLoad(mCurrentPageIndex);
            }
        }
    }
}
