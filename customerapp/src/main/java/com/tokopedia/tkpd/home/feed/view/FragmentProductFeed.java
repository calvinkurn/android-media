package com.tokopedia.tkpd.home.feed.view;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.RetryHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.instoped.InstagramAuth;
import com.tokopedia.seller.instoped.InstopedActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.adapter.DataFeedAdapter;
import com.tokopedia.tkpd.home.feed.data.source.cloud.AddFavoriteShopService;
import com.tokopedia.tkpd.home.feed.di.component.DaggerDataFeedComponent;
import com.tokopedia.tkpd.home.util.DefaultRetryListener;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;
import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.FAVORITE_TAB;
import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.HOTLIST_TAB;

@RuntimePermissions
public class FragmentProductFeed extends BaseDaggerFragment implements FeedContract.View,
        DefaultRetryListener.OnClickRetry, ListenerFabClick, SwipeRefreshLayout.OnRefreshListener,
        TopAdsItemClickListener {

    private static final String TAG = "FragmentProductFeed";
    public static final int MAX_CHOOSEN_IMAGE = 10;

    @BindView(R.id.index_main_recycler_view)
    RecyclerView contentRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeRefreshLayout;
    @BindView(R.id.fab_speed_dial)
    FabSpeedDial fabAddProduct;
    @BindView(R.id.main_content)
    LinearLayout mainContentLinearLayout;
    @BindView(R.id.empty_product_feed)
    LinearLayout emptyFeedView;
    @BindView(R.id.empty_layout_history)
    RelativeLayout emptyHistoryView;

    @Inject
    FeedPresenter feedPresenter;

    View parentView;

    private GridLayoutManager gridLayoutManager;
    private DataFeedAdapter adapter;
    private Unbinder unbinder;
    private RetryHandler retryHandler;
    private TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private static final String TOPADS_ITEM = "2";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initVar();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_feed, container, false);
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
        if (shopID == null || shopID.equals("0") || shopID.length() == 0) {
            fabAddProduct.setVisibility(View.GONE);
            fabAddProduct.setVisibility(View.GONE);
        } else {
            fabAddProduct.setVisibility(View.VISIBLE);
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
        DaggerDataFeedComponent feedComponent
                = (DaggerDataFeedComponent) DaggerDataFeedComponent
                .builder()
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this)).build();
        feedComponent.inject(this);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null) {
            ScreenTracking.screen(getScreenName());
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
        adapter.removeRetry();
        topAdsRecyclerAdapter.showLoading();
        topAdsRecyclerAdapter.setEndlessScrollListener();
        topAdsRecyclerAdapter.shouldLoadAds(true);
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
        TrackingUtils.sendMoEngageOpenFeedEvent(dataFeedList.size());
        final int historyDataPosition = 0;
        if (dataFeedList.get(historyDataPosition) instanceof HistoryProductListItem) {
            topAdsRecyclerAdapter.setHasHeader(true);
        } else {
            topAdsRecyclerAdapter.setHasHeader(false);
        }
        adapter.updateHistoryAdapter(dataFeedList.get(historyDataPosition));
        topAdsRecyclerAdapter.shouldLoadAds(false);
        adapter.setData(dataFeedList);
    }

    @Override
    public void showRetryLoadMore() {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        topAdsRecyclerAdapter.unsetEndlessScrollListener();
        topAdsRecyclerAdapter.hideLoading();
        adapter.setRetryFeed();
    }

    @Override
    public void showLoadMoreFeed(List<RecyclerViewItem> dataFeedList) {
        adapter.addNextPage(dataFeedList);
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
    }

    @Override
    public void disableLoadmore() {
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public void refreshFeedData(List<RecyclerViewItem> dataFeedList) {
        // I know this is BUSUK caused adapter not revamped yet
        if (dataFeedList != null && dataFeedList.size() > 0) {
            final int historyDataPosition = 0;
            if (dataFeedList.get(historyDataPosition) instanceof HistoryProductListItem) {
                topAdsRecyclerAdapter.setHasHeader(true);
            } else {
                topAdsRecyclerAdapter.setHasHeader(false);
            }
            adapter.updateHistoryAdapter(dataFeedList.get(historyDataPosition));
            topAdsRecyclerAdapter.reset();
            topAdsRecyclerAdapter.shouldLoadAds(dataFeedList.size() > 1);
            adapter.setData(dataFeedList);
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
    public void showEmptyHistoryProduct() {
        if (!(adapter != null && adapter.getHistoryAdapter() != null
                && adapter.getHistoryAdapter().getData() != null
                && adapter.getHistoryAdapter().getData().size() > 0)) {
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
    public void showEmptyFeedAdapter() {
        adapter.setEmptyFeed();
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
                        topAdsRecyclerAdapter.setEndlessScrollListener();
                        topAdsRecyclerAdapter.shouldLoadAds(true);
                        feedPresenter.refreshDataFeed();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public boolean isViewNotEmpty() {
        return !adapter.getData().isEmpty();
    }

    @Override
    public void showErrorFeed() {
        NetworkErrorHelper.showEmptyState(getContext(), parentView,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        topAdsRecyclerAdapter.setEndlessScrollListener();
                        topAdsRecyclerAdapter.shouldLoadAds(true);
                        feedPresenter.refreshDataFeed();
                    }
                });
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
        topAdsRecyclerAdapter.shouldLoadAds(true);
        topAdsRecyclerAdapter.setEndlessScrollListener();
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
        adapter = new DataFeedAdapter(getActivity(), new ArrayList<RecyclerViewItem>());
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .withPreferedCategory()
                .setEndpoint(Endpoint.PRODUCT)
                .topAdsParams(generateTopAdsParams())
                .build();
        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), adapter);
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setSpanSizeLookup(getSpanSizeLookup());
        topAdsRecyclerAdapter.setHasHeader(true);
        topAdsRecyclerAdapter.setConfig(config);
    }

    @Override
    public void onProductItemClicked(Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        Shop shop = dataShop.getShop();
        Intent intent = new Intent(getActivity(), AddFavoriteShopService.class);
        intent.putExtra(
                AddFavoriteShopService.EXTRAS_SESSION_ID, SessionHandler.getLoginID(getContext()));

        intent.putExtra(AddFavoriteShopService.EXTRAS_SHOP_ID, shop.getId());
        intent.putExtra(AddFavoriteShopService.EXTRAS_AD_KEY, dataShop.getAdRefKey());
        getActivity().startService(intent);
    }

    private TopAdsParams generateTopAdsParams() {
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, TopAdsParams.SRC_PRODUCT_FEED);
        params.getParam().put(TopAdsParams.KEY_ITEM, TOPADS_ITEM);
        return params;
    }

    private void prepareView(View parentView) {
        contentRecyclerView.setLayoutManager(gridLayoutManager);
        contentRecyclerView.setHasFixedSize(true);

        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                Log.d(TAG, "onLoadMore totalItemsCount " + totalCount + " page " + page);
                feedPresenter.loadMoreDataFeed();
            }
        });
        contentRecyclerView.setAdapter(topAdsRecyclerAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setOnRetryListener(onAdapterRetryListener());
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

    private GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isPositionOnHistory(position)
                        || isPositionOnEmptyFeed(position)
                        || isPositionOnRetryFeed(position)
                        || topAdsRecyclerAdapter.isTopAdsViewHolder(position)
                        || topAdsRecyclerAdapter.isLoading(position)) {
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
                    case R.id.action_gallery:
                        GalleryActivity.moveToImageGalleryCamera(getActivity(), 0, false, 5);
                        break;
                    case R.id.action_camera:
                        onAddGallery();
                        break;
                }
                return false;
            }
        });
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onInstagramClicked() {
        if (getActivity().getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getActivity().getApplication()).startInstopedActivityForResult(getContext(),
                    FragmentProductFeed.this,
                    INSTAGRAM_SELECT_REQUEST_CODE, MAX_CHOOSEN_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTAGRAM_SELECT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<InstagramMediaModel> images = data.getParcelableArrayListExtra(GalleryActivity.PRODUCT_SOC_MED_DATA);
            if (images == null || images.size() == 0) {
                return;
            }
            Application application = getActivity().getApplication();
            if(application != null && application instanceof SellerModuleRouter){
                ((SellerModuleRouter) application).goMultipleInstagramAddProduct(
                        getContext(), images);
            }
        }
    }

    private void onAddGallery() {
        GalleryActivity.moveToImageGalleryCamera(getActivity(), 0, true, -1);
    }

    private void onAddInstagram() {
        Intent moveToProductActivity = new Intent(getActivity(), InstopedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(InstopedActivity.FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        moveToProductActivity.putExtras(bundle);
        startActivity(moveToProductActivity);
    }

    private boolean isPositionOnHistory(int position) {
        return adapter.isHistory(position);
    }

    private boolean isPositionOnEmptyFeed(int position) {
        return adapter.isEmptyFeed(position);
    }

    private boolean isPositionOnRetryFeed(int position) {
        return adapter.isRetry(topAdsRecyclerAdapter.getPlacer().getItem(position).originalPos());
    }

    private void sendAppsFlyerData() {
        ScreenTracking.sendAFGeneralScreenEvent(Jordan.AF_SCREEN_PRODUCT_FEED);
    }

}
