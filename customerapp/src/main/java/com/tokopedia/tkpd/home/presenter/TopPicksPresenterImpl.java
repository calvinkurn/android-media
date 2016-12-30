package com.tokopedia.tkpd.home.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.entity.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.entity.homeMenu.LayoutSection;
import com.tokopedia.core.network.entity.topPicks.TopPicksResponse;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.home.HomeCatMenuView;
import com.tokopedia.tkpd.home.TopPicksView;
import com.tokopedia.tkpd.home.interactor.HomeMenuInteractor;
import com.tokopedia.tkpd.home.interactor.HomeMenuInteractorImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by Alifa on 12/29/2016.
 */

public class TopPicksPresenterImpl implements TopPicksPresenter, ErrorListener {

    private final HomeMenuInteractor homeMenuInteractor;
    private TopPicksView view;

    public TopPicksPresenterImpl(TopPicksView view) {
        this.homeMenuInteractor = new HomeMenuInteractorImpl();
        this.view = view;
    }

    @Override
    public void fetchTopPicks(boolean isFromRetry) {
        Subscriber<Response<String>> subscriber = getSubcribption();
        Map<String, String> param = new HashMap<>();
        param.put("random","true");
        param.put("count", String.valueOf(1));
        param.put("item", String.valueOf(3));
        param.put("device", "android");
        param.put("source", "home");
        homeMenuInteractor.fetchTopPicksNetworkNetwork(param,subscriber);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onUnknown() {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onBadRequest() {

    }

    @Override
    public void onForbidden() {

    }

    private Subscriber<Response<String>> getSubcribption() {
        return new Subscriber<Response<String>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //handleErrorWhenGetHomeCatMenu(e);
            }

            @Override
            public void onNext(Response<String> response) {
                if (response != null && response.body() != null) {
                    TopPicksResponse topPicksResponse = new Gson().fromJson(
                            response.body(), TopPicksResponse.class);

                    ArrayList<Toppick> toppicksList = new ArrayList<>();
                    for (Toppick toppicks : topPicksResponse.getData().getToppicks()) {
                        toppicksList.add(toppicks);
                    }
                    view.renderTopPicks(toppicksList);
                } else {
                    new ErrorHandler(TopPicksPresenterImpl.this, response.code());

                }

            }
        };
    }
}
