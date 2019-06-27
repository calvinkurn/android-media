package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.contract.TokoCashHistoryContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.network.exception.UserInactivateTokoCashException;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryPresenter extends BaseDaggerPresenter<TokoCashHistoryContract.View> implements TokoCashHistoryContract.Presenter {

    private final GetHistoryDataUseCase getHistoryDataUseCase;
    private UserSessionInterface userSession;
    private int page = 1;

    @Inject
    public TokoCashHistoryPresenter(GetHistoryDataUseCase getHistoryDataUseCase,
                                    UserSessionInterface userSession) {
        this.getHistoryDataUseCase = getHistoryDataUseCase;
        this.userSession = userSession;
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
                        if (isViewAttached())
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
        if (userSession.isLoggedIn()) {
            page = 1;
            getHistoryDataUseCase.execute(getView().getHistoryTokoCashParam(false, page),
                    new Subscriber<TokoCashHistoryData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached()) {
                                if (e instanceof UserInactivateTokoCashException) {
                                    getView().navigatePageToActivateTokocash();
                                } else {
                                    getView().hideLoading();
                                    getView().renderEmptyPage(e);
                                }
                            }
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
        } else {
            getView().navigateToLoginPage();
        }
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
                        if (isViewAttached())
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
        detachView();
        if (getHistoryDataUseCase != null) getHistoryDataUseCase.unsubscribe();
    }
}