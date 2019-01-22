package com.tokopedia.topads.sdk.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.TopAdsRouter;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent;
import com.tokopedia.topads.sdk.di.TopAdsComponent;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;
import com.tokopedia.topads.sdk.view.adapter.AdsItemDecoration;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ShopFeedViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class TopAdsView extends LinearLayout implements AdsView, LocalAdsClickListener {

    private static final String TAG = TopAdsView.class.getSimpleName();
    @Inject
    TopAdsPresenter presenter;
    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private TypedArray styledAttributes;
    private DisplayMode displayMode = DisplayMode.GRID; // Default Display Mode
    private TopAdsItemClickListener adsItemClickListener;
    private AdsItemDecoration itemDecoration;
    private RelativeLayout contentLayout;
    private static final int DEFAULT_SPAN_COUNT = 2;
    private TopAdsListener adsListener;

    public TopAdsView(Context context) {
        super(context);
        inflateView(context, null, 0);
        initInjector();
        initPresenter();
    }

    public TopAdsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
        initInjector();
        initPresenter();
    }

    public TopAdsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
        initInjector();
        initPresenter();
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopAdsView, defStyle, 0);
        inflate(getContext(), R.layout.layout_ads, this);
        adapter = new AdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
        adapter.setEnableWishlist(true);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));
        itemDecoration = new AdsItemDecoration(context.getResources()
                .getDimensionPixelSize(R.dimen.dp_16));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
    }

    public void setConfig(Config config) {
        presenter.setConfig(config);
    }

    @Override
    public void initInjector() {
        BaseMainApplication application = ((BaseMainApplication) getContext().getApplicationContext());
        TopAdsComponent component = DaggerTopAdsComponent.builder()
                .baseAppComponent(application.getBaseAppComponent())
                .build();
        component.inject(this);
        component.inject(presenter);
    }

    @Override
    public void initPresenter() {
        presenter.attachView(this);
        try {
            presenter.setMaxItems(styledAttributes.getInteger(R.styleable.TopAdsView_items, 2));
            String ep = styledAttributes.getString(R.styleable.TopAdsView_ep);
            presenter.setEndpoinParam((ep == null ? "0" : ep));
        }finally {
            styledAttributes.recycle();
        }
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        this.adsItemClickListener = adsItemClickListener;
    }

    public void setMaxItems(int items) {
        presenter.setMaxItems(items);
    }

    public void showProduct() {
        setDisplayMode(DisplayMode.GRID);
        presenter.setEndpoinParam("1");
        adapter.clearData();
        presenter.loadTopAds();
    }

    public void showShop() {
        setDisplayMode(DisplayMode.GRID);
        presenter.setEndpoinParam("2");
        adapter.clearData();
        presenter.loadTopAds();
    }

    public void loadTopAds() {
        presenter.loadTopAds();
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        switch (displayMode) {
            case GRID:
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT,
                        GridLayoutManager.VERTICAL, false));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case LIST:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case FEED:
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT,
                        GridLayoutManager.VERTICAL, false));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case FEED_EMPTY:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
        }
        this.displayMode = displayMode;
        presenter.setDisplayMode(displayMode);
        adapter.switchDisplayMode(displayMode);
    }

    @Override
    public void displayAds(List<Item> list, int position) {
        adapter.setList(list);
        if (adsListener != null && list.size() > 0) {
            adsListener.onTopAdsLoaded(list);
        }
    }

    @Override
    public void onShopItemClicked(int position, Data data) {
        Shop shop = data.getShop();
        shop.setAdRefKey(data.getAdRefKey());
        shop.setAdId(data.getId());
        presenter.openShopTopAds(position, data.getShopClickUrl(), shop);
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        Product product = data.getProduct();
        product.setAdRefKey(data.getAdRefKey());
        product.setAdId(data.getId());
        presenter.openProductTopAds(position, data.getProductClickUrl(), product);
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onAddFavorite(position, dataShop);
        }
    }

    @Override
    public void onAddWishLish(int position, Data data) {
        if(data.getProduct().isWishlist()){
            presenter.removeWishlist(data);
        } else {
            presenter.addWishlist(data);
        }
    }

    @Override
    public void notifyProductClickListener(int position, Product product) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onProductItemClicked(position, product);
        }
    }

    @Override
    public void notifyShopClickListener(int position, Shop shop) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onShopItemClicked(position, shop);
        }
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    public void setFavoritedShop(int position, boolean b) {
        Item item = adapter.getItem(position);
        if (item instanceof ShopFeedViewModel) {
            ((ShopFeedViewModel) item).getData().setFavorit(b);
        }
        if (item instanceof ShopGridViewModel) {
            ((ShopGridViewModel) item).getData().setFavorit(b);
        }
        if (item instanceof ShopListViewModel) {
            ((ShopListViewModel) item).getData().setFavorit(b);
        }
        adapter.notifyItemChanged(position);
    }

    @Override
    public String getString(int resId) {
        return getContext().getString(resId);
    }

    @Override
    public void doLogin() {
        Intent intent = ((TopAdsRouter) getContext().getApplicationContext()).getLoginIntent(getContext());
        getContext().startActivity(intent);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSuccessAddWishlist() {
        SnackbarManager.makeGreen(getRootView().findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorAddWishlist() {
        SnackbarManager.makeRed(getRootView().findViewById(android.R.id.content), getString(R.string.msg_error_add_wishlist),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessRemoveWishlist() {
        SnackbarManager.makeGreen(getRootView().findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorRemoveWishlist() {
        SnackbarManager.makeRed(getRootView().findViewById(android.R.id.content), getString(R.string.msg_error_remove_wishlist),
                Snackbar.LENGTH_LONG).show();
    }
}
