package com.tokopedia.gm.statistic.view.presenter;

import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Subscriber;

/**
 * @author normansyahputa on 7/18/17.
 */
public class GMStatisticTransactionPresenterImpl extends GMStatisticTransactionPresenter {
    private GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase;
    private UserSessionInterface userSessionInterface;

    public GMStatisticTransactionPresenterImpl(
            GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase,
            UserSessionInterface userSession) {
        super();
        this.gmStatGetTransactionGraphUseCase = gmStatGetTransactionGraphUseCase;
        this.userSessionInterface = userSession;
    }

    @Override
    public void loadDataWithDate(final GMModuleRouter gmModuleRouter, long startDate, long endDate) {
        gmStatGetTransactionGraphUseCase.execute(GMStatGetTransactionGraphUseCase.createRequestParam(startDate, endDate, userSessionInterface.getShopId()), new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().onErrorLoadTransactionGraph(throwable);
                    getView().onErrorLoadTopAdsGraph(throwable);
                }
            }

            @Override
            public void onNext(GMTransactionGraphMergeModel mergeModel) {
                fetchTopAdsDeposit(mergeModel);
                // get necessary object, just take from transaction graph view
                getView().onSuccessLoadTransactionGraph(mergeModel);
            }

            private void fetchTopAdsDeposit(final GMTransactionGraphMergeModel gmTransactionGraphMergeModel) {
                if (gmTransactionGraphMergeModel.dataDeposit.isAdUsage()) {
                    getView().bindTopAds(gmTransactionGraphMergeModel.gmTopAdsAmountViewModel);
                } else {
                    getView().bindTopAdsCreditNotUsed(gmTransactionGraphMergeModel.gmTopAdsAmountViewModel, gmTransactionGraphMergeModel.dataDeposit);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        gmStatGetTransactionGraphUseCase.unsubscribe();
    }
}