package com.tokopedia.library.baseadapter;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.library.pagination.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T extends BaseItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int LOADING = -94567;
    private static final int ERROR_ITEM_COUNT = -1;

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
    private AdapterCallback mCallback;

    private View mRetryView, mLoaderView;

    private List<T> mItems;
    private final List<T> mOrigItems;

    protected BaseAdapter(AdapterCallback callback) {
        if (callback == null) {
            throw new RuntimeException("AdapterCallback object cannot be null");
        }

        this.mItems = new ArrayList<>();
        this.mOrigItems = new ArrayList<>();
        this.mCallback = callback;
    }

    /**
     * Return list for current adapter
     *
     * @return List items
     */
    public final List<T> getItems() {
        return mItems;
    }

    /**
     * Return list for current adapter
     *
     * @return List items
     */
    public final List<T> getOrigItems() {
        return mOrigItems;
    }

    /**
     * reset list for current adapter to 0
     *
     * @return List items
     */
    public final void resetOriginalList() {
         mOrigItems.clear();
         mItems.clear();
    }

    /**
     * Must have
     * To get itemviewholder this method must be implement by consumer
     *
     * @param parent   Parent view
     * @param inflater inflater object
     * @return VH
     */
    protected abstract BaseVH getItemViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType);

    /**
     * To provide error view(If error occurred during load). by default library will provide green "coba lagi" button with one error info text.
     * user can also customize this method as per need.
     * <p>
     * See <code>R.layout.pg_retry.xml<code/>
     *
     * @param context
     * @return View retry view
     */
    protected View getRetryView(Context context) {
        return Utils.getRetryView(context);
    }

    /**
     * To provide loader view. by default library will provide simple circular progressbar during next page load.
     * user can also customize this method as per need.
     *
     * @param context
     * @return View Loader view
     */
    protected View getLoaderView(Context context) {
        return Utils.getLoaderView(context);
    }

    /**
     * Utility method to getting object for footer row
     *
     * @return Footer object T
     */
    final T getFooterObject() {
        return (T) new BaseItem();
    }

    public String getRetryButtonLabel(Context context) {
        return context.getString(R.string.pg_label_retry);
    }

    public final T getItem(int position) {
        return getItems().get(position);
    }

    public final void remove(T t) {
        int position = getItems().indexOf(t);
        if (position > -1) {
            getItems().remove(position);
            notifyItemRemoved(position);
        }
    }

    public final void add(T t) {
        getItems().add(t);
        notifyItemInserted(getItems().size() - 1);
    }

    public final void addAll(List<T> items) {
        for (T t : items) {
            add(t);
        }
    }

    @Override
    public final int getItemCount() {
        return getItems() == null ? 0 : getItems().size();
    }

    public final boolean isEmpty() {
        return getItemCount() == 0;
    }

    public final void clear() {
        mIsLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    /**
     * To get VH for footer
     *
     * @param parent   Parent View
     * @param inflater inflater
     * @return ViewHolder
     */
    private BaseVH getLoaderViewHolder(ViewGroup parent, LayoutInflater inflater) {
        return new LoadingVH(inflater.inflate(R.layout.pg_loader_container, parent, false));
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     *
     * @param position adapter position
     * @return item view type
     */
    @Override
    public final int getItemViewType(int position) {
        return (position == getItems().size() - 1 && mIsLoadingAdded) ? LOADING : getCustomItemViewType(position);
    }

    /**
     * @param position
     * @return View type for creating view holder
     */
    public int getCustomItemViewType(int position) {
        return 0;
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     * This one the magic to detect scroll and invoke <code>loadData(int)<code/>
     *
     * @param recyclerView RecyclerView instance
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
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
    public final void startDataLoading() {
        if (mCallback != null) {
            if (mCurrentPageIndex == 1) {
                mCallback.onStartFirstPageLoad();
            } else {
                mCallback.onStartPageLoad(mCurrentPageIndex);
            }
        }

        loadData(mCurrentPageIndex);
    }

    /**
     * Method to invoke page loading
     */
    public final void startDataLoading(String ... args) {
        if (mCallback != null) {
            if (mCurrentPageIndex == 1) {
                mCallback.onStartFirstPageLoad();
            } else {
                mCallback.onStartPageLoad(mCurrentPageIndex);
            }
        }

        loadData(mCurrentPageIndex, args);
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     *
     * @param parent   Parent view
     * @param viewType View type which needs to be inflate default zero
     * @return BaseVH
     */
    @Nullable
    @Override
    public final BaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseVH viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LOADING:
                viewHolder = getLoaderViewHolder(parent, inflater);
                break;
            default:
                viewHolder = getItemViewHolder(parent, inflater, viewType);
                break;
        }

        return viewHolder;
    }

    /**
     * <b>PLEASE DO NOT OVERRIDE<b/>
     *
     * @param holder   ViewHolder extends BaseVH
     * @param position Adapter position
     */
    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final T item = getItems().get(position);
        ((BaseVH)holder).bindView(item, position);
    }

    protected final void addLoadingFooter() {
        mIsLoadingAdded = true;
        add(getFooterObject());
    }

    protected final void removeLoadingFooter() {
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
    @CallSuper
    public void loadData(int currentPageIndex, String ... args) {
        setLoading(true);
    }

    /**
     * <b>Must invoked by consumer with response items to that library can add them over ui<b/>
     *
     * @param moreItems items needs to be add on list
     */
    protected final void loadCompleted(@NonNull List<T> moreItems, Object rawObject) {
        if (mCallback != null) {
            if (mCurrentPageIndex == 1 && moreItems.isEmpty()) {
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
        mOrigItems.addAll(moreItems);
    }

    /**
     * Must invoked by consumer with error message to show retry node
     */
    protected final void loadCompletedWithError() {
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
    protected final void loadCompletedWithError(String errorMessage) {
        getItem(getItems().size() - 1).setRetryMessage(errorMessage);
        loadCompletedWithError();
    }

    /**
     * This method will be invoked by consumer for indicating last page so that library can stop loading next page
     *
     * @param isLastPage to detect for last page
     */
    public final void setLastPage(boolean isLastPage) {
        this.mIsLastPage = isLastPage;

        if (isLastPage) {
            removeLoadingFooter();
        } else {
            addLoadingFooter();
        }
    }

    /**
     * For resetting the adapter state. (This can be useful like swipe-to-refresh UI)
     */
    public final void resetAdapter() {
        clear();
        mCurrentPageIndex = 1;
        mIsError = false;
        setLoading(false);
        setLastPage(false);
    }

    public final int getCurrentPageIndex() {
        return mCurrentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        mCurrentPageIndex = currentPageIndex;
    }

    protected final boolean isLoading() {
        return this.mIsLoading;
    }

    protected final void setLoading(boolean isLoading) {
        this.mIsLoading = isLoading;
    }

    protected final boolean isLastPage() {
        return this.mIsLastPage;
    }

    protected class LoadingVH extends BaseVH implements View.OnClickListener {
        private LinearLayout container;

        public LoadingVH(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.pagination_loader_container);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            loadData(mCurrentPageIndex);
            if (mCallback != null) {
                mCallback.onRetryPageLoad(mCurrentPageIndex);
            }
        }

        @Override
        public void bindView(BaseItem item, int position) {
            if (mIsError) {
                if (mRetryView == null) {
                    mRetryView = getRetryView(container.getContext());
                }

                if (mRetryView.getParent() != null) {
                    ((ViewGroup) mRetryView.getParent()).removeView(mRetryView);
                }

                container.removeAllViews();
                container.addView(mRetryView);
                if (item.getRetryMessage() == null || item.getRetryMessage().isEmpty()) {
                    ((TextView) container.findViewById(R.id.text_error_info)).setText(R.string.pg_error_info);
                } else {
                    ((TextView) container.findViewById(R.id.text_error_info)).setText(item.getRetryMessage());
                }

                if (getRetryButtonLabel(container.getContext()).isEmpty()) {
                    ((TextView) container.findViewById(R.id.text_btn_retry)).setText(R.string.pg_label_retry);
                } else {
                    ((TextView) container.findViewById(R.id.text_btn_retry)).setText(getRetryButtonLabel(container.getContext()));
                }
            } else {
                if (mLoaderView == null) {
                    mLoaderView = getLoaderView(container.getContext());
                }

                if (mLoaderView.getParent() != null) {
                    ((ViewGroup) mLoaderView.getParent()).removeView(mLoaderView);
                }

                container.removeAllViews();
                container.addView(mLoaderView);
            }
        }
    }


    /**
     * Base ViewHolder class which will be extend by consumer class ViewHolder
     */
    public abstract class BaseVH extends RecyclerView.ViewHolder {

        public BaseVH(View itemView) {
            super(itemView);
        }

        /**
         * @param item     T
         * @param position position of adapter
         */
        public abstract void bindView(T item, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mItems = (List<T>) results.values;
                notifyDataSetChanged();

                if(!isLastPage() && constraint.toString().isEmpty()) {
                    mIsLoadingAdded = true;
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                mIsLoadingAdded = false;
                List<T> filteredResults = null;

                if (constraint.length() == 0) {
                    filteredResults = new ArrayList<>(getOrigItems());
                } else {
                    filteredResults = getFilteredResults(constraint.toString());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    public List<T> getFilteredResults(String constraint) {
        List<T> results = new ArrayList<T>();

        for (T item : getOrigItems()) {
            if (containsIgnoreCase(item.getSearchQuery(), constraint)) {
                results.add(item);
            }
        }

        return results;
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0) {
            return true; // Empty string is contained
        }

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp) {
                continue;
            }

            if (src.regionMatches(true, i, what, 0, length)) {
                return true;
            }
        }

        return false;
    }

}
