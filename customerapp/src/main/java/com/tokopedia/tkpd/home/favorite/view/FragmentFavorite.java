package com.tokopedia.tkpd.home.favorite.view;

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

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.di.component.DaggerFavoriteComponent;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteAdapter;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteAdapterTypeFactory;
import com.tokopedia.tkpd.home.favorite.view.adapter.viewholders.EmptyWishslistHolder;
import com.tokopedia.tkpd.home.favorite.view.adapter.viewholders.WishlistViewHolder;
import com.tokopedia.tkpd.home.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Kulomady on 1/20/17.
 */

public class FragmentFavorite extends BaseDaggerFragment
        implements FavoriteContract.View, FavoriteClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.index_favorite_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;
    @BindView(R.id.include_loading)
    ProgressBar progressBar;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    LinearLayoutManager layoutManager;
    DefaultItemAnimator animator;

    @Inject
    FavoritePresenter favoritePresenter;

    private static final long DURATION_ANIMATOR = 1000;
    private Unbinder unbinder;
    private FavoriteAdapter favoriteAdapter;
    private EndlessRecyclerviewListener recylerviewScrollListener;

    private SnackbarRetry messageSnackbar;
    private boolean isHasToShowMessageFailed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_index_favorite_v2, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        prepareView();
        favoritePresenter.attachView(this);
        favoritePresenter.loadDataWishlistAndFavorite();
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
                if (messageSnackbar != null && isHasToShowMessageFailed) {
                    messageSnackbar.showRetrySnackbar();
                }
                ScreenTracking.screen(getScreenName());
                favoritePresenter.loadDataTopAdsShop();
            } else {
                if (messageSnackbar != null) {
                    messageSnackbar.hideRetrySnackbar();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            onCreate(new Bundle());
        }

    }

    @Override
    protected void initInjector() {
        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .activityModule(new ActivityModule(getActivity()))
                .build();
        DaggerFavoriteComponent daggerFavoriteComponent
                = (DaggerFavoriteComponent) DaggerFavoriteComponent.builder()
                .appComponent(daggerAppComponent)
                .build();
        daggerFavoriteComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_FAVORITE_SHOP;
    }

    @Override
    public void addTopAdsShop(TopAdsShopViewModel shopViewModel) {
        final int indexFirstAdapter = 0;
        final int indexSecondAdapater = 1;
        if (isAdapterNotEmpty()) {
            if (isFirstPositonWishlist(indexFirstAdapter)) {
                if (favoriteAdapter.getItemCount() >= 2) {
                    favoriteAdapter.setElement(indexSecondAdapater, shopViewModel);
                } else {
                    favoriteAdapter.addElement(shopViewModel);
                }
            } else {
                favoriteAdapter.setElement(indexFirstAdapter, shopViewModel);
            }
        } else {
            favoriteAdapter.setElement(indexFirstAdapter, shopViewModel);
        }
    }


    @Override
    public void showDataFavorite(List<Visitable> elementList) {
        favoriteAdapter.hideLoading();
        favoriteAdapter.clearData();
        favoriteAdapter.setElement(elementList);
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
        NetworkErrorHelper.showEmptyState(getContext(),
                mainContent,
                new NetworkErrorHelper.RetryClickedListener() {

                    @Override
                    public void onRetryClicked() {
                        favoritePresenter.refreshAllDataFavoritePage();
                    }
                });
    }

    @Override
    public void hasToShowWishlistFailedMessage() {
        isHasToShowMessageFailed = true;
    }

    @Override
    public void hasToDismissWishlistFailedMessage() {
        isHasToShowMessageFailed = false;
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
        favoriteAdapter.setElement(favoriteShopPosition, shopViewModel);
    }

    @Override
    public void onRefresh() {
        favoritePresenter.refreshAllDataFavoritePage();
    }

    @Override
    public void onFavoriteShopClicked(View view, TopAdsShopItem shopItem) {
        favoritePresenter.addFavoriteShop(view, shopItem);
    }

    private void prepareView() {

        FavoriteAdapterTypeFactory typeFactoryForList = new FavoriteAdapterTypeFactory(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favoriteAdapter = new FavoriteAdapter(typeFactoryForList, new ArrayList<Visitable>());
        animator = new DefaultItemAnimator();
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
        swipeToRefresh.setOnRefreshListener(this);

        messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        favoritePresenter.refreshAllDataFavoritePage();
                    }
                });

    }

    private boolean isFirstPositonWishlist(int indexFirstAdapter) {
        return favoriteAdapter.getItemViewType(indexFirstAdapter) == WishlistViewHolder.LAYOUT
                || favoriteAdapter.getItemViewType(indexFirstAdapter) == EmptyWishslistHolder.LAYOUT;
    }

    private boolean isAdapterNotEmpty() {
        return favoriteAdapter.getItemCount() > 0;
    }


}
