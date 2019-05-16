package com.tokopedia.topads.auto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfoResponse;
import com.tokopedia.topads.auto.data.repository.AutoAdsRepository;

/**
 * Author errysuprayogi on 15,May,2019
 */
public class TopAdsInfoViewModel extends AndroidViewModel {

    private AutoAdsRepository repository;
    private MutableLiveData<TopAdsShopInfoResponse> shopInfo;

    public TopAdsInfoViewModel(@NonNull Application application) {
        super(application);
        if(repository!=null){
            return;
        }
        repository = AutoAdsRepository.getInstance();
        shopInfo = repository.getAdsShopInfo();
    }

    public LiveData<TopAdsShopInfoResponse> getShopInfo() {
        return shopInfo;
    }
}
