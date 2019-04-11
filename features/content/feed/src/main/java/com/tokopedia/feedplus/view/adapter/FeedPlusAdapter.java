package com.tokopedia.feedplus.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.feedplus.view.util.EndlessScrollRecycleListener;
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;
import com.tokopedia.feedplus.view.viewmodel.product.AddFeedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final FeedPlusTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private EmptyFeedBeforeLoginModel emptyFeedBeforeLoginModel;
    private LoadingMoreModel loadingMoreModel;
    private RetryModel retryModel;
    private boolean unsetListener;
    private AddFeedModel addFeedModel;
    private OnLoadListener loadListener;
    private RecyclerView recyclerView;
    private int itemTreshold = 5;

    private EndlessScrollRecycleListener endlessScrollListener = new EndlessScrollRecycleListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            if (isLoading())
                return;
            if (loadListener != null && !unsetListener && list.size() > itemTreshold) {
                showLoading();
                loadListener.onLoad(totalItemsCount);
            }
        }

        @Override
        public void onScroll(int lastVisiblePosition) {
            if (loadListener instanceof OnScrollListener)
                ((OnScrollListener) loadListener).onScroll(lastVisiblePosition);
        }
    };

    public FeedPlusAdapter(FeedPlusTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingMoreModel = new LoadingMoreModel();
        this.retryModel = new RetryModel();
        this.addFeedModel = new AddFeedModel();
        this.emptyFeedBeforeLoginModel = new EmptyFeedBeforeLoginModel();
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(list.get(position), payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void add(Visitable visitable) {
        int position = this.list.size();
        if (this.list.add(visitable)) {
            notifyItemInserted(position);
        }
    }

    private void remove(Visitable visitable) {
        int position = this.list.indexOf(visitable);
        if (this.list.remove(visitable)) {
            notifyItemRemoved(position);
        }
    }

    public void setList(List<Visitable> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<Visitable> list) {
        int positionStart = getItemCount();
        if (this.list.addAll(list)) {
            notifyItemRangeInserted(positionStart, list.size());
        }
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void showEmpty() {
        add(emptyModel);
    }

    public void removeEmpty() {
        remove(emptyModel);
    }

    public void showRetry(){
        add(retryModel);
    }

    public void removeRetry(){
        remove(retryModel);
    }

    public void showLoading() {
        add(loadingMoreModel);
    }

    public void removeLoading() {
        remove(loadingMoreModel);
    }

    public boolean isLoading() {
        return this.list.contains(loadingMoreModel);
    }

    public List<Visitable> getlist() {
        return list;
    }

    public void showAddFeed() {
        add(addFeedModel);
    }

    public void removeAddFeed(){
        remove(addFeedModel);
    }

    public void addItem(Visitable item) {
        add(item);
    }

    public void showUserNotLogin() {
        add(emptyFeedBeforeLoginModel);
    }

    public int getItemTreshold() {
        return itemTreshold;
    }

    public void setItemTreshold(int itemTreshold) {
        this.itemTreshold = itemTreshold;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);
        setEndlessScrollListener();
    }

    @Override
    public void onViewRecycled(@NonNull AbstractViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public void setEndlessScrollListener() {
        unsetListener = false;
        recyclerView.addOnScrollListener(endlessScrollListener);
    }

    public void unsetEndlessScrollListener() {
        unsetListener = true;
        recyclerView.removeOnScrollListener(endlessScrollListener);
    }

    public interface OnLoadListener {
        void onLoad(int totalCount);

    }

    public interface OnScrollListener extends OnLoadListener {
        void onScroll(int lastVisiblePosition);

    }
}
