package com.tokopedia.tkpd.home.favorite.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RetryHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.adapter.FavoriteRecyclerViewAdapter;
import com.tokopedia.tkpd.home.favorite.presenter.Favorite;
import com.tokopedia.tkpd.home.favorite.presenter.FavoriteImpl;
import com.tokopedia.tkpd.home.util.DefaultRetryListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 30/10/2015.
 */
public class FragmentIndexFavoriteV2 extends TkpdBaseV4Fragment implements FavoriteView, DefaultRetryListener.OnClickRetry {
    Favorite favorite;
    BaseRecyclerViewAdapter adapter;// FavoriteRecyclerViewAdapter
    @BindView(R.id.index_favorite_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;
    @BindView(R.id.include_loading)
    ProgressBar progressBar;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    RecyclerView.LayoutManager layoutManager;
    DefaultItemAnimator animator;
    RetryHandler retryHandlerFull;

    public static final String WISHLISH_EXTRA_KEY = "Wishlist";
    private Unbinder unbinder;

    @Override
    public void initHolder() {

    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_FAVORITE_SHOP;
    }

    @Override
    public void initAdapter(List<RecyclerViewItem> data) {
        adapter = new FavoriteRecyclerViewAdapter(getActivity(), data);
        // passing presenter for adapter
        ((FavoriteRecyclerViewAdapter) adapter).setFavorite(favorite);
    }

    @Override
    public void initLayoutManager() {
        layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void prepareView() {
        animator = new DefaultItemAnimator();
        animator.setAddDuration(DURATION_ANIMATOR);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(animator);
        setAdapter();
    }

    @Override
    public void setAdapter() {
        recyclerView.setAdapter(adapter);
        setOnRetryListenerRV();
    }

    @Override
    public void setPullEnabled(boolean isPullEnabled) {
        Log.d(TAG, FragmentIndexFavoriteV2.class.getSimpleName() + (isPullEnabled ? " hidupkan" : " matikan") + " pull to refresh");
        swipeToRefresh.setEnabled(isPullEnabled);
    }

    @Override
    public void displayPull(boolean isShow) {
        Log.d(TAG, FragmentIndexFavoriteV2.class.getSimpleName() + (isShow ? " tampilkan" : " hilangkan ") + " pull to refresh");
        swipeToRefresh.setRefreshing(isShow);
    }

    @Override
    public boolean isSwipeShow() {
        if (swipeToRefresh != null)
            return swipeToRefresh.isRefreshing();
        else
            return false;
    }

    @Override
    public void loadDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        Log.d(TAG, FragmentIndexFavoriteV2.class.getSimpleName() + (isLoadMore ? " tampilkan" : " hilangkan ") + " load more");
        adapter.setIsLoading(isLoadMore);
    }

    @Override
    public boolean isLoadMoreShow() {
        GridLayoutManager temp = (GridLayoutManager) layoutManager;
        return adapter.getItemViewType(temp.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public void moveToOtherActivity(Bundle bundle, Class<?> clazz) {
        if (bundle == null)
            throw new RuntimeException("need to passed not null bundle !!!");
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void displayRetry(boolean isRetry) {
        Log.d(TAG, FragmentIndexFavoriteV2.class.getSimpleName() + (isRetry ? " tampilkan" : " hilangkan ") + " isRetry");
        adapter.setIsRetry(isRetry);
    }

    @Override
    public void displayTimeout() {
        CommonUtils.UniversalToast(getActivity(), getActivity().getString(R.string.msg_connection_timeout_toast));
    }

    @Override
    public void setOnRetryListenerRV() {
        adapter.setOnRetryListenerRV(new BaseRecyclerViewAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                new DefaultRetryListener(DefaultRetryListener.RETRY_FOOTER, FragmentIndexFavoriteV2.this).onRetryCliked();
            }
        });
    }

    @Override
    public boolean isLandscape() {
        return getScreenRotation() == LANDSCAPE;
    }

    @Override
    public int getScreenRotation() {
        return getActivity().getResources().getConfiguration().orientation;
    }

    @Override
    public void onRetryFull() {
        displayProgressBar(true);
        displayMainContent(false);
        hideRetryFull();
        favorite.resetToPageOne();
    }

    @Override
    public void onRetryFooter() {
        favorite.fetchListData(Favorite.FAVORITE_DATA_TYPE);
    }

    public static class ViewHolder {
        RecyclerView recyclerView;
        SwipeToRefresh swipeToRefresh;
        ProgressBar progressBar;
        RelativeLayout mainContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favorite = new FavoriteImpl(this);
//        favorite.onFetchDataAfterRotate(savedInstanceState);
        favorite.initInstances(getActivity());

        initIntentExtra();
        if (getActivity() instanceof ParentIndexHome) {
            if (((ParentIndexHome) getActivity()).getViewPager() != null) {
                if (!isDataExist() && ((ParentIndexHome)getActivity()).getViewPager().getCurrentItem() == 2) {
                    favorite.initData();
                    Log.d("NISNISNIS", "IMPRESSION ON CREATE");
                }
            }
        }

    }

    private void initIntentExtra() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        boolean isFromWishList = bundle.getBoolean(WISHLISH_EXTRA_KEY, false);
        if (isFromWishList) {
            favorite.initData();
            Log.d("NISNISNIS", "IMPRESSION FAV ON WISHLIST");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_index_favorite_v2, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        prepareView();
        displayProgressBar(true);
        displayMainContent(false);
        retryHandlerFull = new RetryHandler(getActivity(), parentView);
        retryHandlerFull.setRetryView();
        retryHandlerFull.setOnRetryListener(new DefaultRetryListener(DefaultRetryListener.RETRY_FULL, this));
        setListener();
        return parentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        favorite.onSaveDataBeforeRotate(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, FragmentIndexFavoriteV2.class.getSimpleName() + " screen Rotation " + (isLandscape() ? "LANDSCAPE" : "PORTRAIT"));
        favorite.subscribe();
//        if (favorite.isAfterRotate()) {
//            displayMainContent(true);
//            displayProgressBar(false);
//            loadDataChange();
//
//            if (!favorite.isMorePage())
//                displayLoadMore(false);
//            else
//                displayLoadMore(true);
//        }else{
//            favorite.initData();
//        }

    }

    @Override
    public void onPause() {
        super.onPause();
        favorite.unSubscribe();
    }

    @Override
    public int getLastPosition() {
        return ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void displayRetryFull() {
        retryHandlerFull.enableRetryView();
    }

    @Override
    public void hideRetryFull() {
        retryHandlerFull.disableRetryView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            if (isVisibleToUser && isAdded() && getActivity() != null) {
                favorite.setLocalyticFlow(getActivity());
                favorite.sendAppsFlyerData(getActivity());
                ScreenTracking.screen(getScreenName());
                if (!isDataExist()) {
                    favorite.initData();
                    Log.d("NISNISNIS", "IMPRESSION USER VISIBLE HINT");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            onCreate(new Bundle());
        }

        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void scrollTo(int index) {
        layoutManager.scrollToPosition(index);
    }

    protected void setListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                if (isLoadMoreShow() && itemPosition == layoutManager.getItemCount() - 1) {// !isSwipeShow() && RetrofitUtils.isConnected(getActivity()) &&
                    favorite.loadMore();
                }
//                else{
//                    displayLoadMore(false);
//                    displayRetry(true);
//                    loadDataChange();
//                }
            }
        });
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setIsLoading(false);// disable load more if swipe to refresh proceed
                favorite.resetToPageOne();
            }
        });
        favorite.setRetryListener();
    }

    @Override
    public void displayMainContent(boolean isDisplay) {
        Log.d(TAG, FragmentIndexFavoriteV2.class.getSimpleName() + " main content ingin " + (isDisplay ? "dihidupkan" : "dimatikan"));
        if (isDisplay)
            mainContent.setVisibility(View.VISIBLE);
        else
            mainContent.setVisibility(View.GONE);
    }

    @Override
    public void displayProgressBar(boolean isDisplay) {
        Log.d(TAG, FragmentIndexFavoriteV2.class.getSimpleName() + " progress bar ingin " + (isDisplay ? "dihidupkan" : "dimatikan"));
        if (isDisplay)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    private Boolean isDataExist() {
        return adapter.getData().size() != 0;
    }
}
