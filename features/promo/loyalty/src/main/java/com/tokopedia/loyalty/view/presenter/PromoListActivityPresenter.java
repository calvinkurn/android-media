package com.tokopedia.loyalty.view.presenter;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoListActivityView;
import com.tokopedia.network.constant.ErrorNetMessage;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 04/01/18.
 */

public class PromoListActivityPresenter implements IPromoListActivityPresenter {

    private final IPromoInteractor promoInteractor;
    private final IPromoListActivityView view;

    @Inject
    public PromoListActivityPresenter(IPromoInteractor promoInteractor, IPromoListActivityView view) {
        this.promoInteractor = promoInteractor;
        this.view = view;
    }

    @Override
    public void processGetPromoMenu() {
        view.showProgressLoading();
        promoInteractor.getPromoMenuList(
                new TKPDMapParam<String, String>(), new Subscriber<List<PromoMenuData>>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        view.hideProgressLoading();

                        if (e instanceof JsonSyntaxException) {
                            Map<String, String> messageMap = new HashMap<>();
                            messageMap.put("type", "json");
                            messageMap.put("err", Log.getStackTraceString(e));
                            messageMap.put("req", PromoCodePresenter.class.getCanonicalName());
                            ServerLogger.log(Priority.P2, "LOYALTY_PARSE_ERROR", messageMap);
                        }
                        
                        if (e instanceof UnknownHostException) {
                            /* Ini kalau ga ada internet */
                            view.renderErrorNoConnectionGetPromoMenuDataList(
                                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                            );
                        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                            /* Ini kalau timeout */
                            view.renderErrorTimeoutConnectionGetPromoMenuDataListt(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        } else if (e instanceof HttpErrorException) {
                            /* Ini Http error, misal 403, 500, 404,
                            code http errornya bisa diambil
                             e.getErrorCode */
                            view.renderErrorHttpGetPromoMenuDataList(e.getMessage());
                        } else {
                            /* Ini diluar dari segalanya hahahaha */
                            view.renderErrorHttpGetPromoMenuDataList(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }

                    @Override
                    public void onNext(List<PromoMenuData> promoMenuData) {
                        view.renderPromoMenuDataList(promoMenuData);
                    }
                }
        );
    }
}
