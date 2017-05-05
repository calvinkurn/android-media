package com.tokopedia.core.drawer2.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.topcash.Action;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashSubscriber extends Subscriber<TokoCashModel> {
    private final DrawerDataListener viewListener;

    public TokoCashSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorGetTokoCash(
                    viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorGetTokoCash(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorGetTokoCash(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorGetTokoCash(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorGetTokoCash(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorGetTokoCash(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof MessageErrorException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorGetTokoCash(e.getLocalizedMessage());
        } else {
            viewListener.onErrorGetTokoCash(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(TokoCashModel tokoCashModel) {
        if(tokoCashModel.isSuccess())
            viewListener.onGetTokoCash(convertToViewModel(tokoCashModel.getTokoCashData()));
        else 
            viewListener.onErrorGetTokoCash(tokoCashModel.getErrorMessage());
    }

    private DrawerTokoCash convertToViewModel(TokoCashData tokoCashData) {
        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
        drawerTokoCash.setBalance(tokoCashData.getBalance());
        drawerTokoCash.setLink(tokoCashData.getLink());
        drawerTokoCash.setRedirectUrl(tokoCashData.getRedirectUrl());
        drawerTokoCash.setText(tokoCashData.getText());
        drawerTokoCash.setDrawerTokoCashAction(convertToActionViewModel(tokoCashData.getAction()));
        return drawerTokoCash;
    }

    private DrawerTokoCashAction convertToActionViewModel(Action action) {
        DrawerTokoCashAction drawerTokoCashAction = new DrawerTokoCashAction();
        drawerTokoCashAction.setText(action.getText());
        drawerTokoCashAction.setRedirectUrl(action.getRedirectUrl());
        return drawerTokoCashAction;
    }
}

