package com.tokopedia.topads.dashboard.view.presenter;

import android.util.Log;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.topads.common.constant.TopAdsSourceOption;
import com.tokopedia.topads.common.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.common.domain.interactor.TopAdsCheckAndSaveSourceTaggingUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCheckProductPromoUseCase;
import com.tokopedia.topads.dashboard.view.listener.TopAdsCheckProductPromoView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsCheckProductPromoPresenter implements CustomerPresenter<TopAdsCheckProductPromoView> {
    private final TopAdsCheckProductPromoUseCase useCase;
    private final TopAdsCheckAndSaveSourceTaggingUseCase checkAndSaveSourceTaggingUseCase;
    private final TopAdsAddSourceTaggingUseCase adsAddSourceTaggingUseCase;
    private TopAdsCheckProductPromoView view;
    private static final String IS_UNPROMOTED_PRODUCT = "0";

    @Inject
    public TopAdsCheckProductPromoPresenter(TopAdsCheckProductPromoUseCase useCase,
                                            TopAdsCheckAndSaveSourceTaggingUseCase topAdsCheckAndSaveSourceTaggingUseCase,
                                            TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase) {
        this.useCase = useCase;
        this.checkAndSaveSourceTaggingUseCase = topAdsCheckAndSaveSourceTaggingUseCase;
        this.adsAddSourceTaggingUseCase = topAdsAddSourceTaggingUseCase;
    }

    public void save(String source){
        adsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase
                .createRequestParams(source, DateFormat.getDateTimeInstance().format(new Date())), new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {

            }
        });
    }

    public void checkAndSaveSource(){
        checkAndSaveSourceTaggingUseCase.execute(TopAdsCheckAndSaveSourceTaggingUseCase
                        .createRequestParams(TopAdsSourceOption.APPLINK, DateFormat.getDateTimeInstance().format(new Date())),
                new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    public void checkPromoAds(String shopId, String itemId, String userId){
        useCase.execute(TopAdsCheckProductPromoUseCase.createRequestParams(shopId, itemId, userId), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MessageErrorException) {
                    view.renderErrorView(e.getMessage());
                } else if (e instanceof RuntimeHttpErrorException) {
                    view.renderErrorView(e.getMessage());
                } else if (e instanceof IOException) {
                    view.renderRetryRefresh();
                } else {
                    view.renderErrorView(null);
                }
                view.finishLoadingProgress();
            }

            @Override
            public void onNext(String s) {
                view.finishLoadingProgress();
                if (s.equalsIgnoreCase(IS_UNPROMOTED_PRODUCT)){
                    view.moveToCreateAds();
                } else {
                    view.moveToAdsDetail(s);
                }
            }
        });
    }

    @Override
    public void attachView(TopAdsCheckProductPromoView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view =  null;
    }
}
