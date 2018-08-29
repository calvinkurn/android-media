package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.topads.common.data.model.DataCheckPromo;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsCheckTimeAndSaveSourceTaggingUseCase;
import com.tokopedia.topads.common.domain.interactor.TopAdsCheckProductPromoUseCase;
import com.tokopedia.topads.dashboard.view.listener.TopAdsCheckProductPromoView;

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
                .createRequestParams(source), new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                //do nothing
            }
        });
    }

    public void checkAndSaveSource(){
        checkAndSaveSourceTaggingUseCase.execute(TopAdsCheckTimeAndSaveSourceTaggingUseCase
                        .createRequestParams(TopAdsSourceOption.APPLINK),
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
        useCase.execute(TopAdsCheckProductPromoUseCase.createRequestParams(shopId, itemId, userId),
                new Subscriber<DataCheckPromo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().finishLoadingProgress();
                    getView().renderErrorView(e);
                }
            }

            @Override
            public void onNext(DataCheckPromo dataCheckPromo) {
                if (isViewAttached()) {
                    if (dataCheckPromo.getId().equalsIgnoreCase(IS_UNPROMOTED_PRODUCT)) {
                        getView().moveToCreateAds();
                    } else {
                        getView().moveToAdsDetail(dataCheckPromo.getId());
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
