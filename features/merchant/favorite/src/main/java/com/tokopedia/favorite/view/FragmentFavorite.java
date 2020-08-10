package com.tokopedia.favorite.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.favorite.R;
import com.tokopedia.favorite.di.component.FavoriteComponent;
import com.tokopedia.favorite.di.component.DaggerFavoriteComponent;
import com.tokopedia.favorite.view.adapter.FavoriteAdapter;
import com.tokopedia.favorite.view.adapter.FavoriteAdapterTypeFactory;
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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

    private static final String LOGIN_STATUS = "logged_in_status";
    private static final String IS_FAVORITE_EMPTY = "is_favorite_empty";
    private static final String OPEN_FAVORITE = "Favorite_Screen_Launched";
    private static final String SCREEN_UNIFY_HOME_SHOP_FAVORIT = "/fav-shop";

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

    @Inject
    FavoriteViewModel viewModel;

    private FavoriteAdapter favoriteAdapter;
    private EndlessRecyclerViewScrollListener recylerviewScrollListener;
    private SnackbarRetry messageSnackbar;
    private boolean isFavoriteShopNetworkFailed;
    private boolean isTopAdsShopNetworkFailed;
    private View favoriteShopViewSelected;
    private TopAdsShopItem shopItemSelected;
    private PerformanceMonitoring performanceMonitoring;
    private UserSessionInterface userSession;

    public static Fragment newInstance() {
        return new FragmentFavorite();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        userSession = new UserSession(getContext());
        performanceMonitoring = PerformanceMonitoring.start(FAVORITE_TRACE);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.favorite_fragment_index_favorite_v2, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.index_favorite_recycler_view);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        progressBar = (ProgressBar) parentView.findViewById(R.id.include_loading);
        mainContent = (RelativeLayout) parentView.findViewById(R.id.main_content);
        wishlistNotLoggedIn = parentView.findViewById(R.id.partial_empty_wishlist);
        btnLogin = parentView.findViewById(R.id.btn_login);

        if (userSession.isLoggedIn()) {
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
                    Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
                    startActivity(intent);
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
                TrackApp.getInstance().getGTM().sendScreenAuthenticated(getScreenName());
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
        updateEndlessRecyclerViewListener();
    }

    @Override
    protected void initInjector() {
        FavoriteComponent daggerFavoriteComponent
                = DaggerFavoriteComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .build();
        daggerFavoriteComponent.inject(this);
    }

    public BaseAppComponent getBaseAppComponent() {
        return ((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent();
    }

    @Override
    protected String getScreenName() {
        return SCREEN_UNIFY_HOME_SHOP_FAVORIT;
    }

    @Override
    public void refreshDataFavorite(List<Visitable> elementList) {
        favoriteAdapter.hideLoading();
        favoriteAdapter.clearData();
        favoriteAdapter.setElement(elementList);
        updateEndlessRecyclerViewListener();
    }

    @Override
    public void showInitialDataPage(List<Visitable> dataFavorite) {
        sendFavoriteShopScreenTracker(dataFavorite.isEmpty());
        favoriteAdapter.hideLoading();
        favoriteAdapter.clearData();
        favoriteAdapter.setElement(dataFavorite);
        updateEndlessRecyclerViewListener();
        Map<String, Object> value = DataLayer.mapOf(
                LOGIN_STATUS, userSession.isLoggedIn(),
                IS_FAVORITE_EMPTY, dataFavorite.size() == 0
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, OPEN_FAVORITE);
    }

    @Override
    public void showMoreDataFavoriteShop(List<Visitable> elementList) {
        favoriteAdapter.hideLoading();
        favoriteAdapter.addMoreData(elementList);
        updateEndlessRecyclerViewListener();
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
                () -> {
                    favoritePresenter.loadMoreFavoriteShop();
                }).showRetrySnackbar();
        favoriteAdapter.hideLoading();
        updateEndlessRecyclerViewListener();
    }

    @Override
    public void showErrorLoadData() {
        if (isAdapterNotEmpty()) {
            showTopadsShopFailedMessage();
            validateMessageError();
        } else {
            NetworkErrorHelper.showEmptyState(getContext(),
                    mainContent,
                    () -> favoritePresenter.refreshAllDataFavoritePage());
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
    public void sendFavoriteShopImpression(String clickUrl) {
        new ImpresionTask(getActivity().getClass().getSimpleName(), userSession).execute(clickUrl);
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
        recylerviewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
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

    private void updateEndlessRecyclerViewListener() {
        recylerviewScrollListener.updateStateAfterGetData();
        recylerviewScrollListener.setHasNextPage(favoritePresenter.hasNextPage());
    }
}
