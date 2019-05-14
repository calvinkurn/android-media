package com.tokopedia.topads.auto.repository;

import android.arch.lifecycle.MutableLiveData;

import com.tokopedia.topads.auto.data.TopAdsAutoAds;
import com.tokopedia.topads.auto.data.TopAdsShopInfo;
import com.tokopedia.topads.auto.data.TopadsBidInfo;

/**
 * Author errysuprayogi on 15,May,2019
 */
public class AutoAdsRepository {

    private static AutoAdsRepository instance;
    private TopAdsShopInfo adsShopInfo;
    private TopadsBidInfo bidInfo;
    private TopAdsAutoAds autoAds;

    public static AutoAdsRepository getInstance() {
        if(instance == null){
            instance = new AutoAdsRepository();
        }
        return instance;
    }

    public MutableLiveData<TopAdsShopInfo> getAdsShopInfo() {
        MutableLiveData<TopAdsShopInfo> data = new MutableLiveData<>();
        data.setValue(adsShopInfo);
        return data;
    }

    public void setAdsShopInfo(TopAdsShopInfo adsShopInfo) {
        this.adsShopInfo = adsShopInfo;
    }

    public MutableLiveData<TopadsBidInfo> getBidInfo() {
        MutableLiveData<TopadsBidInfo> data = new MutableLiveData<>();
        data.setValue(bidInfo);
        return data;
    }

    public void setBidInfo(TopadsBidInfo bidInfo) {
        this.bidInfo = bidInfo;
    }

    public MutableLiveData<TopAdsAutoAds> getAutoAds() {
        MutableLiveData<TopAdsAutoAds> data = new MutableLiveData<>();
        data.setValue(autoAds);
        return data;
    }

    public void setAutoAds(TopAdsAutoAds autoAds) {
        this.autoAds = autoAds;
    }
}
