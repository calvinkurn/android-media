package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.digital.tokocash.interactor.ITokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.listener.TokoCashHistoryListener;
import com.tokopedia.digital.tokocash.model.HeaderHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryPresenter implements ITokoCashHistoryPresenter {

    private final ITokoCashHistoryInteractor interactor;

    private final TokoCashHistoryListener view;

    private String afterId = "";

    public TokoCashHistoryPresenter(ITokoCashHistoryInteractor interactor, TokoCashHistoryListener view) {
        this.interactor = interactor;
        this.view = view;
    }

    @Override
    public void getHistoryTokoCash(String type, String startDate, String endDate) {
        view.showLoadingHistory();
        interactor.getHistoryTokoCash(getTokoCashHistorySubscriber(), type, startDate, endDate, "");
    }

    private Subscriber<TokoCashHistoryData> getTokoCashHistorySubscriber() {
        return new Subscriber<TokoCashHistoryData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResponseDataNullException) {
                    view.renderEmptyTokoCashHistory(new ArrayList<HeaderHistory>());
                } else {
                    errorNetworkHandler(e);
                }
            }

            @Override
            public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                view.hideLoading();
                view.hideLoadingHistory();
                if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                    view.renderDataTokoCashHistory(tokoCashHistoryData, true);
                    afterId = String.valueOf(tokoCashHistoryData.getItemHistoryList()
                            .get(tokoCashHistoryData.getItemHistoryList().size() - 1)
                            .getTransactionDetailId());
                    view.setHasNextPage(tokoCashHistoryData.isNext_uri());
                } else {
                    view.renderEmptyTokoCashHistory(tokoCashHistoryData.getHeaderHistory());
                }
            }
        };
    }

    @Override
    public void getHistoryLoadMore(String type, String startDate, String endDate) {
        view.showLoadingHistory();
        interactor.getHistoryTokoCash(getTokoCashHistoryLoadMore(), type, startDate, endDate, afterId);
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
                view.hideLoadingHistory();
                if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                    view.renderDataTokoCashHistory(tokoCashHistoryData, false);
                    afterId = String.valueOf(tokoCashHistoryData.getItemHistoryList()
                            .get(tokoCashHistoryData.getItemHistoryList().size() - 1)
                            .getTransactionDetailId());
                }
                view.setHasNextPage(tokoCashHistoryData.isNext_uri());
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

    @Override
    public void onDestroy() {
        interactor.onDestroy();
    }
}
