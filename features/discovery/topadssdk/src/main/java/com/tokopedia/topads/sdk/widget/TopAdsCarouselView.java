package com.tokopedia.topads.sdk.widget;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.TopAdsRouter;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent;
import com.tokopedia.topads.sdk.di.TopAdsComponent;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.SpacesItemDecoration;
import com.tokopedia.topads.sdk.view.TopAdsInfoBottomSheetDynamicChannel;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by errysuprayogi on 7/20/18.
 */

public class TopAdsCarouselView extends LinearLayout implements AdsView, LocalAdsClickListener, View.OnClickListener {

    private static final String TAG = TopAdsCarouselView.class.getSimpleName();
    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private TopAdsListener adsListener;
    private TopAdsItemClickListener adsItemClickListener;
    private TopAdsItemImpressionListener adsItemImpressionListener;
    private LinearLayoutManager layoutManager;
    private ImageView btnCta;
    private TypedArray styledAttributes;
    private TextView title;
    private TopAdsInfoBottomSheetDynamicChannel infoBottomSheet;

    @Inject
    TopAdsPresenter presenter;

    public TopAdsCarouselView(Context context) {
        super(context);
        inflateView(context, null, 0);
        initInjector();
        initPresenter();
    }

    public TopAdsCarouselView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
        initInjector();
        initPresenter();
    }

    public TopAdsCarouselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
        initInjector();
        initPresenter();
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopAdsCarouselView, defStyle, 0);
        inflate(context, R.layout.layout_ads_carousel, this);
        adapter = new AdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
        adapter.setEnableWishlist(true);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false);
        btnCta = findViewById(R.id.info_cta);
        btnCta.setOnClickListener(this);
        title = findViewById(R.id.title);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_5)));
        infoBottomSheet = TopAdsInfoBottomSheetDynamicChannel.newInstance(getContext());
        try {
            adapter.setImpressionOffset(styledAttributes.getDimensionPixelSize(R.styleable.TopAdsCarouselView_ads_offset, 0));
            String presetTitle = styledAttributes.getString(R.styleable.TopAdsCarouselView_ads_title);
            if (presetTitle != null)
                title.setText(presetTitle);
        } finally {
            styledAttributes.recycle();
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.info_cta) {
            infoBottomSheet.show();
        }
    }

    public void setConfig(Config config) {
        presenter.setConfig(config);
        setDisplayMode(DisplayMode.CAROUSEL);
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        this.adsItemClickListener = adsItemClickListener;
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
        presenter.setEndpoinParam("1");
    }

    public void setMaxItems(int items) {
        presenter.setMaxItems(items);
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        presenter.setDisplayMode(displayMode);
    }

    public void loadTopAds() {
        presenter.loadTopAds();
    }

    @Override
    public void displayAds(List<Item> list, int position) {
        adapter.setList(list);
        if (adsListener != null && list.size() > 0) {
            adsListener.onTopAdsLoaded(list);
        }
        if (list.isEmpty()) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    public void setData(TopAdsModel data) {
        if (data != null && data.getError() == null && data.getStatus().getErrorCode() == 0) {
            List<Item> visitables = new ArrayList<>();
            for (int i = 0; i < data.getData().size(); i++) {
                Data d = data.getData().get(i);
                if (d.getProduct() != null) {
                    visitables.add(ModelConverter.convertToCarouselListViewModel(d));
                }
            }
            adapter.setList(visitables);
            if (visitables.isEmpty()) {
                setVisibility(GONE);
            } else {
                setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
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
    public void onAddFavorite(int position, Data data) {

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

    public void setAdsItemImpressionListener(TopAdsItemImpressionListener adsItemImpressionListener) {
        this.adapter.setAdsItemImpressionListener(adsItemImpressionListener);
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
