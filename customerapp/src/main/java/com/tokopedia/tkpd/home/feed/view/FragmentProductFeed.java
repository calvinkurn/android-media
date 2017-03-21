package com.tokopedia.tkpd.home.feed.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.RetryHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.instoped.InstagramAuth;
import com.tokopedia.seller.instoped.InstopedActivity;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.myproduct.ProductActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.adapter.DataFeedAdapter;
import com.tokopedia.tkpd.home.feed.di.component.DaggerDataFeedComponent;
import com.tokopedia.tkpd.home.util.DefaultRetryListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tokopedia.core.home.adapter.ProductFeedAdapter.FAVORITE_TAB;
import static com.tokopedia.core.home.adapter.ProductFeedAdapter.HOTLIST_TAB;


public class FragmentProductFeed extends BaseDaggerFragment implements FeedContract.View,
        DefaultRetryListener.OnClickRetry, ListenerFabClick, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.index_main_recycler_view)
    RecyclerView contentRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeRefreshLayout;
    @BindView(R.id.fab_speed_dial)
    FloatingActionButton fabAddProduct;
    @BindView(R.id.main_content)
    LinearLayout mainContentLinearLayout;
    @BindView(R.id.empty_wishlist)
    RelativeLayout emptyFeedView;
    @BindView(R.id.empty_layout_history)
    RelativeLayout emptyHistoryView;

    @Inject
    FeedPresenter feedPresenter;

    private GridLayoutManager gridLayoutManager;
    private DataFeedAdapter adapter;
    private Unbinder unbinder;
    private RetryHandler retryHandler;

    private int currentTopAdsPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initVar();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_feed, container, false);
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
//        if (!fabAddProduct.isShown()) {
//            fabAddProduct.setVisibility(View.VISIBLE);
//        }
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
        adapter.notifyItemInserted(historyDataPosition);

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
            adapter.addAll(true, true, dataFeedList);
            adapter.notifyItemInserted(0);
            currentTopAdsPage = 3;
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
        feedPresenter.initializeDataFeed();
        if (adapter.getData().size() > 0) {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            feedPresenter.refreshDataFeed();
                        }
                    }).showRetrySnackbar();

        }
    }

    @Override
    public String getTopAdsPage() {
        return String.valueOf(currentTopAdsPage);
    }

    @Override
    public void increaseTopAdsPage() {
        currentTopAdsPage += 2;
    }

    @Override
    public void showEmptyHistoryProduct() {
        if(!(adapter!=null && adapter.getHistoryAdapter()!=null
                && adapter.getHistoryAdapter().getData() !=null
                && adapter.getHistoryAdapter().getData().size() > 0)){
        emptyHistoryView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyHistoryProduct() {
        emptyHistoryView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyFeed() {
        emptyFeedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidFeed() {
        emptyFeedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyFeed() {
        emptyFeedView.setVisibility(View.GONE);
    }

    @Override
    public void showMessageRefreshFailed() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        feedPresenter.refreshDataFeed();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public boolean isViewNotEmpty() {
        return !adapter.getData().isEmpty();
    }

    @Override
    public void forceShowEmptyHistory() {
         emptyHistoryView.setVisibility(View.VISIBLE);
    }

    @Override
    public HistoryProductListItem getViewmodelHistory() {
        if (adapter != null && adapter.getData() != null && !adapter.getData().isEmpty()) {
            return (HistoryProductListItem) adapter.getData().get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<RecyclerViewItem> getViewmodelFeed() {
        if (adapter != null && adapter.getData() != null && !adapter.getData().isEmpty()) {
            return adapter.getData();
        } else {
            return null;
        }
    }


    @Override
    public void onRefresh() {
        feedPresenter.refreshDataFeed();
    }


    @OnClick(R.id.find_now)
    void onFindNowClicked() {
        ParentIndexHome.ChangeTabListener listener
                = ((ParentIndexHome) getContext()).GetHotListListener();

        listener.onChangeTab(HOTLIST_TAB);
    }

    @OnClick(R.id.find_favorite_shop)
    void onFindFavoriteClicked() {
        ParentIndexHome.ChangeTabListener listener
                = ((ParentIndexHome) getContext()).GetFavoriteListener();

        listener.onChangeTab(FAVORITE_TAB);
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
                } else if (isPositionOnEmptyFeed(position)) {
                    return ProductFeedHelper.PORTRAIT_COLUMN_HEADER;
                } else {
                    return ProductFeedHelper.PORTRAIT_COLUMN;
                }
            }
        };
    }

    private void setFabListener() {
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.moveToAddProduct(getActivity());
            }
        });
//        fabAddProduct.setListenerFabClick(this);
//        fabAddProduct.setMenuListener(new SimpleMenuListenerAdapter() {
//            @Override
//            public boolean onMenuItemSelected(MenuItem menuItem) {
//                int id = menuItem.getItemId();
//
//                switch (id) {
//                    case R.id.action_instagram:
//                        onAddInstagram();
//                        break;
//                    case R.id.action_gallery:
//                        GalleryActivity.moveToImageGalleryCamera(getActivity(), 0, false, 5);
//                        break;
//                    case R.id.action_camera:
//                        onAddGallery();
//                        break;
//                }
//                return false;
//            }
//        });
    }

    private void onAddGallery() {
        GalleryActivity.moveToImageGalleryCamera(getActivity(), 0, true, -1);
    }

    private void onAddInstagram() {
        Intent moveToProductActivity = new Intent(getActivity(), InstopedActivity.class);
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

    private boolean isPositionOnEmptyFeed(int position) {
        return adapter.isEmptyFeed(position);
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
