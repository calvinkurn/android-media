package com.tokopedia.topads.dashboard.view.presenter;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.topads.common.constant.TopAdsSourceOption;
import com.tokopedia.topads.common.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.common.domain.interactor.TopAdsCheckTimeAndSaveSourceTaggingUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCheckProductPromoUseCase;
import com.tokopedia.topads.dashboard.view.listener.TopAdsCheckProductPromoView;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsCheckProductPromoPresenter extends BaseDaggerPresenter<TopAdsCheckProductPromoView> {
    private final TopAdsCheckProductPromoUseCase useCase;
    private final TopAdsCheckTimeAndSaveSourceTaggingUseCase checkAndSaveSourceTaggingUseCase;
    private final TopAdsAddSourceTaggingUseCase adsAddSourceTaggingUseCase;
    private static final String IS_UNPROMOTED_PRODUCT = "0";

    @Inject
    public TopAdsCheckProductPromoPresenter(TopAdsCheckProductPromoUseCase useCase,
                                            TopAdsCheckTimeAndSaveSourceTaggingUseCase topAdsCheckTimeAndSaveSourceTaggingUseCase,
                                            TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase) {
        this.useCase = useCase;
        this.checkAndSaveSourceTaggingUseCase = topAdsCheckTimeAndSaveSourceTaggingUseCase;
        this.adsAddSourceTaggingUseCase = topAdsAddSourceTaggingUseCase;
    }

    public void save(String source){
        adsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase
                .createRequestParams(source, new Date().getTime()), new Subscriber<Void>() {
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
        checkAndSaveSourceTaggingUseCase.execute(TopAdsCheckTimeAndSaveSourceTaggingUseCase
                        .createRequestParams(TopAdsSourceOption.APPLINK, new Date().getTime()),
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
                if (isViewAttached()) {
                    if (e instanceof IOException) {
                        getView().renderRetryRefresh();
                    } else {
                        getView().renderErrorView(e);
                    }
                    getView().finishLoadingProgress();
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().finishLoadingProgress();
                    if (s.equalsIgnoreCase(IS_UNPROMOTED_PRODUCT)) {
                        getView().moveToCreateAds();
                    } else {
                        getView().moveToAdsDetail(s);
                    }
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        useCase.unsubscribe();
        checkAndSaveSourceTaggingUseCase.unsubscribe();
        adsAddSourceTaggingUseCase.unsubscribe();
    }
}
