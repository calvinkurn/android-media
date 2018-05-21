package com.tokopedia.digital.tokocash.presenter;

import android.content.Context;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.digital.R;
import com.tokopedia.digital.tokocash.errorhandle.ResponseTokoCashRuntimeException;
import com.tokopedia.digital.tokocash.interactor.ITokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.listener.HelpHistoryListener;
import com.tokopedia.digital.tokocash.model.HelpHistoryTokoCash;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 10/17/17.
 */

public class HelpHistoryPresenter implements IHelpHistoryPresenter {

    private ITokoCashHistoryInteractor historyInteractor;
    private HelpHistoryListener view;
    private Context context;

    public HelpHistoryPresenter(Context context, ITokoCashHistoryInteractor historyInteractor, HelpHistoryListener view) {
        this.historyInteractor = historyInteractor;
        this.view = view;
        this.context = context;
    }

    @Override
    public void getHelpCategoryHistory() {
        historyInteractor.getHelpListCategory(getHelpHistorySubscriber());
    }

    private Subscriber<List<HelpHistoryTokoCash>> getHelpHistorySubscriber() {
        return new Subscriber<List<HelpHistoryTokoCash>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                handleError(e, context.getString(R.string.error_message_load_spinner_help));
            }

            @Override
            public void onNext(List<HelpHistoryTokoCash> helpHistoryTokoCashes) {
                view.loadHelpHistoryData(helpHistoryTokoCashes);
            }
        };
    }

    @Override
    public void submitHelpHistory(String subject, String message, String category, String transactionId) {
        historyInteractor.postHelpHistory(getSubmitHelpHistotorySubscriber(), subject, message, category, transactionId);
        view.showProgressLoading();
    }

    private Subscriber<Boolean> getSubmitHelpHistotorySubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                handleError(e, context.getString(R.string.error_message_send_help));
            }

            @Override
            public void onNext(Boolean isSubmitted) {
                view.hideProgressLoading();
                view.successSubmitHelpHistory();
            }
        };
    }

    private void handleError(Throwable e, String defaultErrorMessage) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.showErrorHelpHistory(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.showErrorHelpHistory(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseTokoCashRuntimeException) {
            view.showErrorHelpHistory(e.getMessage());
        } else if (e instanceof ResponseDataNullException) {
            view.showErrorHelpHistory(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.showErrorHelpHistory(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            ServerErrorHandlerUtil.handleError(e);
        } else {
            view.showErrorHelpHistory(defaultErrorMessage);
        }
    }
}
