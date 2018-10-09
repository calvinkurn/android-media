package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.keyword.domain.interactor.TopAdsOldKeywordActionBulkUseCase;
import com.tokopedia.topads.keyword.domain.interactor.TopAdsKeywordGetDetailUseCase;
import com.tokopedia.topads.keyword.domain.model.KeywordDetailDomain;
import com.tokopedia.topads.keyword.view.mapper.TopAdsKeywordDetailMapperView;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailViewListener;

import java.util.Date;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailPresenterImpl extends TopadsKeywordDetailPresenter<TopAdsDetailViewListener> {

    private final TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase;
    private final TopAdsOldKeywordActionBulkUseCase topAdsOldKeywordActionBulkUseCase;

    public TopAdsKeywordDetailPresenterImpl(TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase,
                                            TopAdsOldKeywordActionBulkUseCase topAdsOldKeywordActionBulkUseCase) {
        this.topAdsKeywordGetDetailUseCase = topAdsKeywordGetDetailUseCase;
        this.topAdsOldKeywordActionBulkUseCase = topAdsOldKeywordActionBulkUseCase;
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, String id, int isPositive, String shopId) {
        topAdsKeywordGetDetailUseCase.execute(TopAdsKeywordGetDetailUseCase.createRequestParams(startDate,
                endDate, id, isPositive, shopId), getSubscriberRefreshAd());
    }

    @Override
    public void deleteAd(String id, String groupId, String shopId) {
        topAdsOldKeywordActionBulkUseCase.execute(TopAdsOldKeywordActionBulkUseCase.createRequestParams(id, groupId,
                shopId, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD),
                getSubscriberDeleteAd());
    }

    @Override
    public void turnOnAd(String id, String groupId, String shopID) {
        topAdsOldKeywordActionBulkUseCase.execute(TopAdsOldKeywordActionBulkUseCase.createRequestParams(id, groupId,
                shopID, TopAdsNetworkConstant.ACTION_BULK_ON_AD),
                getSubscriberTurnOnAd());
    }

    @Override
    public void turnOffAd(String id, String groupId, String shopID) {
        topAdsOldKeywordActionBulkUseCase.execute(TopAdsOldKeywordActionBulkUseCase.createRequestParams(id, groupId,
                shopID, TopAdsNetworkConstant.ACTION_BULK_OFF_AD),
                getSubscriberTurnOffAd());
    }

    private Subscriber<KeywordDetailDomain> getSubscriberRefreshAd() {
        return new Subscriber<KeywordDetailDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onLoadAdError();
                }
            }

            @Override
            public void onNext(KeywordDetailDomain keywordDetailDomain) {
                getView().onAdLoaded(TopAdsKeywordDetailMapperView.mapDomainToView(keywordDetailDomain));
            }
        };
    }

    public Subscriber<Boolean> getSubscriberDeleteAd() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onDeleteAdError();
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if (isSuccess) {
                    getView().onDeleteAdSuccess();
                } else {
                    getView().onDeleteAdError();
                }
            }
        };
    }

    public Subscriber<Boolean> getSubscriberTurnOnAd() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onTurnOnAdError(e);
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if(isSuccess){
                    getView().onTurnOnAdSuccess(null);
                }else {
                    getView().onTurnOnAdError(new RuntimeException());
                }
            }
        };
    }

    public Subscriber<Boolean> getSubscriberTurnOffAd() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onTurnOffAdError();
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().onTurnOffAdSuccess(null);
            }
        };
    }

    @Override
    public void unSubscribe() {
        topAdsOldKeywordActionBulkUseCase.unsubscribe();
        topAdsKeywordGetDetailUseCase.unsubscribe();
    }
}
