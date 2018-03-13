package com.tokopedia.topads.sdk.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.interactor.MerlinRecomendationUseCase;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.PreferedCategoryUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsUseCase;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.PreferedCategoryListener;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsPresenter implements AdsPresenter, PreferedCategoryListener {

    private static final String TAG = "TopAdsPresenter";
    private AdsView adsView;
    private Context context;
    private TopAdsUseCase adsUseCase;
    private OpenTopAdsUseCase openTopAdsUseCase;
    private PreferedCategoryUseCase preferedCategoryUseCase;
    private MerlinRecomendationUseCase merlinRecomendationUseCase;
    private TopAdsParams adsParams;
    private Config config;

    public TopAdsPresenter(Context context) {
        this.context = context;
        this.adsUseCase = new TopAdsUseCase(context);
        this.openTopAdsUseCase = new OpenTopAdsUseCase(context);
        CacheHandler cacheHandler = new CacheHandler(context, CacheHandler.TOP_ADS_CACHE);
        this.preferedCategoryUseCase = new PreferedCategoryUseCase(context, this, cacheHandler);
        this.merlinRecomendationUseCase = new MerlinRecomendationUseCase(context, this);
        this.adsParams = new TopAdsParams();
    }

    @Override
    public void onSuccessLoadPrefered(int randomCategoryId) {
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, String.valueOf(randomCategoryId));
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, config.getUserId());
        adsUseCase.setConfig(config);
        adsUseCase.execute(adsParams, adsView);
    }

    @Override
    public void onErrorLoadPrefed() {

    }

    public void setConfig(Config config) {
        this.config = config;
        this.adsParams = config.getTopAdsParams();
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public void setMaxItems(int items) {
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, String.valueOf(items));
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        adsUseCase.setDisplayMode(displayMode);
        config.setDisplayMode(displayMode);
    }

    public DisplayMode getDisplayMode() {
        return adsUseCase.getDisplayMode();
    }

    @Override
    public void setEndpoinParam(String ep) {
        switch (ep) {
            case "0":
                adsParams.getParam().remove(TopAdsParams.KEY_EP);
                break;
            case "1":
                adsParams.getParam().put(TopAdsParams.KEY_EP, "product");
                break;
            case "2":
                adsParams.getParam().put(TopAdsParams.KEY_EP, "shop");
                break;
        }
    }

    @Override
    public void loadTopAds() {
        Log.d(TAG, "Load TopAds");
        replaceSourceParams();
        if (config.getEndpoint() != null) {
            setEndpoinParam(config.getEndpoint().getDescription());
        }
        if (config.isWithPreferedCategory()) {
            getPreferedCategory();
        } else if(config.isWithMerlinCategory()){
            getMerlinCategory();
        } else {
            adsParams.getParam().put(TopAdsParams.KEY_USER_ID, config.getUserId());
            adsUseCase.setConfig(config);
            adsUseCase.execute(adsParams, adsView);
        }
    }

    private void replaceSourceParams() {
        if (adsParams.getParam().containsKey(TopAdsParams.KEY_SRC)) {
            if (adsParams.getParam().get(TopAdsParams.KEY_SRC).contains(TopAdsParams.DEFAULT_KEY_SRC)) {
                adsParams.getParam().put(TopAdsParams.KEY_SRC, TopAdsParams.DEFAULT_KEY_SRC);
            }
            if (adsParams.getParam().get(TopAdsParams.KEY_SRC).contains("hot_product")) {
                adsParams.getParam().put(TopAdsParams.KEY_SRC, "hotlist");
            }
        }
    }


    @Override
    public void openProductTopAds(String click_url, final Product product) {
        openTopAdsUseCase.execute(click_url, adsView);
        adsView.notifyProductClickListener(product);
    }

    @Override
    public void openShopTopAds(String click_url, final Shop shop) {
        openTopAdsUseCase.execute(click_url, adsView);
        adsView.notifyShopClickListener(shop);
    }

    @Override
    public TopAdsParams getTopAdsParam() {
        return adsParams;
    }

    @Override
    public void setParams(TopAdsParams adsParams) {
        this.adsParams = adsParams;
    }

    @Override
    public void attachView(AdsView view) {
        this.adsView = view;
    }

    @Override
    public void detachView() {
        this.adsView = null;
        adsUseCase.unsubscribe();
        openTopAdsUseCase.unsubscribe();
    }

    @Override
    public boolean isViewAttached() {
        return adsView != null;
    }

    @Override
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new AdsViewNotAttachedException();
        }
    }

    public static class AdsViewNotAttachedException extends RuntimeException {
        public AdsViewNotAttachedException() {
            super("Please call Presenter.attachView() before " +
                    "requesting data to the presenter");
        }
    }

    @Override
    public void getPreferedCategory() {
        preferedCategoryUseCase.setConfig(config);
        preferedCategoryUseCase.execute(adsParams, adsView);
    }

    @Override
    public void getMerlinCategory() {
        merlinRecomendationUseCase.setConfig(config);
        merlinRecomendationUseCase.execute(adsParams, adsView);
    }
}
