package com.tokopedia.tkpd.home.feed.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.instoped.InstagramAuth;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.util.RetryHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.DataFeedAdapter;
import com.tokopedia.tkpd.home.feed.di.component.DaggerDataFeedComponent;
import com.tokopedia.tkpd.home.util.DefaultRetryListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentProductFeed extends BaseDaggerFragment implements FeedContract.View,
        DefaultRetryListener.OnClickRetry, ListenerFabClick, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.index_main_recycler_view)
    RecyclerView contentRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeRefreshLayout;
    @BindView(R.id.fab_speed_dial)
    FabSpeedDial fabAddProduct;

    @Inject
    FeedPresenter feedPresenter;

    private GridLayoutManager gridLayoutManager;
    private DataFeedAdapter adapter;
    private Unbinder unbinder;
    private RetryHandler retryHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initVar();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_index_main_v2, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        prepareView(parentView);
        feedPresenter.attachView(this);
        feedPresenter.initializeDataFeed();
        feedPresenter.refreshDataFeed();
        return parentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        String shopID = SessionHandler.getShopID(getActivity());
        String invalidShopId = "0";
        if (shopID == null || invalidShopId.equals(shopID) || shopID.length() == 0) {
            fabAddProduct.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        feedPresenter.detachView();
    }

    @Override
    protected void initInjector() {
        AppComponent component = getComponent(AppComponent.class);
        DaggerDataFeedComponent feedComponent
                = (DaggerDataFeedComponent) DaggerDataFeedComponent
                .builder()
                .appComponent(component).build();
        feedComponent.inject(this);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null) {
            ScreenTracking.screen(getScreenName());
            setLocalyticFlow();
            sendAppsFlyerData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_PRODUCT_FEED;
    }


    @Override
    public void onRetryFull() {
        feedPresenter.refreshDataFeed();
    }

    @Override
    public void onRetryFooter() {
        feedPresenter.loadMoreDataFeed();
    }


    @Override
    public void onFabClick() {
        if (!fabAddProduct.isShown()) {
            fabAddProduct.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showRetryGetAllDataFeed() {
        retryHandler.enableRetryView();
    }

    @Override
    public void showFeedDataFromCache(List<RecyclerViewItem> dataFeedList) {
        final int historyDataPosition = 0;
        adapter.updateHistoryAdapter(dataFeedList.get(historyDataPosition));
        adapter.addAll(true, false, dataFeedList);
        adapter.notifyItemInserted(0);

    }

    @Override
    public void showRetryLoadMore() {
        adapter.setIsRetry(true);
    }

    @Override
    public void showLoadMoreFeed(List<RecyclerViewItem> dataFeedList) {
        adapter.addNextPage(dataFeedList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean isLoading() {
        return adapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition())
                == TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public void showRefreshLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void enableLoadmore() {
        adapter.setIsLoading(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void disableLoadmore() {
        adapter.setIsLoading(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFeedData(List<RecyclerViewItem> dataFeedList) {
        // I know this is BUSUK caused adapter not revamped yet
        if (dataFeedList != null && dataFeedList.size() > 0) {
            final int historyDataPosition = 0;
            adapter.updateHistoryAdapter(dataFeedList.get(historyDataPosition));
            adapter.addAll(true, false, dataFeedList);
            adapter.notifyItemInserted(0);
        }
    }

    @Override
    public void hideContentView() {
        contentRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showContentView() {
        contentRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshFailed() {
        adapter.setIsRetry(true);
    }


    @Override
    public void onRefresh() {
        feedPresenter.refreshDataFeed();
    }


    private void initVar() {
        final int columnSize
                = ProductFeedHelper.calcColumnSize(getResources().getConfiguration().orientation);

        gridLayoutManager = new GridLayoutManager(getActivity(), columnSize);
        gridLayoutManager.setSpanSizeLookup(getSpanSizeLookup());
        adapter = new DataFeedAdapter(getActivity(), new ArrayList<RecyclerViewItem>());
    }


    private void prepareView(View parentView) {
        contentRecyclerView.setLayoutManager(gridLayoutManager);
        contentRecyclerView.setHasFixedSize(true);
        contentRecyclerView.setAdapter(adapter);
        contentRecyclerView.addOnScrollListener(onRecyclerViewListener());
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setOnRetryListenerRV(onAdapterRetryListener());
        setFabListener();
        retryHandler = new RetryHandler(getActivity(), parentView);


    }

    @NonNull
    private BaseRecyclerViewAdapter.OnRetryListener onAdapterRetryListener() {
        return new BaseRecyclerViewAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                new DefaultRetryListener(
                        DefaultRetryListener.RETRY_FOOTER,
                        FragmentProductFeed.this)
                        .onRetryCliked();
            }
        };
    }

    @NonNull
    private RecyclerView.OnScrollListener onRecyclerViewListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading() && gridLayoutManager.findLastVisibleItemPosition() ==
                        gridLayoutManager.getItemCount() - 1) {
                    feedPresenter.loadMoreDataFeed();
                }
            }
        };
    }

    private GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isPositionInFooter(position)) {
                    return ProductFeedHelper.PORTRAIT_COLUMN_FOOTER;
                } else if (isPositionOnHistory(position)) {
                    return ProductFeedHelper.PORTRAIT_COLUMN_HEADER;
                } else if (isPositionOnTopAds(position)) {
                    return ProductFeedHelper.PORTRAIT_COLUMN_HEADER;
                } else {
                    return ProductFeedHelper.PORTRAIT_COLUMN;
                }
            }
        };
    }

    private void setFabListener() {
        fabAddProduct.setListenerFabClick(this);
        fabAddProduct.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.action_instagram:
                        onAddInstagram();
                        break;
                    case R.id.action_gallery:
                        ProductActivity.moveToImageGalleryCamera(getActivity(), 0, false, 5);
                        break;
                    case R.id.action_camera:
                        onAddGallery();
                        break;
                }
                return false;
            }
        });
    }

    private void onAddGallery() {
        ProductActivity.moveToImageGalleryCamera(getActivity(), 0, true, -1);
    }

    private void onAddInstagram() {
        Intent moveToProductActivity = new Intent(getActivity(), ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        moveToProductActivity.putExtras(bundle);
        startActivity(moveToProductActivity);
    }

    private boolean isPositionOnTopAds(int position) {
        return adapter.isTopAds(position);
    }

    private boolean isPositionOnHistory(int position) {
        return adapter.isHistory(position);
    }

    private boolean isPositionInFooter(int position) {
        return position == adapter.getData().size();
    }

    private void setLocalyticFlow() {
        ScreenTracking.screenLoca(getString(R.string.home_product_feed));
    }

    private void sendAppsFlyerData() {
        ScreenTracking.sendAFGeneralScreenEvent(Jordan.AF_SCREEN_PRODUCT_FEED);
    }

}
