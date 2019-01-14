package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.TopAdsRouter;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.ObserverType;
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent;
import com.tokopedia.topads.sdk.di.TopAdsComponent;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ClientViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by errysuprayogi on 4/18/17.
 */

public class TopAdsPlacer implements AdsView, LocalAdsClickListener {

    private static final String TAG = TopAdsPlacer.class.getSimpleName();
    private int ajustedPositionStart = 0;
    private int ajustedItemCount = 0;
    private int observerType;
    private TopAdsListener topAdsListener;
    private TopAdsItemClickListener adsItemClickListener;
    private DataObserver observer;
    private List<Item> itemList = new ArrayList<>();
    private List<Item> adsItems = Collections.emptyList();
    private int mPage = 1;
    private boolean hasHeader = false;
    private int headerCount = 0;
    private boolean headerPlaced = false;
    private boolean isFeed = false;
    private boolean shouldLoadAds = true; //default load ads
    private final TopAdsRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private static final int ROW_ADS_INDEX_FEED = 2;
    private Context context;

    @Inject
    TopAdsPresenter presenter;

    public TopAdsPlacer(TopAdsRecyclerAdapter adapter, Context context,
                        TopAdsAdapterTypeFactory typeFactory, DataObserver observer) {
        this.context = context;
        this.adapter = adapter;
        this.observer = observer;
        typeFactory.setItemClickListener(this);
        initInjector();
        initPresenter();
    }

    public void setAjustedItemCount(int ajustedItemCount) {
        this.ajustedItemCount = ajustedItemCount;
    }

    public int getPositionStart(int positionStart) {
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if (item instanceof ClientViewModel && item.originalPos() == positionStart) {
                positionStart = i;
                break;
            }
        }
        return positionStart;
    }

    public int getAjustedPositionStart() {
        return ajustedPositionStart;
    }

    public int getAjustedItemCount() {
        return ajustedItemCount;
    }

    public void setTopAdsListener(TopAdsListener topAdsListener) {
        this.topAdsListener = topAdsListener;
    }

    public void setShouldLoadAds(boolean shouldLoadAds) {
        this.shouldLoadAds = shouldLoadAds;
    }

    public void onChanged() {
        reset();
        observerType = ObserverType.CHANGE;
        if (shouldLoadAds && adsItems.isEmpty()) {
            loadTopAds();
        }
        renderItem(ajustedPositionStart, ajustedItemCount);
    }

    public void onItemRangeInserted(final int positionStart, final int itemCount) {
        Log.d(TAG, "onItemRangeInserted positionStart " + positionStart + " itemCount " + itemCount);
        ajustedPositionStart = positionStart;
        ajustedItemCount = itemCount;
        observerType = ObserverType.ITEM_RANGE_INSERTED;
        if (shouldLoadAds) {
            loadTopAds();
        }
        renderItem(ajustedPositionStart, (ajustedPositionStart + ajustedItemCount));
    }

    @Override
    public void initInjector() {
        BaseMainApplication application = ((BaseMainApplication) context.getApplicationContext());
        TopAdsComponent component = DaggerTopAdsComponent.builder()
                .baseAppComponent(application.getBaseAppComponent())
                .build();
        component.inject(this);
        component.inject(presenter);
    }

    @Override
    public void initPresenter() {
        presenter.attachView(this);
    }

    public void setMaxItems(int items) {
        presenter.setMaxItems(items);
    }

    public void setHasHeader(boolean hasHeader, int headerCount) {
        this.hasHeader = hasHeader;
        this.headerCount = headerCount;
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        presenter.setDisplayMode(displayMode);
        for (Item visitable : itemList) {
            if (visitable instanceof TopAdsViewModel) {
                TopAdsViewModel topAdsViewModel = (TopAdsViewModel) visitable;
                topAdsViewModel.switchDisplayMode(displayMode);
            }
        }
    }

    public DisplayMode getDisplayMode() {
        return presenter.getDisplayMode();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    public int getItemCount() {
        return itemList.size();
    }

    public int getPage() {
        return mPage;
    }

    public void reset() {
        ajustedPositionStart = 0;
        mPage = 1;
        itemList.clear();
        if (hasHeader)
            headerPlaced = false;
    }

    public void clearAds() {
        adsItems.clear();
    }

    public void loadTopAds() {
        presenter.getTopAdsParam().setAdsPosition(ajustedPositionStart == 0 ? ajustedPositionStart : getItemCount());
        presenter.getTopAdsParam().getParam().put(TopAdsParams.KEY_PAGE, String.valueOf(mPage));
        presenter.loadTopAds();
    }

    public void attachRecycleView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void displayAds(List<Item> list, int position) {
        Log.d(TAG, "displayAds list size " + list.size() + " position " + position);
        adsItems = list;
        if (isFeed) {
            int tresHold = (ajustedItemCount - 1);
            if (getItemCount() > tresHold) {
                if (hasHeader && !headerPlaced) {
                    headerPlaced = true;
                    setTopAds(adsItems, ROW_ADS_INDEX_FEED);
                } else {
                    setTopAds(adsItems, getItemCount() - tresHold);
                }
            } else {
                setTopAds(adsItems, getItemCount());
            }
        } else {
            if ((hasHeader && !headerPlaced) || (hasHeader && mPage == 1)) {
                headerPlaced = true;
                setTopAds(adsItems, headerCount);
            } else {
                setTopAds(adsItems, position);
                if (recyclerView != null && position == 0) {
                    recyclerView.scrollToPosition(position);
                }
            }
        }
        if (topAdsListener != null) {
            topAdsListener.onTopAdsLoaded(list);
        }
    }

    private void renderItem(int positionStart, int itemCount) {
        Log.d(TAG, "renderItem start " + positionStart + " item count " + itemCount);
        ArrayList<Item> arrayList = new ArrayList<>();
        for (int i = positionStart; i < itemCount; i++) {
            ClientViewModel model = new ClientViewModel();
            model.setPosition(i);
            Log.d(TAG, "add new item pos " + i);
            arrayList.add(model);
        }
        ajustedPositionStart = getItemCount();
        itemList.addAll(arrayList);
        if (hasHeader && !adsItems.isEmpty() && !headerPlaced) {
            headerPlaced = true;
            setTopAds(adsItems, headerCount);
        }
        observer.onStreamLoaded(observerType);
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        Log.e(TAG, "Ads failed to load error message " + message);
        setShouldLoadAds(false);
        if (topAdsListener != null) {
            topAdsListener.onTopAdsFailToLoad(errorCode, message);
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

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        this.adsItemClickListener = adsItemClickListener;
    }

    public void setConfig(Config config) {
        if (config.getDisplayMode() == DisplayMode.FEED) {
            isFeed = true;
        }
        presenter.setConfig(config);
    }

    public Config getConfig() {
        return presenter.getConfig();
    }

    private void setTopAds(List<Item> list, int pos) {
        Log.d(TAG, "setTopAds size " + list.size() + " pos " + pos);
        if (pos >= 0 && pos < itemList.size()) {
            if (list.size() > 0) {
                itemList.add(pos, new TopAdsViewModel(list));
                adapter.notifyItemInserted(pos);
            } else {
                setShouldLoadAds(false);
            }
        }
    }

    public void increasePage() {
        mPage++;
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

    public interface DataObserver {
        void onStreamLoaded(int type);
    }

    @Override
    public String getString(int resId) {
        return context.getString(resId);
    }

    @Override
    public void doLogin() {
        Intent intent = ((TopAdsRouter) context.getApplicationContext()).getLoginIntent(context);
        context.startActivity(intent);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSuccessAddWishlist() {
        SnackbarManager.makeGreen(recyclerView.getRootView().findViewById(android.R.id.content),
                getString(R.string.msg_success_add_wishlist),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorAddWishlist() {
        SnackbarManager.makeRed(recyclerView.getRootView().findViewById(android.R.id.content),
                getString(R.string.msg_error_add_wishlist),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessRemoveWishlist() {
        SnackbarManager.makeGreen(recyclerView.getRootView().findViewById(android.R.id.content),
                getString(R.string.msg_success_remove_wishlist),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorRemoveWishlist() {
        SnackbarManager.makeRed(recyclerView.getRootView().findViewById(android.R.id.content),
                getString(R.string.msg_error_remove_wishlist),
                Snackbar.LENGTH_LONG).show();
    }
}
