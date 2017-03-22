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
import com.tokopedia.core.util.RetryHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.di.component.DaggerFavoriteComponent;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteAdapter;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteAdapterTypeFactory;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.tkpd.home.util.DefaultRetryListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tokopedia.tkpd.home.favorite.view.FavoriteView.DURATION_ANIMATOR;

/**
 * @author Kulomady on 1/20/17.
 */

public class FragmentFavorite extends BaseDaggerFragment
        implements FavoriteContract.View, DefaultRetryListener.OnClickRetry, FavoriteClickListener {

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
    RetryHandler retryHandlerFull;

    @Inject
    FavoritePresenter mFavoritePresenter;

    public static final String WISHLISH_EXTRA_KEY = "DomainWishlist";
    private Unbinder unbinder;
    private FavoriteAdapter favoriteAdapter;
    private EndlessRecyclerviewListener scrollListener;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_FAVORITE_SHOP;
    }

    @Override
    public void onRetryFull() {

    }

    @Override
    public void onRetryFooter() {

    }

    @Override
    public void addTopAdsShop(TopAdsShopViewModel shopViewModel) {
        if (favoriteAdapter.getItemCount() > 0) {
            favoriteAdapter.setElement(1, shopViewModel);
        }
    }


    @Override
    public void addAllDataFavorite(List<Visitable> elementList, boolean clearData) {
        favoriteAdapter.addElement(elementList, clearData);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        swipeToRefresh.setRefreshing(refreshing);
        if (!refreshing) {
            scrollListener.resetState();
        }
    }

    @Override
    public void showErrorLoadMore() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                favoriteAdapter.hideLoading();
                mFavoritePresenter.loadOnMore();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void showErrorLoadData() {
        NetworkErrorHelper.showEmptyState(getContext(), mainContent, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mFavoritePresenter.loadOnRefresh();
            }
        });
    }

    @Override
    public boolean isLoading() {
        return favoriteAdapter.isLoading();
    }


    @Override
    public void showLoading() {
        favoriteAdapter.showLoading();
    }

    @Override
    public void addFavoriteShop(FavoriteShopViewModel shopViewModel) {
        favoriteAdapter.setElement(2, shopViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntentExtra();
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

    private void initIntentExtra() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_index_favorite_v2, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        prepareView(parentView);
        setListener();
        mFavoritePresenter.attachView(this);
        mFavoritePresenter.loadDataWishlistAndFavorite();
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
        mFavoritePresenter.onSaveDataBeforeRotate(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mFavoritePresenter.onViewStateRestored(savedInstanceState);
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
                ScreenTracking.screen(getScreenName());
                mFavoritePresenter.loadDataTopAdsShop();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            onCreate(new Bundle());
        }

    }

    protected void setListener() {
        retryHandlerFull.setOnRetryListener(
                new DefaultRetryListener(DefaultRetryListener.RETRY_FULL, this));

        recyclerView.addOnScrollListener(scrollListener);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFavoritePresenter.loadOnRefresh();
            }
        });
    }

    @Override
    public void onFavClicked(View view, TopAdsShopItem shopItem) {
        mFavoritePresenter.setFavoriteShop(view, shopItem);
    }


    private void prepareView(View parentView) {

        FavoriteAdapterTypeFactory typeFactoryForList = new FavoriteAdapterTypeFactory(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favoriteAdapter = new FavoriteAdapter(typeFactoryForList, new ArrayList<Visitable>());
        animator = new DefaultItemAnimator();
        animator.setAddDuration(DURATION_ANIMATOR);
        scrollListener = new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mFavoritePresenter.loadOnMore();
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(favoriteAdapter);
        retryHandlerFull = new RetryHandler(getActivity(), parentView);
        retryHandlerFull.setRetryView();
    }


}
