package com.tokopedia.topads.dashboard.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEtalaseListUseCase;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;
import com.tokopedia.topads.dashboard.view.listener.TopAdsEtalaseListView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import rx.Subscriber;

public class TopAdsEtalaseListPresenterImpl extends BaseDaggerPresenter<TopAdsEtalaseListView>
        implements TopAdsEtalaseListPresenter {

    private TopAdsEtalaseListUseCase topAdsEtalaseListUseCase;

    public TopAdsEtalaseListPresenterImpl(TopAdsEtalaseListUseCase topAdsEtalaseListUseCase) {
        this.topAdsEtalaseListUseCase = topAdsEtalaseListUseCase;
    }

    @Override
    public void populateEtalaseList(String shopId) {
        if (TextUtils.isEmpty(shopId))
            return;
        getView().showLoad(true);

        topAdsEtalaseListUseCase.execute(
                TopAdsEtalaseListUseCase.createRequestParams(shopId),
                getShopEtalaseSubscriber()
        );
    }

    private Subscriber<List<Etalase>> getShopEtalaseSubscriber() {
        return new Subscriber<List<Etalase>>() {
            @Override
            public void onCompleted() {
                getView().showLoad(false);
            }

            @Override
            public void onError(Throwable e) {
                getView().showLoad(false);
                if (e instanceof UnknownHostException ||
                        e instanceof ConnectException) {
                    getView().onLoadConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    getView().onLoadConnectionError();
                } else {
                    getView().onLoadError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(List<Etalase> etalaseList) {
                if (etalaseList != null && etalaseList.size() > 0) {
                    getView().onLoadSuccess(etalaseList);
                } else {
                    getView().onLoadSuccessEtalaseEmpty();
                }
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsEtalaseListUseCase.unsubscribe();
    }
}