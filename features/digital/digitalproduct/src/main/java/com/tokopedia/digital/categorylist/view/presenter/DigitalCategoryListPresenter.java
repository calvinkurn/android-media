package com.tokopedia.digital.categorylist.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.categorylist.data.cloud.entity.tokocash.TokoCashData;
import com.tokopedia.digital.categorylist.data.cloud.exception.SessionExpiredException;
import com.tokopedia.digital.categorylist.domain.interactor.IDigitalCategoryListInteractor;
import com.tokopedia.digital.categorylist.view.listener.IDigitalCategoryListView;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.network.constant.ErrorNetMessage;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListPresenter extends BaseDaggerPresenter<IDigitalCategoryListView> implements IDigitalCategoryListPresenter {

    private final IDigitalCategoryListInteractor digitalCategoryListInteractor;
    private DigitalModuleRouter digitalModuleRouter;

    @Inject
    public DigitalCategoryListPresenter(
            IDigitalCategoryListInteractor digitalCategoryListInteractor,
            DigitalModuleRouter digitalModuleRouter) {
        this.digitalCategoryListInteractor = digitalCategoryListInteractor;
        this.digitalModuleRouter = digitalModuleRouter;
    }

    @Override
    public void processGetDigitalCategoryList(String deviceVersion) {
        getView().disableSwipeRefresh();
        digitalCategoryListInteractor.getDigitalCategoryItemDataList(
                deviceVersion,
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
                getView().enableSwipeRefresh();
            }

            @Override
            public void onError(Throwable e) {
                /*if (e instanceof RuntimeHttpErrorException) {
                    getView().renderErrorHttpGetDigitalCategoryList(
                            e.getMessage()
                    );
                } else*/ if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    getView().renderErrorNoConnectionGetDigitalCategoryList(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_SHORT
                    );
                } else if (e instanceof SocketTimeoutException) {
                    getView().renderErrorTimeoutConnectionGetDigitalCategoryList(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT_SHORT
                    );
                } else {
                    getView().renderErrorGetDigitalCategoryList(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT
                    );
                }
            }

            @Override
            public void onNext(List<DigitalCategoryItemData> digitalCategoryItemDataList) {
                getView().renderDigitalCategoryDataList(
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
                        && getView().isUserLogin()) {
                    digitalModuleRouter.showForceLogoutDialog();
                }
            }

            @Override
            public void onNext(TokoCashData tokoCashData) {
                getView().renderTokoCashData(tokoCashData);
            }
        };
    }
}
