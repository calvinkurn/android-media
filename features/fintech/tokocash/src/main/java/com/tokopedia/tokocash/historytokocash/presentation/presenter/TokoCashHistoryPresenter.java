package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.contract.TokoCashHistoryContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryPresenter extends BaseDaggerPresenter<TokoCashHistoryContract.View> implements TokoCashHistoryContract.Presenter {

    private final GetHistoryDataUseCase getHistoryDataUseCase;
    private int page = 1;

    @Inject
    public TokoCashHistoryPresenter(GetHistoryDataUseCase getHistoryDataUseCase) {
        this.getHistoryDataUseCase = getHistoryDataUseCase;
    }

    @Override
    public void getWaitingTransaction() {
        page = 1;
        getHistoryDataUseCase.execute(getView().getHistoryTokoCashParam(true, page),
                new Subscriber<TokoCashHistoryData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideWaitingTransaction();
                    }

                    @Override
                    public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                        if (tokoCashHistoryData.getItemHistoryList() != null)
                            getView().renderWaitingTransaction(tokoCashHistoryData);
                        else
                            getView().hideWaitingTransaction();
                    }
                });
    }

    @Override
    public void getInitHistoryTokoCash() {
        page = 1;
        getHistoryDataUseCase.execute(getView().getHistoryTokoCashParam(false, page),
                new Subscriber<TokoCashHistoryData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLoading();
                        getView().renderEmptyPage(e);
                    }

                    @Override
                    public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                        getView().hideLoading();
                        if (tokoCashHistoryData.getItemHistoryList().size() == 0 && !tokoCashHistoryData.isNext_uri()) {
                            getView().renderDataTokoCashHistory(tokoCashHistoryData, true);
                            getView().renderEmptyTokoCashHistory(tokoCashHistoryData.getHeaderHistory());
                        } else {
                            if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                                getView().renderDataTokoCashHistory(tokoCashHistoryData, true);
                                getView().setHasNextPage(tokoCashHistoryData.isNext_uri());
                                if (tokoCashHistoryData.isNext_uri()) page++;
                            } else {
                                getView().renderEmptyTokoCashHistory(tokoCashHistoryData.getHeaderHistory());
                            }
                        }
                    }
                });
    }

    @Override
    public void getHistoryLoadMore() {
        getHistoryDataUseCase.execute(getView().getHistoryTokoCashParam(false, page),
                new Subscriber<TokoCashHistoryData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().renderErrorMessage(e);
                    }

                    @Override
                    public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                        if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                            getView().renderDataTokoCashHistory(tokoCashHistoryData, false);
                        }
                        getView().setHasNextPage(tokoCashHistoryData.isNext_uri());
                        if (tokoCashHistoryData.isNext_uri()) page++;
                    }
                });
    }

    @Override
    public void onDestroyPresenter() {
        if (getHistoryDataUseCase != null) getHistoryDataUseCase.unsubscribe();
    }
}