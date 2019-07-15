package com.tokopedia.instantdebitbca.data.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.instantdebitbca.data.domain.GetAccessTokenBcaUseCase;
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterBcaUseCase;
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterEditLimit;
import com.tokopedia.instantdebitbca.data.view.interfaces.InstantDebitBcaContract;
import com.tokopedia.instantdebitbca.data.view.model.NotifyDebitRegisterBca;
import com.tokopedia.instantdebitbca.data.view.model.TokenInstantDebitBca;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class InstantDebitBcaPresenter extends BaseDaggerPresenter<InstantDebitBcaContract.View>
        implements InstantDebitBcaContract.Presenter {

    private CompositeSubscription compositeSubscription;
    private GetAccessTokenBcaUseCase getAccessTokenBcaUseCase;
    private NotifyDebitRegisterBcaUseCase notifyDebitRegisterBcaUseCase;
    private NotifyDebitRegisterEditLimit notifyDebitRegisterEditLimit;

    @Inject
    public InstantDebitBcaPresenter(GetAccessTokenBcaUseCase getAccessTokenBcaUseCase,
                                    NotifyDebitRegisterBcaUseCase notifyDebitRegisterBcaUseCase,
                                    NotifyDebitRegisterEditLimit notifyDebitRegisterEditLimit) {
        this.getAccessTokenBcaUseCase = getAccessTokenBcaUseCase;
        this.notifyDebitRegisterBcaUseCase = notifyDebitRegisterBcaUseCase;
        this.notifyDebitRegisterEditLimit = notifyDebitRegisterEditLimit;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getAccessTokenBca() {
        compositeSubscription.add(
                getAccessTokenBcaUseCase.createObservable(getAccessTokenBcaUseCase.createRequestParam())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TokenInstantDebitBca>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().showErrorMessage(e);
                                }
                            }

                            @Override
                            public void onNext(TokenInstantDebitBca tokenInstantDebitBca) {
                                getView().openWidgetBca(tokenInstantDebitBca.getAccessToken());
                            }
                        }));
    }

    @Override
    public void notifyDebitRegisterBca(String debitData, String deviceId) {
        compositeSubscription.add(
                notifyDebitRegisterBcaUseCase.createObservable(notifyDebitRegisterBcaUseCase.createRequestParam(debitData, deviceId))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<NotifyDebitRegisterBca>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().redirectPageAfterRegisterBca();
                                }
                            }

                            @Override
                            public void onNext(NotifyDebitRegisterBca notifyDebitRegisterBca) {
                                getView().redirectPageAfterRegisterBca();
                            }
                        }));
    }

    @Override
    public void notifyDebitRegisterEditLimit(String debitData, String deviceId){
        compositeSubscription.add(
                notifyDebitRegisterEditLimit.createObservable(notifyDebitRegisterEditLimit.createRequestParam(debitData, deviceId))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<NotifyDebitRegisterBca>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().redirectPageAfterRegisterBca();
                                }
                            }

                            @Override
                            public void onNext(NotifyDebitRegisterBca notifyDebitRegisterBca) {
                                getView().redirectPageAfterRegisterBca();
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        detachView();
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }
}
