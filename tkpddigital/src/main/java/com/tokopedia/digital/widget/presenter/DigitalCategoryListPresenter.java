package com.tokopedia.digital.widget.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.exception.SessionExpiredException;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;
import com.tokopedia.digital.tokocash.receiver.TokoCashBroadcastReceiver;
import com.tokopedia.digital.widget.interactor.IDigitalCategoryListInteractor;
import com.tokopedia.digital.widget.listener.IDigitalCategoryListView;
import com.tokopedia.digital.widget.model.DigitalCategoryItemData;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListPresenter implements IDigitalCategoryListPresenter {

    private final IDigitalCategoryListInteractor digitalCategoryListInteractor;
    private final IDigitalCategoryListView digitalCategoryListView;

    private boolean isFromSeller;

    public DigitalCategoryListPresenter(
            boolean isFromSeller, IDigitalCategoryListInteractor digitalCategoryListInteractor,
            IDigitalCategoryListView iDigitalCategoryListView
    ) {
        this.isFromSeller = isFromSeller;
        this.digitalCategoryListInteractor = digitalCategoryListInteractor;
        this.digitalCategoryListView = iDigitalCategoryListView;
    }

    @Override
    public void processGetDigitalCategoryList() {
        digitalCategoryListView.disableSwipeRefresh();
        digitalCategoryListInteractor.getDigitalCategoryItemDataList(
                getSubscriberDigitalCategoryList()
        );
    }

    @Override
    public void processGetTokoCashData() {
        digitalCategoryListInteractor.getTokoCashData(getSubscriberFetchTokoCashData());
    }

    @NonNull
    private Subscriber<List<DigitalCategoryItemData>> getSubscriberDigitalCategoryList() {
        return new Subscriber<List<DigitalCategoryItemData>>() {
            @Override
            public void onCompleted() {
                Log.d("DigitalCategoryListPresenter", "onCompleted");
                digitalCategoryListView.enableSwipeRefresh();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("DigitalCategoryListPresenter", "onError");
                if (e instanceof RuntimeHttpErrorException) {
                    digitalCategoryListView.renderErrorHttpGetDigitalCategoryList(
                            e.getMessage()
                    );
                } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    digitalCategoryListView.renderErrorNoConnectionGetDigitalCategoryList(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_SHORT
                    );
                } else if (e instanceof SocketTimeoutException) {
                    digitalCategoryListView.renderErrorTimeoutConnectionGetDigitalCategoryList(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT_SHORT
                    );
                } else {
                    Log.d("DigitalCategoryListPresenter", e.getMessage());
                    digitalCategoryListView.renderErrorGetDigitalCategoryList(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT
                    );
                }
            }

            @Override
            public void onNext(List<DigitalCategoryItemData> digitalCategoryItemDataList) {
                Log.d("DigitalCategoryListPresenter", "onNext");
                if (isFromSeller) {
                    Iterator<DigitalCategoryItemData> iter = digitalCategoryItemDataList.iterator();
                    while (iter.hasNext()) {
                        DigitalCategoryItemData digitalCategoryItemData = iter.next();
                        if (isUberOrTiketKeretaApi(digitalCategoryItemData.getName())) {
                            iter.remove();
                        }
                    }
                }
                digitalCategoryListView.renderDigitalCategoryDataList(
                        digitalCategoryItemDataList
                );
            }
        };
    }

    @NonNull
    private Subscriber<TokoCashData> getSubscriberFetchTokoCashData() {
        return new Subscriber<TokoCashData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SessionExpiredException
                        && digitalCategoryListView.isUserLogin()) {
                    ServerErrorHandler.showForceLogoutDialog();
                }
            }

            @Override
            public void onNext(TokoCashData tokoCashDataResponse) {
                Intent intent = new Intent(TokoCashBroadcastReceiver.ACTION_GET_TOKOCASH_DIGITAL);
                intent.putExtra(TokoCashBroadcastReceiver.EXTRA_RESULT_TOKOCASH_DATA_DIGITAL,
                        tokoCashDataResponse);
                digitalCategoryListView.sendBroadcastReceiver(intent);
            }
        };
    }

    private boolean isUberOrTiketKeretaApi(String product) {
        return product.equals("Uber") || product.equals("Tiket Kereta Api");
    }

}
