package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.topads.common.util.TopAdsSourceTaggingUseCaseUtil;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdAction;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailViewListener;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public class TopAdsDetailProductViewPresenterImpl<T extends Ad> extends TopAdsDetailProductPresenterImpl implements TopAdsDetailProductPresenter {

    private TopAdsDetailViewListener<T> topAdsDetailViewListener;
    private TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;

    public TopAdsDetailProductViewPresenterImpl(Context context, TopAdsDetailViewListener<T> topAdsDetailViewListener, TopAdsProductAdInteractor productAdInteractor) {
        super(context, topAdsDetailViewListener, productAdInteractor);
        this.topAdsDetailViewListener = topAdsDetailViewListener;
        this.topAdsAddSourceTaggingUseCase = TopAdsSourceTaggingUseCaseUtil.getTopAdsAddSourceTaggingUseCase(context);
    }

    @Override
    public void turnOnAds(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        topAdsProductAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOnAdSuccess(dataResponseActionAds);
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOnAdError(throwable);
            }
        });
    }

    @Override
    public void turnOffAds(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        topAdsProductAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOffAdSuccess(dataResponseActionAds);
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOffAdError();
            }
        });
    }

    @Override
    public void deleteAd(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        topAdsProductAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onDeleteAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onDeleteAdError();
            }
        });
    }

    public void saveSourceTagging(@TopAdsSourceOption String source){
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source),
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

    @NonNull
    private DataRequest<ProductAdBulkAction> generateActionRequest(String id, String action) {
        DataRequest<ProductAdBulkAction> dataRequest = new DataRequest<>();
        ProductAdBulkAction dataRequestSingleAd = new ProductAdBulkAction();
        dataRequestSingleAd.setAction(action);
        dataRequestSingleAd.setShopId(getShopId());
        List<ProductAdAction> dataRequestSingleAdses = new ArrayList<>();
        ProductAdAction data = new ProductAdAction();
        data.setId(id);
        dataRequestSingleAdses.add(data);
        dataRequestSingleAd.setAds(dataRequestSingleAdses);
        dataRequest.setData(dataRequestSingleAd);
        return dataRequest;
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        topAdsAddSourceTaggingUseCase.unsubscribe();
    }
}