package com.tokopedia.home.account.favorite.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.account.favorite.view.adapter.FavoriteAdapter;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.di.component.DaggerFavoriteComponent;
import com.tokopedia.home.account.favorite.view.adapter.FavoriteAdapterTypeFactory;
import com.tokopedia.home.account.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.home.account.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.home.account.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * @author Kulomady on 1/20/17.
 */

public class FragmentFavorite extends BaseDaggerFragment
        implements FavoriteContract.View, FavoriteClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = FragmentFavorite.class.getSimpleName();

    private static final long DURATION_ANIMATOR = 1000;
    private static final String FAVORITE_TRACE = "mp_favourite_shop";
    private static final String FAVORITE_SHOP_SCREEN_NAME = "/favorite";
    public static final String SCREEN_PARAM_IS_LOGGED_IN_STATUS = "isLoggedInStatus";
    public static final String SCREEN_PARAM_IS_FAVOURITE_EMPTY = "isFavouriteEmpty";
    private Boolean isUserEventTrackerDoneTrack = false;

    //value is logged in is always true, because favorite shop can only be open from
    //account fragment, which is only logged in user that can access that tab
    public static final String VALUE_IS_LOGGED_IN_FAVORITE_SHOP = "true";

    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    ProgressBar progressBar;
    RelativeLayout mainContent;
    View wishlistNotLoggedIn;
    Button btnLogin;

    @Inject
    FavoritePresenter favoritePresenter;

    private FavoriteAdapter favoriteAdapter;
    private EndlessRecyclerviewListener recylerviewScrollListener;
    private SnackbarRetry messageSnackbar;
    private boolean isFavoriteShopNetworkFailed;
    private boolean isTopAdsShopNetworkFailed;
    private View favoriteShopViewSelected;
    private TopAdsShopItem shopItemSelected;
    private PerformanceMonitoring performanceMonitoring;

    public static Fragment newInstance() {
        return new FragmentFavorite();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        performanceMonitoring = PerformanceMonitoring.start(FAVORITE_TRACE);
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
        btnLogin = parentView.findViewById(R.id.btn_login);

        if (SessionHandler.isV4Login(getActivity())) {
            prepareView();
            favoritePresenter.attachView(this);
            favoritePresenter.loadInitialData();
        } else {
            wishlistNotLoggedIn.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }
        return parentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoritePresenter.detachView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity().getApplication() instanceof TkpdCoreRouter) {
                        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getLoginIntent(getContext());
                        startActivity(intent);
                    }
                }
            });
        }
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
                ScreenTracking.screen(MainApplication.getAppContext(), getScreenName());
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
            if (isFavoriteShopNetworkFailed || isTopAdsShopNetworkFailed) {
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
    public void stopLoadingFavoriteShop() {
        favoriteAdapter.hideLoading();
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
        sendFavoriteShopScreenTracker(dataFavorite.isEmpty());
        favoriteAdapter.hideLoading();
        favoriteAdapter.clearData();
        favoriteAdapter.setElement(dataFavorite);
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(getActivity()),
                AppEventTracking.MOENGAGE.IS_FAVORITE_EMPTY, dataFavorite.size() == 0
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.OPEN_FAVORITE);
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
    }

    @Override
    public void stopTracePerformanceMonitoring() {
        performanceMonitoring.stopTrace();
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

    private void sendFavoriteShopScreenTracker(boolean isFavouriteEmpty) {
        if (!isUserEventTrackerDoneTrack) {
            HashMap<String, String> customDimensions = new HashMap<>();
            customDimensions.put(SCREEN_PARAM_IS_LOGGED_IN_STATUS, VALUE_IS_LOGGED_IN_FAVORITE_SHOP);
            customDimensions.put(SCREEN_PARAM_IS_FAVOURITE_EMPTY, String.valueOf(isFavouriteEmpty));

            TrackApp.getInstance().getGTM().sendScreenAuthenticated(
                    FAVORITE_SHOP_SCREEN_NAME,
                    customDimensions
            );
            isUserEventTrackerDoneTrack = true;
        }
    }
}
