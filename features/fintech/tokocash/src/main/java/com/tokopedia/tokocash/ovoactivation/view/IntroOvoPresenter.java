package com.tokopedia.tokocash.ovoactivation.view;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.balance.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.common.WalletProvider;
import com.tokopedia.tokocash.ovoactivation.domain.CheckNumberOvoUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;
import com.tokopedia.cachemanager.PersistentCacheManager;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class IntroOvoPresenter extends BaseDaggerPresenter<IntroOvoContract.View>
        implements IntroOvoContract.Presenter {

    private CheckNumberOvoUseCase checkNumberOvoUseCase;
    private GetBalanceTokoCashUseCase getBalanceTokoCashUseCase;
    private WalletProvider walletProvider;
    private CompositeSubscription compositeSubscription;

    @Inject
    public IntroOvoPresenter(CheckNumberOvoUseCase checkNumberOvoUseCase,
                             GetBalanceTokoCashUseCase getBalanceTokoCashUseCase,
                             WalletProvider walletProvider) {
        this.checkNumberOvoUseCase = checkNumberOvoUseCase;
        this.walletProvider = walletProvider;
        this.getBalanceTokoCashUseCase = getBalanceTokoCashUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void checkPhoneNumber() {
        getView().showProgressBar();
        compositeSubscription.add(
                checkNumberOvoUseCase.createObservable(RequestParams.EMPTY)
                        .subscribeOn(walletProvider.computation())
                        .unsubscribeOn(walletProvider.computation())
                        .observeOn(walletProvider.uiScheduler())
                        .subscribe(new Subscriber<CheckPhoneOvoModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().hideProgressBar();
                                    String message = getView().getErrorMessage(e);
                                    getView().showSnackbarErrorMessage(message);
                                }
                            }

                            @Override
                            public void onNext(CheckPhoneOvoModel checkPhoneOvoModel) {
                                getView().hideProgressBar();
                                PersistentCacheManager.instance.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);

                                if (!checkPhoneOvoModel.isAllow()) {
                                    if (!TextUtils.isEmpty(checkPhoneOvoModel.getErrorModel().getMessage())) {
                                        getView().showSnackbarErrorMessage(checkPhoneOvoModel.getErrorModel().getMessage());
                                    } else {
                                        getView().showDialogErrorPhoneNumber(checkPhoneOvoModel.getPhoneActionModel());
                                    }
                                } else {
                                    if (checkPhoneOvoModel.isRegistered()) {
                                        getView().directPageWithApplink(checkPhoneOvoModel.getRegisteredApplink());
                                    } else {
                                        getView().directPageWithExtraApplink(checkPhoneOvoModel.getNotRegisteredApplink(),
                                                checkPhoneOvoModel.getRegisteredApplink(),
                                                checkPhoneOvoModel.getPhoneNumber(),
                                                checkPhoneOvoModel.getChangeMsisdnApplink());
                                    }
                                }
                            }
                        })
        );
    }

    @Override
    public void getBalanceWallet() {
        compositeSubscription.add(getBalanceTokoCashUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(walletProvider.computation())
                .unsubscribeOn(walletProvider.computation())
                .observeOn(walletProvider.uiScheduler())
                .subscribe(new Subscriber<BalanceTokoCash>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            String message = getView().getErrorMessage(e);
                            getView().showSnackbarErrorMessage(message);
                        }
                    }

                    @Override
                    public void onNext(BalanceTokoCash balanceTokoCash) {
                        getView().setApplinkButton(balanceTokoCash.getHelpApplink(),
                                balanceTokoCash.getTncApplink());
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        detachView();
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }
}
