package com.tokopedia.home.beranda.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.helper.HomeFeedEndlessScrollListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeFeedAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeFeedItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeFeedViewHolder;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class HomeFeedFragment extends BaseListFragment<Visitable<HomeFeedTypeFactory>, HomeFeedTypeFactory>
        implements HomeFeedContract.View {

    public static final String ARG_TAB_INDEX = "ARG_TAB_INDEX";
    public static final String ARG_RECOM_ID = "ARG_RECOM_ID";
    public static final String ARG_TAB_NAME = "ARG_TAB_NAME";
    public static final String ARG_TAB_HEIGHT = "ARG_TAB_HEIGHT";
    private static final String PDP_EXTRA_PRODUCT_ID = "product_id";
    private static final String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";
    private static final String WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition";
    private static final int REQUEST_FROM_PDP = 349;

    private static final int DEFAULT_TOTAL_ITEM_PER_PAGE = 12;
    private static final int DEFAULT_SPAN_COUNT = 2;

    @Inject
    HomeFeedPresenter presenter;

    @Inject
    UserSessionInterface userSession;

    private TrackingQueue homeTrackingQueue;

    private int totalScrollY;
    private int tabIndex;
    private int recomId;
    private String tabName;
    private boolean hasLoadData;
    private HomeEggListener homeEggListener;
    private HomeTabFeedListener homeTabFeedListener;
    private RecyclerView.RecycledViewPool parentPool;
    private HomeCategoryListener homeCategoryListener;

    public static HomeFeedFragment newInstance(int tabIndex,
                                               int recomId,
                                               String tabName) {
        HomeFeedFragment homeFeedFragment = new HomeFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HomeFeedFragment.ARG_TAB_INDEX, tabIndex);
        bundle.putInt(HomeFeedFragment.ARG_RECOM_ID, recomId);
        bundle.putString(HomeFeedFragment.ARG_TAB_NAME, tabName);
        homeFeedFragment.setArguments(bundle);
        return homeFeedFragment;
    }

    public void setListener(HomeCategoryListener homeCategoryListener,
                            HomeEggListener homeEggListener,
                            HomeTabFeedListener homeTabFeedListener) {
        this.homeCategoryListener = homeCategoryListener;
        this.homeEggListener = homeEggListener;
        this.homeTabFeedListener = homeTabFeedListener;
    }

    public void setParentPool(RecyclerView.RecycledViewPool parentPool) {
        this.parentPool = parentPool;
    }

    public void setHomeTrackingQueue(TrackingQueue homeTrackingQueue) {
        this.homeTrackingQueue = homeTrackingQueue;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home_feed_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabIndex = getArguments().getInt(ARG_TAB_INDEX);
        recomId = getArguments().getInt(ARG_RECOM_ID);
        tabName = getArguments().getString(ARG_TAB_NAME);
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadFirstPageData();
        initListeners();
    }

    private void setupRecyclerView() {
        ((StaggeredGridLayoutManager) getRecyclerView(getView()).getLayoutManager())
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        getRecyclerView(getView()).addItemDecoration(
                new HomeFeedItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_4))
        );
        if (parentPool != null) {
            parentPool.setMaxRecycledViews(
                    HomeFeedViewHolder.Companion.getLAYOUT(),
                    20
            );
            getRecyclerView(getView()).setRecycledViewPool(parentPool);
        }
    }
    @Override
    public void loadData(int page) {
        presenter.attachView(this);
        presenter.loadData(recomId, DEFAULT_TOTAL_ITEM_PER_PAGE, page);
    }

    @Override
    protected void showLoading() {
        if (!getAdapter().isContainData()) getAdapter().removeErrorNetwork();

        getAdapter().setLoadingModel(getLoadingModel());
        getAdapter().showLoading();
        hideSnackBarRetry();
    }

    @Override
    protected EndlessRecyclerViewScrollListener createEndlessRecyclerViewListener() {
        return new HomeFeedEndlessScrollListener(getRecyclerView(getView()).getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showLoading();
                loadData(page);
            }
        };
    }

    private void hitHomeFeedImpressionTracker(HomeFeedViewModel homeFeedViewModel) {
        if(homeTrackingQueue != null) {
            if (userSession.isLoggedIn()) {
                HomePageTracking.eventImpressionOnProductRecommendationForLoggedInUser(
                        homeTrackingQueue,
                        homeFeedViewModel,
                        tabName.toLowerCase()
                );
            } else {
                HomePageTracking.eventImpressionOnProductRecommendationForNonLoginUser(
                        homeTrackingQueue,
                        homeFeedViewModel,
                        tabName.toLowerCase()
                );
            }
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void showGetListError(Throwable throwable) {
        hideLoading();

        updateStateScrollListener();

        // Note: add element should be the last in line.
        if (getAdapter().getItemCount() > 0) {
            Toaster.INSTANCE.make(getView(), getString(R.string.home_error_connection), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, "", (v)->{});
        } else {
            onGetListErrorWithEmptyData(throwable);
        }
    }

    @NonNull
    @Override
    protected BaseListAdapter<Visitable<HomeFeedTypeFactory>, HomeFeedTypeFactory> createAdapterInstance() {
        HomeFeedAdapter homeFeedAdapter = new HomeFeedAdapter(getAdapterTypeFactory());
        return homeFeedAdapter;
    }

    @Override
    protected HomeFeedTypeFactory getAdapterTypeFactory() {
        return new HomeFeedTypeFactory(this);
    }

    private void initListeners() {
        if(getView() == null) return;

        getRecyclerView(getView()).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalScrollY += dy;

                if (!getUserVisibleHint()) {
                    return;
                }

                if (homeEggListener != null) {
                    homeEggListener.hideEggOnScroll();
                }
                if (homeTabFeedListener != null) {
                    homeTabFeedListener.onFeedContentScrolled(dy, totalScrollY);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!getUserVisibleHint()) {
                    return;
                }
                if (homeTabFeedListener != null) {
                    homeTabFeedListener.onFeedContentScrollStateChanged(newState);
                }
            }
        });
    }

    @Override
    public void onItemClicked(Visitable<HomeFeedTypeFactory> homeFeedViewModel) {
    }

    private void goToProductDetail(String productId, int position) {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
            intent.putExtra(WISHLIST_STATUS_UPDATED_POSITION, position);
            startActivityForResult(intent, REQUEST_FROM_PDP);
        }
    }

    private void updateWishlist(String id, boolean isWishlist, int position) {
        if(position > -1 && getAdapter().getData() != null &&
                getAdapter().getDataSize() > position && getAdapter().getData().get(position) instanceof HomeFeedViewModel) {
            HomeFeedViewModel model = (HomeFeedViewModel) getAdapter().getData().get(position);
            if (model.getProductId().equals(id)) {
                model.setWishList(isWishlist);
                getAdapter().notifyItemChanged(position);
            }
        }
    }

    @Override
    protected boolean callInitialLoadAutomatically() {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        loadFirstPageData();
    }

    private void loadFirstPageData() {
        if (getUserVisibleHint() && isAdded() && getActivity() != null && presenter != null && !hasLoadData) {
            hasLoadData = true;
            loadInitialData();
        }
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            BerandaComponent component = DaggerBerandaComponent.builder().baseAppComponent(((BaseMainApplication)
                    getActivity().getApplication()).getBaseAppComponent()).build();
            component.inject(this);
            component.inject(presenter);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public void scrollToTop() {
        if (getView() == null) {
            return;
        }

        StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) getRecyclerView(getView()).getLayoutManager());

        if (staggeredGridLayoutManager != null && staggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0] > 10) {
            getRecyclerView(getView()).scrollToPosition(10);
        }
        getRecyclerView(getView()).smoothScrollToPosition(0);
    }

    @Override
    public void onProductImpression(HomeFeedViewModel model, int position) {
        if (model.isTopAds()) {
            if(userSession.isLoggedIn()){
                homeTrackingQueue.putEETracking((HashMap<String, Object>) HomeRecommendationTracking.INSTANCE.getRecommendationProductViewLoginTopAds(
                        tabName.toLowerCase(),
                        model
                ));
            } else {
                homeTrackingQueue.putEETracking((HashMap<String, Object>) HomeRecommendationTracking.INSTANCE.getRecommendationProductViewNonLoginTopAds(
                        tabName.toLowerCase(),
                        model
                ));
            }
        } else {
            if(userSession.isLoggedIn()){
                homeTrackingQueue.putEETracking((HashMap<String, Object>) HomeRecommendationTracking.INSTANCE.getRecommendationProductViewLogin(
                        tabName.toLowerCase(),
                        model
                ));
            } else {
                homeTrackingQueue.putEETracking((HashMap<String, Object>) HomeRecommendationTracking.INSTANCE.getRecommendationProductViewNonLogin(
                        tabName.toLowerCase(),
                        model
                ));
            }
        }
    }

    @Override
    public void onWishlistClick(@NotNull HomeFeedViewModel homeFeedViewModel,
                                int position,
                                boolean isAddWishlist,
                                @NotNull Function2<? super Boolean, ? super Throwable, Unit> responseWishlist) {
        if(presenter.isLogin()) {
            if (isAddWishlist) {
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(HomeRecommendationTracking.INSTANCE.getRecommendationAddWishlistLogin(homeFeedViewModel.getProductId(), tabName));
                presenter.addWishlist(homeFeedViewModel, responseWishlist);
            } else {
                HomePageTracking.eventClickRemoveWishlistOnProductRecommendation(getActivity(), tabName);
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(HomeRecommendationTracking.INSTANCE.getRecommendationRemoveWishlistLogin(homeFeedViewModel.getProductId(), tabName));
                presenter.removeWishlist(homeFeedViewModel, responseWishlist);
            }
        }else {
            TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(HomeRecommendationTracking.INSTANCE.getRecommendationAddWishlistNonLogin(homeFeedViewModel.getProductId(), tabName));
            RouteManager.route(getContext(), ApplinkConst.LOGIN);
        }
    }

    @Override
    public void onProductClick(HomeFeedViewModel homeFeedViewModel, int position) {
        if (userSession.isLoggedIn()) {
            if (!homeFeedViewModel.isTopAds()) {
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(HomeRecommendationTracking.INSTANCE.getRecommendationProductClickLogin(
                        tabName.toLowerCase(),
                        homeFeedViewModel
                ));
            } else {
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(HomeRecommendationTracking.INSTANCE.getRecommendationProductClickLoginTopAds(
                        tabName.toLowerCase(),
                        homeFeedViewModel
                ));
            }
        } else {
            if (!homeFeedViewModel.isTopAds()) {
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(HomeRecommendationTracking.INSTANCE.getRecommendationProductClickNonLogin(
                        tabName.toLowerCase(),
                        homeFeedViewModel
                ));
            } else {
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(HomeRecommendationTracking.INSTANCE.getRecommendationProductClickNonLoginTopAds(
                        tabName.toLowerCase(),
                        homeFeedViewModel
                ));
            }
        }
        goToProductDetail(homeFeedViewModel.getProductId(), position);
    }

    @Override
    public void onPause() {
        if(homeTrackingQueue != null) {
            TopAdsGtmTracker.getInstance().eventRecomendationProductView(homeTrackingQueue,
                    tabName.toLowerCase(), userSession.isLoggedIn());
            homeTrackingQueue.sendAll();
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FROM_PDP && data != null && data.hasExtra(WIHSLIST_STATUS_IS_WISHLIST)) {
            String id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID);
            boolean wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST, false);
            int position = data.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1);
            updateWishlist(id, wishlistStatusFromPdp, position);
        }
    }

    @Override
    public TrackingQueue getTrackingQueue() {
        return homeTrackingQueue;
    }

    @Override
    public String getTabName() {
        return tabName;
    }

    @Override
    public void renderList(@NonNull List<Visitable<HomeFeedTypeFactory>> list, boolean hasNextPage) {
        hideLoading();
        // remove all unneeded element (empty/retry/loading/etc)
        if (isLoadingInitialData) {
            clearAllData();
        }
        getAdapter().addMoreData(list);
        // update the load more state (paging/can loadmore)
        updateScrollListenerState(hasNextPage);

        if (isListEmpty()) {
            showEmpty();
        } else {
            //set flag to false, indicate that the initial data has been set.
            isLoadingInitialData = false;
        }

        // load next page data if adapter data less than minimum scrollable data
        // when the list has next page and auto load next page is enabled
        if (getAdapter().getDataSize() < getMinimumScrollableNumOfItems() && isAutoLoadEnabled()
                && hasNextPage && endlessRecyclerViewScrollListener !=  null) {
            endlessRecyclerViewScrollListener.loadMoreNextPage();
        }
    }

    public void smoothScrollRecyclerViewByVelocity(int distance) {
        if (getView() != null && getRecyclerView(getView()) != null) {
            getRecyclerView(getView()).smoothScrollBy(0, distance);
        }
    }
}
