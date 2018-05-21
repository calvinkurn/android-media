package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.digital.tokocash.interactor.IAccountSettingInteractor;
import com.tokopedia.digital.tokocash.listener.IWalletAccountSettingView;
import com.tokopedia.digital.tokocash.model.OAuthInfo;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingPresenter implements IWalletAccountSettingPresenter {

    private final IWalletAccountSettingView view;
    private final IAccountSettingInteractor interactor;


    public WalletAccountSettingPresenter(IWalletAccountSettingView view, IAccountSettingInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void processGetWalletAccountData() {
        interactor.getOAuthInfo(getOAuthSubscriber());
    }

    private Subscriber<OAuthInfo> getOAuthSubscriber() {
        return new Subscriber<OAuthInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                errorHandling(e);
            }

            @Override
            public void onNext(OAuthInfo oAuthInfo) {
                if (oAuthInfo != null)
                    view.renderWalletOAuthInfoData(oAuthInfo);
            }
        };
    }

    private void errorHandling(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.renderErrorGetWalletAccountSettingData(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.renderErrorGetWalletAccountSettingData(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseDataNullException) {
            view.renderErrorGetWalletAccountSettingData(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.renderErrorGetWalletAccountSettingData(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            ServerErrorHandlerUtil.handleError(e);
        } else {
            view.renderErrorGetWalletAccountSettingData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    @Override
    public void processDeleteConnectedUser(ProfileModel profileModel, String refreshToken,
                                           String identifier, String identifierType) {
        interactor.unlinkAccountTokoCash(unlinkSubscriber(profileModel, identifier), refreshToken, identifier, identifierType);
    }

    private Subscriber<Boolean> unlinkSubscriber(final ProfileModel profileModel, final String identifier) {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                errorHandlingUnlink(e);
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if (isSuccess) {
                    if (profileModel != null) {
                        if (profileModel.getProfileData().getUserInfo().getUserEmail().equals(identifier))
                            view.renderSuccessUnlinkToHome();
                        else
                            view.renderSuccessUnlinkAccount();

                    }
                }
            }
        };
    }

    private void errorHandlingUnlink(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.renderErrorUnlinkAccount(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.renderErrorUnlinkAccount(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseDataNullException) {
            view.renderErrorUnlinkAccount(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.renderErrorUnlinkAccount(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            ServerErrorHandlerUtil.handleError(e);
        } else {
            view.renderErrorUnlinkAccount(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}