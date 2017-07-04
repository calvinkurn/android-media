package com.tokopedia.tkpd.home.interactor;


import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;


/**
 * @author Kulomady on 10/3/16.
 */

public interface HomeMenuInteractor {

    void fetchHomeCategoryMenuFromNetwork(String userId, Subscriber<Response<String>> networksubscriber);

    void fetchHomeCategoryMenuFromDb(OnFetchHomeCategoryMenuFromDbListener listener);

    void removeSubscription();

    interface OnFetchHomeCategoryMenuFromDbListener {
        void onSuccessFetchHomeCategoryListFromDb(List<CategoryMenuModel> categoryMenuModelList);

        void onErrorFetchHomeCategoryListFromDb(Throwable throwable);
    }

    void fetchTopPicksNetworkNetwork(Map<String, String> params, Subscriber<Response<String>> networksubscriber);

    void fetchBrands(String params, Subscriber<Response<Brands>> brandsSubscriber);


}
