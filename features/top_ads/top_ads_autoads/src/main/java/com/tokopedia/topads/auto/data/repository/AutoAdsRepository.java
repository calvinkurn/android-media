package com.tokopedia.topads.auto.data.repository;

import android.arch.lifecycle.MutableLiveData;

import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAdsResponse;
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfoResponse;
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfoResponse;

/**
 * Author errysuprayogi on 15,May,2019
 */
public class AutoAdsRepository {

    private static AutoAdsRepository instance;
    private TopAdsShopInfoResponse adsShopInfo;
    private TopadsBidInfoResponse bidInfo;
    private TopAdsAutoAdsResponse autoAds;

    public static AutoAdsRepository getInstance() {
        if(instance == null){
            instance = new AutoAdsRepository();
        }
        return instance;
    }

    public MutableLiveData<TopAdsShopInfoResponse> getAdsShopInfo() {
        MutableLiveData<TopAdsShopInfoResponse> data = new MutableLiveData<>();
        data.setValue(adsShopInfo);
        return data;
    }

    public void setAdsShopInfo(TopAdsShopInfoResponse adsShopInfo) {
        this.adsShopInfo = adsShopInfo;
    }

    public MutableLiveData<TopadsBidInfoResponse> getBidInfo() {
        MutableLiveData<TopadsBidInfoResponse> data = new MutableLiveData<>();
        data.setValue(bidInfo);
        return data;
    }

    public void setBidInfo(TopadsBidInfoResponse bidInfo) {
        this.bidInfo = bidInfo;
    }

    public MutableLiveData<TopAdsAutoAdsResponse> getAutoAds() {
        MutableLiveData<TopAdsAutoAdsResponse> data = new MutableLiveData<>();
        data.setValue(autoAds);
        return data;
    }

    public void setAutoAds(TopAdsAutoAdsResponse autoAds) {
        this.autoAds = autoAds;
    }
}
