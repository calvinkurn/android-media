package com.tokopedia.wallet.ovoactivation.view

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.wallet.ovoactivation.domain.CheckNumberOvoUseCase
import com.tokopedia.wallet.ovoactivation.provider.WalletProvider
import com.tokopedia.wallet.ovoactivation.view.model.CheckPhoneOvoModel
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 24/09/18.
 */
class IntroOvoPresenter @Inject constructor(
    private val checkNumberOvoUseCase: CheckNumberOvoUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val walletProvider: WalletProvider
) :
    BaseDaggerPresenter<IntroOvoContract.View>(), IntroOvoContract.Presenter {

    val compositeSubscription: CompositeSubscription = CompositeSubscription()

    override fun checkPhoneNumber() {
        view.showProgressBar()
        compositeSubscription.add(
            checkNumberOvoUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(walletProvider.computation())
                .unsubscribeOn(walletProvider.computation())
                .observeOn(walletProvider.uiScheduler())
                .subscribe(object : Subscriber<CheckPhoneOvoModel>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            view.hideProgressBar()
                            val message = view.getErrorMessage(e)
                            view.showSnackbarErrorMessage(message)
                        }
                    }

                    override fun onNext(checkPhoneOvoModel: CheckPhoneOvoModel) {
                        view.hideProgressBar()
                        view.removeTokoCashCache()

                        if (!checkPhoneOvoModel.isAllow) {
                            if (checkPhoneOvoModel.errorModel?.message?.isNotBlank() == true) {
                                view.showSnackbarErrorMessage(checkPhoneOvoModel.errorModel?.message ?: "")
                            } else {
                                view.showDialogErrorPhoneNumber(checkPhoneOvoModel.phoneActionModel)
                            }
                        } else {
                            if (checkPhoneOvoModel.isRegistered) {
                                view.directPageWithApplink(checkPhoneOvoModel.registeredApplink)
                            } else {
                                view.directPageWithExtraApplink(
                                    checkPhoneOvoModel.notRegisteredApplink,
                                    checkPhoneOvoModel.registeredApplink,
                                    checkPhoneOvoModel.phoneNumber,
                                    checkPhoneOvoModel.changeMsisdnApplink
                                )
                            }
                        }
                    }
                })
        )
    }

    override fun getBalanceWallet() {
        compositeSubscription.add(
            getWalletBalanceUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(walletProvider.computation())
                .unsubscribeOn(walletProvider.computation())
                .observeOn(walletProvider.uiScheduler())
                .subscribe(object : Subscriber<WalletBalanceModel>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            val message = view.getErrorMessage(e)
                            view.showSnackbarErrorMessage(message)
                        }
                    }

                    override fun onNext(walletBalanceModel: WalletBalanceModel) {
                        view.setApplinkButton(
                            walletBalanceModel.helpApplink,
                            walletBalanceModel.tncApplink
                        )
                    }
                })
        )
    }

    override fun onDestroyView() {
        detachView()
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }
    }
}
