package com.tokopedia.core.home.interactor;

import com.tokopedia.core.home.model.homeMenu.CategoryMenuModel;
import com.tokopedia.core.home.model.homeMenu.HomeCategoryMenuItem;

import java.util.List;

import retrofit2.Response;
import rx.Subscriber;


/**
 * @author Kulomady on 10/3/16.
 */

public interface HomeMenuInteractor {

    void fetchHomeCategoryMenuFromNetwork(Subscriber<Response<HomeCategoryMenuItem>> networksubscriber);

    void fetchHomeCategoryMenuFromDb(OnFetchHomeCategoryMenuFromDbListener listener);

    void removeSubscription();

    interface OnFetchHomeCategoryMenuFromDbListener {
        void onSuccessFetchHomeCategoryListFromDb(List<CategoryMenuModel> categoryMenuModelList);

        void onErrorFetchHomeCategoryListFromDb(Throwable throwable);
    }

}
