package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.digital.tokocash.interactor.ITokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.listener.TokoCashHistoryListener;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryPresenter implements ITokoCashHistoryPresenter {

    private final ITokoCashHistoryInteractor interactor;

    private final TokoCashHistoryListener view;

    private int page = 1;

    public TokoCashHistoryPresenter(ITokoCashHistoryInteractor interactor, TokoCashHistoryListener view) {
        this.interactor = interactor;
        this.view = view;
    }

    @Override
    public void getWaitingTransaction() {
        page = 1;
        interactor.getHistoryTokoCash(getWaitingTransacionSubscriber(), "pending", "", "", page);
    }

    private Subscriber<TokoCashHistoryData> getWaitingTransacionSubscriber() {
        return new Subscriber<TokoCashHistoryData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideWaitingTransaction();
            }

            @Override
            public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                if (tokoCashHistoryData.getItemHistoryList() != null)
                    view.renderWaitingTransaction(tokoCashHistoryData);
                else
                    view.hideWaitingTransaction();
            }
        };
    }

    @Override
    public void getInitHistoryTokoCash(String type, String startDate, String endDate) {
        page = 1;
        interactor.getHistoryTokoCash(getTokoCashHistorySubscriber(), type, startDate, endDate, page);
    }

    private Subscriber<TokoCashHistoryData> getTokoCashHistorySubscriber() {
        return new Subscriber<TokoCashHistoryData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideLoading();
                if (e instanceof ResponseDataNullException) {
                    view.renderEmptyPage("Empty data list");
                } else {
                    errorFirstTimeNetworkHandler(e);
                }
            }

            @Override
            public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                view.hideLoading();
                if (tokoCashHistoryData.getItemHistoryList().size() == 0 && !tokoCashHistoryData.isNext_uri()) {
                    view.renderDataTokoCashHistory(tokoCashHistoryData, true);
                    view.renderEmptyTokoCashHistory(tokoCashHistoryData.getHeaderHistory());
                } else {
                    if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                        view.renderDataTokoCashHistory(tokoCashHistoryData, true);
                        view.setHasNextPage(tokoCashHistoryData.isNext_uri());
                        if (tokoCashHistoryData.isNext_uri()) page++;
                    } else {
                        view.renderEmptyTokoCashHistory(tokoCashHistoryData.getHeaderHistory());
                    }
                }
            }
        };
    }

    @Override
    public void getHistoryLoadMore(String type, String startDate, String endDate) {
        interactor.getHistoryTokoCash(getTokoCashHistoryLoadMore(), type, startDate, endDate, page);
    }

    private Subscriber<TokoCashHistoryData> getTokoCashHistoryLoadMore() {
        return new Subscriber<TokoCashHistoryData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                errorNetworkHandler(e);
            }

            @Override
            public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                    view.renderDataTokoCashHistory(tokoCashHistoryData, false);
                }
                view.setHasNextPage(tokoCashHistoryData.isNext_uri());
                if (tokoCashHistoryData.isNext_uri()) page++;
            }
        };
    }

    private void errorNetworkHandler(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.renderErrorMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.renderErrorMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseDataNullException) {
            view.renderErrorMessage(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.renderErrorMessage(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            ServerErrorHandlerUtil.handleError(e);
        } else {
            view.renderErrorMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    private void errorFirstTimeNetworkHandler(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.renderEmptyPage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.renderEmptyPage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseDataNullException) {
            view.renderEmptyPage(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.renderEmptyPage(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            ServerErrorHandlerUtil.handleError(e);
        } else {
            view.renderEmptyPage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}
