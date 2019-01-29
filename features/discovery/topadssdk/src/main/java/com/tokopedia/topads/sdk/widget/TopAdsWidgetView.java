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

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.TopAdsRouter;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent;
import com.tokopedia.topads.sdk.di.TopAdsComponent;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by errysuprayogi on 2/20/18.
 */

public class TopAdsWidgetView extends LinearLayout implements AdsView, LocalAdsClickListener {

    private static final String TAG = TopAdsWidgetView.class.getSimpleName();
    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private static final int BIG_SPAN = 1;
    private static final int GRID_SPAN = 2;
    private List<Data> data = new ArrayList<>();
    private TopAdsItemClickListener itemClickListener;
    private TopAdsListener adsListener;
    private OpenTopAdsUseCase openTopAdsUseCase;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private DisplayMode mode = DisplayMode.GRID;
    private TypedArray styledAttributes;
    private TopAdsItemImpressionListener impressionListener;

    @Inject
    TopAdsPresenter presenter;

    public TopAdsWidgetView(Context context) {
        super(context);
        inflateView(context, null, 0);
        initInjector();
        initPresenter();
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

    public TopAdsWidgetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
        initInjector();
        initPresenter();
    }

    public TopAdsWidgetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
        initInjector();
        initPresenter();
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopAdsWidgetView, defStyle, 0);
        inflate(getContext(), R.layout.layout_ads_no_padding, this);
        openTopAdsUseCase = new OpenTopAdsUseCase(context);
        adapter = new AdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
        adapter.setAdsItemImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                if(impressionListener!=null){
                    impressionListener.onImpressionProductAdsItem(position, product);
                }
            }
        });
        try {
            adapter.setEnableWishlist(styledAttributes.getBoolean(R.styleable.TopAdsWidgetView_enable_wishlist, false));
        } finally {
            styledAttributes.recycle();
        }
        gridLayoutManager = new GridLayoutManager(context, GRID_SPAN,
                        GridLayoutManager.VERTICAL, false);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration decoration) {
        recyclerView.addItemDecoration(decoration);
        notifyDataChange();
    }

    public void setData(List<Data> data) {
        this.data = data;
        List<Item> visitables = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            if (d.getProduct() != null) {
                visitables.add(ModelConverter.convertProductData(d, mode));
            } else if (d.getShop() != null) {
                visitables.add(ModelConverter.convertShopData(d, mode));
            }
        }
        adapter.setList(visitables);
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        openTopAdsUseCase.unsubscribe();
    }

    @Override
    public void onShopItemClicked(int position, Data data) {
        Shop shop = data.getShop();
        shop.setAdRefKey(data.getAdRefKey());
        shop.setAdId(data.getId());
        itemClickListener.onShopItemClicked(position, shop);
        openTopAdsUseCase.execute(data.getShopClickUrl());
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        Product product = data.getProduct();
        product.setAdRefKey(data.getAdRefKey());
        product.setAdId(data.getId());
        itemClickListener.onProductItemClicked(position, product);
        openTopAdsUseCase.execute(data.getProductClickUrl());
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        itemClickListener.onAddFavorite(position, dataShop);
    }

    @Override
    public void onAddWishLish(int position, Data data) {
        if(data.getProduct().isWishlist()){
            presenter.removeWishlist(data);
        } else {
            presenter.addWishlist(data);
        }
    }

    public void setItemClickListener(TopAdsItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setImpressionListener(TopAdsItemImpressionListener impressionListener) {
        this.impressionListener = impressionListener;
    }

    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    public void setAdapterPosition(int adapterPosition) {
        adapter.setAdapterPosition(adapterPosition);
    }

    public void setDisplayMode(DisplayMode mode) {
        this.mode = mode;
        switch (mode) {
            case BIG:
                gridLayoutManager.setSpanCount(BIG_SPAN);
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case GRID:
                gridLayoutManager.setSpanCount(GRID_SPAN);
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case LIST:
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
            case FEED:
                gridLayoutManager.setSpanCount(GRID_SPAN);
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case FEED_EMPTY:
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
        }
        adapter.switchDisplayMode(mode);
    }

    @Override
    public void initPresenter() {
        presenter.attachView(this);
    }

    @Override
    public void displayAds(List<Item> list, int position) {

    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    @Override
    public void notifyProductClickListener(int position, Product product) {
        if (itemClickListener != null) {
            itemClickListener.onProductItemClicked(position, product);
        }
    }

    @Override
    public void notifyShopClickListener(int position, Shop shop) {
        if (itemClickListener != null) {
            itemClickListener.onShopItemClicked(position, shop);
        }
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
