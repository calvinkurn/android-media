package com.tokopedia.core.drawer2.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositData;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 5/4/17.
 */

public class GetDepositSubscriber extends Subscriber<DepositModel> {

    private final DrawerDataListener viewListener;

    public GetDepositSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorGetDeposit(
                    viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorGetDeposit(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorGetDeposit(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorGetDeposit(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorGetDeposit(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorGetDeposit(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof MessageErrorException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorGetDeposit(e.getLocalizedMessage());
        } else {
            viewListener.onErrorGetDeposit(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(DepositModel depositModel) {
        if (depositModel.isSuccess())
            viewListener.onGetDeposit(convertToViewModel(depositModel.getDepositData()));
        else
            viewListener.onErrorGetDeposit(depositModel.getErrorMessage());
    }

    private DrawerDeposit convertToViewModel(DepositData depositData) {
        DrawerDeposit drawerDeposit = new DrawerDeposit();
        drawerDeposit.setDeposit(depositData.getDepositTotal());
        return drawerDeposit;
    }
}
