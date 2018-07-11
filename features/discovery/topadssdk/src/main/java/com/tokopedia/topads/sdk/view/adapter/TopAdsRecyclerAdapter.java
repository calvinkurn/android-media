package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.ObserverType;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.utils.EndlessScrollRecycleListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewholder.LoadingViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.LoadingViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

/**
 * @author by errysuprayogi on 4/11/17.
 */

public class TopAdsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = TopAdsRecyclerAdapter.class.getSimpleName();

    @NonNull
    private final RecyclerView.AdapterDataObserver mAdapterDataObserver;
    @Nullable
    private RecyclerView recyclerView;
    @NonNull
    private final RecyclerView.Adapter mOriginalAdapter;
    private final Context mContext;
    private TopAdsAdapterTypeFactory typeFactory;
    private TopAdsInfoClickListener adsInfoClickListener;
    private OnLoadListener loadListener;
    private boolean loadMore = false;
    private boolean unsetListener = false;
    private GridLayoutManager.SpanSizeLookup spanSizeLookup;
    private LoadingViewModel loadingViewModel = new LoadingViewModel();
    private TopAdsPlacer placer;
    private int itemTreshold = 5;

    private EndlessScrollRecycleListener endlessScrollListener = new EndlessScrollRecycleListener() {

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            if (loadMore)
                return;
            if (loadListener != null && !unsetListener && placer.getItemList().size() > itemTreshold) {
                placer.increasePage();
                showLoading();
                loadListener.onLoad(placer.getPage(), totalItemsCount);
            }
        }

        @Override
        public void onScroll(int lastVisiblePosition) {
            if (loadListener instanceof OnScrollListener)
                ((OnScrollListener) loadListener).onScroll(lastVisiblePosition);
        }
    };

    private TopAdsPlacer.DataObserver dataObserver = new TopAdsPlacer.DataObserver() {
        @Override
        public void onStreamLoaded(int type) {
            switch (type) {
                case ObserverType.CHANGE:
                    notifyDataSetChanged();
                    break;
                case ObserverType.ITEM_RANGE_INSERTED:
                    notifyItemRangeInserted(
                            placer.getAjustedPositionStart(), placer.getAjustedItemCount());
                    break;
            }
        }
    };

    public TopAdsRecyclerAdapter(
            @NonNull Context context, @NonNull final RecyclerView.Adapter originalAdapter) {
        mOriginalAdapter = originalAdapter;
        mContext = context;
        typeFactory = new TopAdsAdapterTypeFactory();
        placer = new TopAdsPlacer(this, context, typeFactory, dataObserver);
        mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                hideLoading();
                placer.setAjustedItemCount(mOriginalAdapter.getItemCount());
                placer.onChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                positionStart = placer.getPositionStart(positionStart);
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(final int positionStart, final int itemCount) {
                hideLoading();
                placer.onItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                positionStart = placer.getPositionStart(positionStart);
                for (int i = positionStart; i < (positionStart + itemCount); i++) {
                    placer.getItemList().remove(i);
                }
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyDataSetChanged();
            }
        };
        this.mOriginalAdapter.registerAdapterDataObserver(mAdapterDataObserver);

    }

    public void setConfig(Config config) {
        placer.setConfig(config);
    }

    public Config getConfig() {
        return placer.getConfig();
    }

    public void setHasHeader(boolean hasHeader, int headerCount) {
        placer.setHasHeader(hasHeader, headerCount);
    }


    public void setHasHeader(boolean hasHeader) {
        setHasHeader(hasHeader, 1);
    }

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup;
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public void setAdsInfoClickListener(TopAdsInfoClickListener adsInfoClickListener) {
        this.adsInfoClickListener = adsInfoClickListener;
    }

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        placer.setAdsItemClickListener(adsItemClickListener);
    }

    public void setTopAdsListener(TopAdsListener topAdsListener) {
        placer.setTopAdsListener(topAdsListener);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);
        placer.attachRecycleView(this.recyclerView);
        setLayoutManager(this.recyclerView.getLayoutManager());
        setEndlessScrollListener();
    }

    public void setEndlessScrollListenerVisibleThreshold(int threshold) {
        this.endlessScrollListener.setVisibleThreshold(threshold);
        this.itemTreshold = threshold;
    }

    public void unsetEndlessScrollListener() {
        unsetListener = true;
        recyclerView.removeOnScrollListener(endlessScrollListener);
    }

    public void setEndlessScrollListener() {
        unsetListener = false;
        recyclerView.addOnScrollListener(endlessScrollListener);
    }

    public void clearAds() {
        placer.clearAds();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        GridLayoutManager gridLayoutManager;

        public SpanSizeLookup(GridLayoutManager gridLayoutManager) {
            this.gridLayoutManager = gridLayoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            if (placer.getItem(position) instanceof TopAdsViewModel) {
                return gridLayoutManager.getSpanCount();
            } else {
                return 1;
            }
        }
    }

    public boolean isTopAdsViewHolder(int position) {
        if (placer.getItemCount() > 0)
            return placer.getItem(position) instanceof TopAdsViewModel;
        return false;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TopAdsViewHolder.LAYOUT || viewType == LoadingViewHolder.LAYOUT) {
            View view = LayoutInflater.from(context).inflate(viewType, parent, false);
            return typeFactory.createViewHolder((ViewGroup) view, viewType);
        } else {
            return mOriginalAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int originalPos = getOriginalPosition(position);
        if (originalPos == TopAdsViewModel.TOP_ADS_POSITION_TYPE) {
            TopAdsViewHolder topAdsViewHolder = (TopAdsViewHolder) holder;
            TopAdsViewModel adsViewModel = (TopAdsViewModel) placer.getItem(position);
            topAdsViewHolder.setDisplayMode(placer.getDisplayMode());
            topAdsViewHolder.bind(adsViewModel);
            if (adsInfoClickListener != null) {
                topAdsViewHolder.setClickListener(adsInfoClickListener);
            }
        } else if (originalPos == LoadingViewModel.LOADING_POSITION_TYPE) {
            return;
        } else {
            mOriginalAdapter.onBindViewHolder(holder, originalPos);
        }
    }

    public int getOriginalPosition(int position) {
        return placer.getItem(position).originalPos();
    }

    @Override
    public int getItemViewType(final int position) {
        int viewType = placer.getItem(position).type(typeFactory);
        try {
            if (viewType == TopAdsAdapterTypeFactory.CLIENT_ADAPTER_VIEW_TYPE) {
                viewType = mOriginalAdapter.getItemViewType(getOriginalPosition(position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return viewType;
        }
    }

    @Override
    public int getItemCount() {
        return placer.getItemCount();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            if (getConfig().getDisplayMode() == DisplayMode.FEED) {
                placer.setDisplayMode(getConfig().getDisplayMode());
            } else {
                placer.setDisplayMode(DisplayMode.GRID);
            }
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            if (spanSizeLookup != null) {
                gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
            } else {
                gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup(gridLayoutManager));
            }
            this.recyclerView.setLayoutManager(gridLayoutManager);
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (getConfig().getDisplayMode() == DisplayMode.FEED) {
                placer.setDisplayMode(getConfig().getDisplayMode());
            } else {
                placer.setDisplayMode(DisplayMode.LIST);
            }
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            this.recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    public boolean isLoading(int position) {
        return position == placer.getItemList().indexOf(loadingViewModel);
    }

    public boolean isLoading() {
        return placer.getItemList().contains(loadingViewModel);
    }

    public void reset() {
        shouldLoadAds(true);
        loadMore = false;
        placer.reset();
        if (this.recyclerView != null) this.recyclerView.removeAllViews();
        endlessScrollListener.resetState();
        clearAds();
        notifyDataSetChanged();
    }

    public void showLoading() {
        if (!placer.getItemList().contains(loadingViewModel)) {
            placer.getItemList().add(loadingViewModel);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(placer.getItemCount() + 1);
                }
            });
        }
        loadMore = true;
    }

    public void hideLoading() {
        if (placer.getItemList().contains(loadingViewModel)) {
            placer.getItemList().remove(loadingViewModel);
            notifyItemRemoved(placer.getItemCount());
        }
        loadMore = false;
    }

    public void shouldLoadAds(boolean loadAds) {
        placer.setShouldLoadAds(loadAds);
    }

    public interface OnLoadListener {

        void onLoad(int page, int totalCount);

    }

    public interface OnScrollListener extends OnLoadListener {

        void onScroll(int lastVisiblePosition);

    }

    public TopAdsPlacer getPlacer() {
        return placer;
    }

}
