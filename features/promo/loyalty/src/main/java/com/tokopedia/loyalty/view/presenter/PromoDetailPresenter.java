package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoDetailView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Aghny A. Putra on 4/4/18
 */

public class PromoDetailPresenter extends IBasePresenter<IPromoDetailView>
        implements IPromoDetailPresenter {

    private static final String KEY_PARAM_SLUG = "slug";

    private final IPromoInteractor promoInteractor;

    @Inject
    public PromoDetailPresenter(IPromoInteractor promoInteractor) {
        this.promoInteractor = promoInteractor;
    }

    @Override
    public void getPromoDetail(String slug) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(KEY_PARAM_SLUG, slug);

        this.promoInteractor.getPromoList(param, getSubscriber());
    }

    private Subscriber<List<PromoData>> getSubscriber() {
        return new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    if (e instanceof UnknownHostException) {
                        // No internet connection
                        getMvpView().renderErrorNoConnectionGetPromoDetail(
                                ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                        );
                    } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                        // Timeout
                        getMvpView().renderErrorTimeoutConnectionGetPromoDetail(
                                ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                        );
                    } else if (e instanceof HttpErrorException) {
                        // Http errors such as 4xx, 5xx
                        getMvpView().renderErrorHttpGetPromoDetail(e.getMessage());
                    } else {
                        // Undefined errors
                        getMvpView().renderErrorHttpGetPromoDetail(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                }

                e.printStackTrace();
            }

            @Override
            public void onNext(List<PromoData> promoData) {
                if (isViewAttached()) {
                    if (promoData != null && !promoData.isEmpty()) {
                        getMvpView().renderPromoDetail(promoData.get(0));
                    } else {
                        getMvpView().renderErrorShowingPromoDetail(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                }
            }
        };
    }

}