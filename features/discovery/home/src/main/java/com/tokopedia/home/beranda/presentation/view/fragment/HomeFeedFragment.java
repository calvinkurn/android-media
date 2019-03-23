package com.tokopedia.home.beranda.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeFeedAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeFeedItemDecoration;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

public class HomeFeedFragment extends BaseListFragment<HomeFeedViewModel, HomeFeedTypeFactory>
        implements HomeFeedContract.View {

    public static final String ARG_TAB_INDEX = "ARG_TAB_INDEX";
    public static final String ARG_RECOM_ID = "ARG_RECOM_ID";
    public static final String ARG_TAB_NAME = "ARG_TAB_NAME";
    public static final String ARG_TAB_HEIGHT = "ARG_TAB_HEIGHT";

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

    public void setListener(HomeEggListener homeEggListener,
                            HomeTabFeedListener homeTabFeedListener) {
        this.homeEggListener = homeEggListener;
        this.homeTabFeedListener = homeTabFeedListener;
    }

    public void setHomeTrackingQueue(TrackingQueue homeTrackingQueue) {
        this.homeTrackingQueue = homeTrackingQueue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeTrackingQueue = new TrackingQueue(getActivity());
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
                new HomeFeedItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_8))
        );
    }

    @Override
    public void loadData(int page) {
        presenter.attachView(this);
        presenter.loadData(recomId, DEFAULT_TOTAL_ITEM_PER_PAGE, page);
    }

    private void hitHomeFeedImpressionTracker(HomeFeedViewModel homeFeedViewModel) {
        if (userSession.isLoggedIn()){
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

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
    }

    @NonNull
    @Override
    protected BaseListAdapter<HomeFeedViewModel, HomeFeedTypeFactory> createAdapterInstance() {
        HomeFeedAdapter homeFeedAdapter = new HomeFeedAdapter(getAdapterTypeFactory());
        homeFeedAdapter.setOnAdapterInteractionListener(this);
        return homeFeedAdapter;
    }

    @Override
    protected HomeFeedTypeFactory getAdapterTypeFactory() {
        return new HomeFeedTypeFactory(this);
    }

    private void initListeners() {
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
    public void onItemClicked(HomeFeedViewModel homeFeedViewModel) {
        if (userSession.isLoggedIn()) {
            if(!homeFeedViewModel.isTopAds()){
                HomePageTracking.eventClickOnHomeProductFeedForLoggedInUser(
                        homeTrackingQueue,
                        homeFeedViewModel,
                        tabName.toLowerCase()
                );
            }
        } else {
            if(!homeFeedViewModel.isTopAds()){
                HomePageTracking.eventClickOnHomeProductFeedForNonLoginUser(
                        homeTrackingQueue,
                        homeFeedViewModel,
                        tabName.toLowerCase()
                );
            }
        }
        if(homeFeedViewModel.isTopAds()) {
            new ImpresionTask().execute(homeFeedViewModel.getClickUrl());
            Product p = new Product();
            p.setId(homeFeedViewModel.getProductId());
            p.setName(homeFeedViewModel.getProductName());
            p.setPriceFormat(homeFeedViewModel.getPrice());
            TopAdsGtmTracker.getInstance().eventRecomendationProductClick(getContext(), p,
                    tabName.toLowerCase(), homeFeedViewModel.getRecommendationType(),
                    homeFeedViewModel.getCategoryBreadcrumbs(),
                    userSession.isLoggedIn(),
                    homeFeedViewModel.getPosition());
        }
        goToProductDetail(homeFeedViewModel.getProductId(),
                homeFeedViewModel.getImageUrl(),
                homeFeedViewModel.getProductName(), homeFeedViewModel.getPrice());
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        getActivity().startActivity(getProductIntent(productId));
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
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
        return ConstantKey.Analytics.AppScreen.UnifyTracking.SCREEN_UNIFY_HOME_BERANDA;
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
        if(model.isTopAds()) {
            Product p = new Product();
            p.setId(model.getProductId());
            p.setName(model.getProductName());
            p.setPriceFormat(model.getPrice());
            new ImpresionTask().execute(model.getTrackerImageUrl());
            TopAdsGtmTracker.getInstance().addRecomendationProductViewImpressions(p,
                    model.getCategoryBreadcrumbs(), tabName.toLowerCase(),
                    model.getRecommendationType(), model.getPosition());
        } else {
            hitHomeFeedImpressionTracker(model);
        }
    }

    @Override
    public void onPause() {
        TopAdsGtmTracker.getInstance().eventRecomendationProductView(homeTrackingQueue,
                tabName.toLowerCase(), userSession.isLoggedIn());
        homeTrackingQueue.sendAll();
        super.onPause();
    }
}
