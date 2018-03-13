package com.tokopedia.topads.sdk.presenter;

import android.content.Context;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.interactor.BannerAdsUseCase;
import com.tokopedia.topads.sdk.view.BannerAdsContract;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class BannerAdsPresenter implements BannerAdsContract.Presenter {

    private final Context context;
    private BannerAdsContract.View view;
    private BannerAdsUseCase bannerAdsUseCase;
    private TopAdsParams adsParams;
    private Config config;

    public BannerAdsPresenter(Context context) {
        this.context = context;
        this.bannerAdsUseCase = new BannerAdsUseCase(context);
        this.adsParams = new TopAdsParams();
    }

    @Override
    public void attachView(BannerAdsContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        bannerAdsUseCase.unsubscribe();
    }

    @Override
    public boolean isViewAttached() {
        return this.view != null;
    }

    @Override
    public void setParams(TopAdsParams adsParams) {
        this.adsParams = adsParams;
    }

    @Override
    public void loadTopAds() {
        bannerAdsUseCase.setConfig(config);
        bannerAdsUseCase.execute(adsParams, view);
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
        if(config.getEndpoint() == Endpoint.CPM) {
            this.config.getTopAdsParams().getParam().put(TopAdsParams.KEY_TEMPLATE_ID, "3,4");
            this.config.getTopAdsParams().getParam().put(TopAdsParams.KEY_EP, "cpm");
        }
        this.adsParams = config.getTopAdsParams();
    }
}
