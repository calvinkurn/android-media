package com.tokopedia.topads.auto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.tokopedia.topads.auto.data.TopAdsShopInfo;
import com.tokopedia.topads.auto.repository.AutoAdsRepository;

/**
 * Author errysuprayogi on 15,May,2019
 */
public class TopAdsInfoViewModel extends AndroidViewModel {

    private AutoAdsRepository repository;
    private MutableLiveData<TopAdsShopInfo> shopInfo;

    public TopAdsInfoViewModel(@NonNull Application application) {
        super(application);
        if(repository!=null){
            return;
        }
        repository = AutoAdsRepository.getInstance();
        shopInfo = repository.getAdsShopInfo();
    }

    public LiveData<TopAdsShopInfo> getShopInfo() {
        return shopInfo;
    }
}
