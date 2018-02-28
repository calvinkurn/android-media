package com.tokopedia.tkpd.home.favorite.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.perf.metrics.Trace;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.favorite.di.component.DaggerFavoriteComponent;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteAdapter;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteAdapterTypeFactory;
import com.tokopedia.tkpd.home.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Kulomady on 1/20/17.
 */

public class FragmentFavorite extends BaseDaggerFragment
        implements FavoriteContract.View, FavoriteClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final long DURATION_ANIMATOR = 1000;

    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    ProgressBar progressBar;
    RelativeLayout mainContent;
    View wishlistNotLoggedIn;

    @Inject
    FavoritePresenter favoritePresenter;

    private Unbinder unbinder;
    private FavoriteAdapter favoriteAdapter;
    private EndlessRecyclerviewListener recylerviewScrollListener;
    private SnackbarRetry messageSnackbar;
    private boolean isWishlistNetworkFailed;
    private boolean isFavoriteShopNetworkFailed;
    private boolean isTopAdsShopNetworkFailed;
    private View favoriteShopViewSelected;
    private TopAdsShopItem shopItemSelected;
    private Trace trace;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        trace = TrackingUtils.startTrace("favorite_trace");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_index_favorite_v2, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.index_favorite_recycler_view);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        progressBar = (ProgressBar) parentView.findViewById(R.id.include_loading);
        mainContent = (RelativeLayout) parentView.findViewById(R.id.main_content);
        wishlistNotLoggedIn = parentView.findViewById(R.id.partial_empty_wishlist);

        if (SessionHandler.isV4Login(getActivity())) {
            prepareView();
            favoritePresenter.attachView(this);
            checkImpressionOncreate();
        } else {
            wishlistNotLoggedIn.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }
        return parentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        favoritePresenter.detachView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        favoritePresenter.onSaveDataBeforeRotate(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        favoritePresenter.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            if (isVisibleToUser && isAdded() && getActivity() != null) {
                if (isAdapterNotEmpty()) {
                    validateMessageError();
                } else {
                    favoritePresenter.loadInitialData();
                }
                ScreenTracking.screen(getScreenName());
            } else {
                if (messageSnackbar != null && messageSnackbar.isShown()) {
                    messageSnackbar.hideRetrySnackbar();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            onCreate(new Bundle());
        }

    }

    @Override
    public void validateMessageError() {
        if (messageSnackbar != null) {
            if (isWishlistNetworkFailed
                    || isFavoriteShopNetworkFailed || isTopAdsShopNetworkFailed) {
                messageSnackbar.showRetrySnackbar();
            } else {
                messageSnackbar.hideRetrySnackbar();
            }
        }
    }

    @Override
    public void showErrorAddFavoriteShop() {
        NetworkErrorHelper.createSnackbarWithAction(
                getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {

                    @Override
                    public void onRetryClicked() {
                        if (favoriteShopViewSelected != null && shopItemSelected != null) {
                            favoritePresenter
                                    .addFavoriteShop(favoriteShopViewSelected, shopItemSelected);
                        }
                    }
                }).showRetrySnackbar();
    }

    @Override
    protected void initInjector() {
        DaggerFavoriteComponent daggerFavoriteComponent
                = (DaggerFavoriteComponent) DaggerFavoriteComponent.builder()
                .appComponent(MainApplication.getInstance().getApplicationComponent())
                .build();
        daggerFavoriteComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_SHOP_FAVORIT;
    }

    @Override
    public void refreshDataFavorite(List<Visitable> elementList) {
        favoriteAdapter.hideLoading();
        favoriteAdapter.clearData();
        favoriteAdapter.setElement(elementList);
    }

    @Override
    public void showInitialDataPage(List<Visitable> dataFavorite) {
        favoriteAdapter.hideLoading();
        favoriteAdapter.clearData();
        favoriteAdapter.setElement(dataFavorite);
        TrackingUtils.sendMoEngageOpenFavoriteEvent(dataFavorite.size());
    }

    @Override
    public void showMoreDataFavoriteShop(List<Visitable> elementList) {
        favoriteAdapter.hideLoading();
        favoriteAdapter.addMoreData(elementList);
    }


    @Override
    public void showRefreshLoading() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void hideRefreshLoading() {
        swipeToRefresh.setRefreshing(false);
        recylerviewScrollListener.resetState();
        if(trace!=null)
            trace.stop();
    }


    @Override
    public void showErrorLoadMore() {
        NetworkErrorHelper.createSnackbarWithAction(
                getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {

                    @Override
                    public void onRetryClicked() {
                        favoriteAdapter.hideLoading();
                        favoritePresenter.loadMoreFavoriteShop();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void showErrorLoadData() {
        if (isAdapterNotEmpty()) {
            showTopadsShopFailedMessage();
            validateMessageError();
        } else {
            NetworkErrorHelper.showEmptyState(getContext(),
                    mainContent,
                    new NetworkErrorHelper.RetryClickedListener() {

                        @Override
                        public void onRetryClicked() {
                            favoritePresenter.refreshAllDataFavoritePage();
                        }
                    });
        }
    }

    @Override
    public void showWishlistFailedMessage() {
        isWishlistNetworkFailed = true;
    }

    @Override
    public void dismissWishlistFailedMessage() {
        isWishlistNetworkFailed = false;
    }

    @Override
    public void showFavoriteShopFailedMessage() {
        isFavoriteShopNetworkFailed = true;
    }

    @Override
    public void dismissFavoriteShopFailedMessage() {
        isFavoriteShopNetworkFailed = false;
    }

    @Override
    public void showTopadsShopFailedMessage() {
        isTopAdsShopNetworkFailed = true;
    }

    @Override
    public void dismissTopadsShopFailedMessage() {
        isTopAdsShopNetworkFailed = false;
    }

    @Override
    public boolean isLoading() {
        return favoriteAdapter.isLoading();
    }


    @Override
    public void showLoadMoreLoading() {
        favoriteAdapter.showLoading();
    }

    @Override
    public void addFavoriteShop(FavoriteShopViewModel shopViewModel) {
        int favoriteShopPosition = 2;
        favoriteAdapter.addElement(favoriteShopPosition, shopViewModel);
    }

    @Override
    public void onRefresh() {
        favoritePresenter.refreshAllDataFavoritePage();
    }

    @Override
    public void onFavoriteShopClicked(View view, TopAdsShopItem shopItemSelected) {
        favoriteShopViewSelected = view;
        this.shopItemSelected = shopItemSelected;
        favoritePresenter.addFavoriteShop(favoriteShopViewSelected, this.shopItemSelected);
    }

    private void prepareView() {

        initRecyclerview();
        swipeToRefresh.setOnRefreshListener(this);
        messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        favoritePresenter.refreshAllDataFavoritePage();
                    }
                });

    }

    private void initRecyclerview() {
        FavoriteAdapterTypeFactory typeFactoryForList = new FavoriteAdapterTypeFactory(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favoriteAdapter = new FavoriteAdapter(typeFactoryForList, new ArrayList<Visitable>());
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(DURATION_ANIMATOR);
        recylerviewScrollListener = new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                favoritePresenter.loadMoreFavoriteShop();
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.addOnScrollListener(recylerviewScrollListener);

    }

    private boolean isAdapterNotEmpty() {
        return favoriteAdapter.getItemCount() > 0;
    }

    private void checkImpressionOncreate() {
        final int indexTabFavorite = 2;
        if (getActivity() instanceof ParentIndexHome) {
            if (((ParentIndexHome) getActivity()).getViewPager() != null) {
                if (!isAdapterNotEmpty()
                        && ((ParentIndexHome) getActivity())
                        .getViewPager().getCurrentItem() == indexTabFavorite) {

                    favoritePresenter.loadInitialData();
                }
            }
        }
    }
}
