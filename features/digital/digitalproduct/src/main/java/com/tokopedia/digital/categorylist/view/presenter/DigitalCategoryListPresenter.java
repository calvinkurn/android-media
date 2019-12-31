package com.tokopedia.digital.categorylist.view.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel;
import com.tokopedia.digital.categorylist.data.cloud.entity.tokocash.Action;
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
                } else*/
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
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
    private Subscriber<WalletBalanceModel> getSubscriberFetchTokoCashData() {
        return new Subscriber<WalletBalanceModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SessionExpiredException
                        && getView().isUserLogin()) {
//                    digitalModuleRouter.showForceLogoutDialog();
                }
            }

            @Override
            public void onNext(WalletBalanceModel walletBalanceModel) {
                getView().renderTokoCashData(mapper(walletBalanceModel));
            }
        };
    }

    //TokocashData is not changed to WalletBalanceModel because it still in used by several classes in digital module
    private TokoCashData mapper(WalletBalanceModel walletBalanceModel) {
        TokoCashData tokoCashData = new TokoCashData();
        if (walletBalanceModel != null) {

            if (walletBalanceModel.getActionBalanceModel() != null) {
                Action action = new Action();
                action.setmAppLinks(walletBalanceModel.getActionBalanceModel().getApplinks());
                action.setmText(walletBalanceModel.getActionBalanceModel().getLabelAction());
                action.setRedirectUrl(walletBalanceModel.getActionBalanceModel().getRedirectUrl());
                action.setmVisibility(walletBalanceModel.getActionBalanceModel().getVisibility());
                tokoCashData.setAction(action);
            }
            tokoCashData.setAbTags(walletBalanceModel.getAbTags());
            tokoCashData.setmAppLinks(walletBalanceModel.getApplinks());
            tokoCashData.setBalance(walletBalanceModel.getBalance());
            tokoCashData.setHoldBalance(walletBalanceModel.getHoldBalance());
            tokoCashData.setLink(walletBalanceModel.getLink());
            tokoCashData.setRaw_balance(walletBalanceModel.getRawBalance());
            tokoCashData.setRawHoldBalance(walletBalanceModel.getRawHoldBalance());
            tokoCashData.setRawThreshold(walletBalanceModel.getRawThreshold());
            tokoCashData.setRawTotalBalance(walletBalanceModel.getRawTotalBalance());
            tokoCashData.setRedirectUrl(walletBalanceModel.getRedirectUrl());
            tokoCashData.setThreshold(walletBalanceModel.getThreshold());
            tokoCashData.setText(walletBalanceModel.getTitleText());
            tokoCashData.setTotalBalance(walletBalanceModel.getTotalBalance());
        }
        return tokoCashData;
    }
}
