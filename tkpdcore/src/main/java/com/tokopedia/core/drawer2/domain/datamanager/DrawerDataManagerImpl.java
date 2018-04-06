package com.tokopedia.core.drawer2.domain.datamanager;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.SellerDrawerData;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.domain.interactor.GetSellerUserAttributesUseCase;
import com.tokopedia.core.drawer2.domain.interactor.GetUserAttributesUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.TokoCashSubscriber;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.TokoCashUtil;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerDataManagerImpl implements DrawerDataManager {

    private static final String TAG = DrawerDataManagerImpl.class.getSimpleName();
    private final TokoCashUseCase tokoCashUseCase;
    private final GetUserAttributesUseCase userAttributesUseCase;
    private final NewNotificationUseCase newNotificationUseCase;
    private final GetSellerUserAttributesUseCase sellerUserAttributesUseCase;

    private final DrawerDataListener viewListener;

    public DrawerDataManagerImpl(DrawerDataListener viewListener,
                                 NewNotificationUseCase newNotificationUseCase,
                                 TokoCashUseCase tokoCashUseCase,
                                 GetUserAttributesUseCase uaUseCase,
                                 GetSellerUserAttributesUseCase sellerAttributeUseCase) {
        this.viewListener = viewListener;
        this.tokoCashUseCase = tokoCashUseCase;
        this.userAttributesUseCase = uaUseCase;
        this.newNotificationUseCase = newNotificationUseCase;
        this.sellerUserAttributesUseCase = sellerAttributeUseCase;
    }


    @Override
    public void getTokoCash() {
        tokoCashUseCase.execute(RequestParams.EMPTY, new TokoCashSubscriber(viewListener));
    }

    @Override
    public void getNotification() {
        newNotificationUseCase.execute(NotificationUseCase.getRequestParam(
                GlobalConfig.isSellerApp()),
                new NotificationSubscriber(viewListener));
    }

    @Override
    public void unsubscribe() {
        newNotificationUseCase.unsubscribe();
        tokoCashUseCase.unsubscribe();

    }


    /**
     * This function fetches and display the data needed to display in drawer and home page. It fetches
     * - Wallet Info
     * - Saldo Info
     * - Shop Info
     * - User Profile
     *
     * @param sessionHandler
     */

    @Override
    public void getUserAttributes(SessionHandler sessionHandler) {
        userAttributesUseCase.execute(userAttributesUseCase.getUserAttrParam(sessionHandler), new Subscriber<UserDrawerData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                String errorMessage = ErrorHandler.getErrorMessage(e);
                viewListener.onErrorGetTokoCash(ErrorHandler.getErrorMessage(e));
                viewListener.onErrorGetDeposit(ErrorHandler.getErrorMessage(e));
                viewListener.onErrorGetProfile(ErrorHandler.getErrorMessage(e));
                viewListener.onErrorGetProfileCompletion(errorMessage);
            }

            @Override
            public void onNext(UserDrawerData response) {
                if (viewListener.getActivity() == null) {
                    return;
                }

                //update values in session handler
                SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
                if (!sessionHandler.isV4Login()) {
                    if (response.getShopInfoMoengage() != null) {
                        if (!TextUtils.isEmpty(response.getShopInfoMoengage().getInfo().getShopId()))
                            sessionHandler.setShopId(response.getShopInfoMoengage().getInfo().getShopId());
                        if (!TextUtils.isEmpty(response.getShopInfoMoengage().getInfo().getShopDomain()))
                            sessionHandler.setShopDomain(viewListener.getActivity(), response.getShopInfoMoengage().getInfo().getShopDomain());
                    }

                    if (response.getProfile() != null && response.getProfile().getProfilePicture() != null) {
                        sessionHandler.setProfilePicture(response.getProfile().getProfilePicture());
                    }

                    sessionHandler.setTempLoginSession(response.getProfile().getUserId());
                    sessionHandler.setTempPhoneNumber(response.getProfile().getUserId());
                    sessionHandler.setTempLoginName(response.getProfile().getFullName());
                    sessionHandler.setTempLoginEmail(response.getProfile().getEmail());
                }

                //render saldo
                if (response.getSaldo() != null) {
                    String depositFormat = response.getSaldo().getDepositFmt();
                    if (depositFormat != null && !depositFormat.equalsIgnoreCase("ERROR FAIL")) {
                        DrawerDeposit drawerDeposit = new DrawerDeposit();
                        drawerDeposit.setDeposit(depositFormat);
                        viewListener.onGetDeposit(drawerDeposit);
                    } else {
                        viewListener.onErrorGetDeposit(depositFormat);
                    }
                }

                //render wallet data
                if (response.getWallet() != null) {
                    viewListener.onGetTokoCash(TokoCashUtil.convertToViewModel(response.getWallet(), viewListener.getActivity()));
                } else {
                    viewListener.onErrorGetTokoCash(ErrorHandler.getErrorMessage(new IOException()));
                }

                //render profile data
                if (response.getProfile() != null) {
                    DrawerProfile drawerProfile = new DrawerProfile();
                    if (response.getShopInfoMoengage() != null) {
                        if (response.getShopInfoMoengage().getInfo() != null && response.getShopInfoMoengage().getInfo().getShopAvatar() != null)
                            drawerProfile.setShopAvatar(response.getShopInfoMoengage().getInfo().getShopAvatar());
                        if (response.getShopInfoMoengage().getInfo() != null && response.getShopInfoMoengage().getInfo().getShopCover() != null)
                            drawerProfile.setShopCover(response.getShopInfoMoengage().getInfo().getShopCover());
                        if (response.getShopInfoMoengage().getInfo() != null && response.getShopInfoMoengage().getInfo().getShopName() != null)
                            drawerProfile.setShopName(String.valueOf(MethodChecker.fromHtml(
                                    response.getShopInfoMoengage().getInfo().getShopName())));
                    }
                    if (response.getProfile() != null) {
                        if (response.getProfile().getProfilePicture() != null)
                            drawerProfile.setUserAvatar(response.getProfile().getProfilePicture());
                        if (response.getProfile().getFullName() != null)
                            drawerProfile.setUserName(String.valueOf(MethodChecker.fromHtml(
                                    response.getProfile().getFullName())));
                    }
                    viewListener.onGetProfile(drawerProfile);
                }

                //render profile completion
                if (response.getProfile() != null) {
                    viewListener.onSuccessGetProfileCompletion(response.getProfile().getCompletion());
                }
            }
        });
    }

    @Override
    public void getSellerUserAttributes(SessionHandler sessionHandler) {
        sellerUserAttributesUseCase.execute(sellerUserAttributesUseCase.getUserAttrParam(sessionHandler), new Subscriber<SellerDrawerData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(SellerDrawerData response) {
                CommonUtils.dumper("rxapollo string " + response.toString());

                //render saldo
                if (response.getSaldo() != null) {
                    String depositFormat = response.getSaldo().getDepositFmt();
                    if (depositFormat != null && !depositFormat.equalsIgnoreCase("ERROR FAIL")) {
                        DrawerDeposit drawerDeposit = new DrawerDeposit();
                        drawerDeposit.setDeposit(depositFormat);
                        viewListener.onGetDeposit(drawerDeposit);
                    } else {
                        viewListener.onErrorGetDeposit(depositFormat);
                    }
                }

                //render profile data
                if (response.getProfile() != null) {
                    DrawerProfile drawerProfile = new DrawerProfile();
                    if (response.getShopInfoMoengage() != null) {
                        if (response.getShopInfoMoengage().getInfo() != null && response.getShopInfoMoengage().getInfo().getShopAvatar() != null)
                            drawerProfile.setShopAvatar(response.getShopInfoMoengage().getInfo().getShopAvatar());
                        if (response.getShopInfoMoengage().getInfo() != null && response.getShopInfoMoengage().getInfo().getShopCover() != null)
                            drawerProfile.setShopCover(response.getShopInfoMoengage().getInfo().getShopCover());
                        if (response.getShopInfoMoengage().getInfo() != null && response.getShopInfoMoengage().getInfo().getShopName() != null)
                            drawerProfile.setShopName(String.valueOf(MethodChecker.fromHtml(
                                    response.getShopInfoMoengage().getInfo().getShopName())));
                    }
                    if (response.getProfile() != null) {
                        if (response.getProfile().getProfilePicture() != null)
                            drawerProfile.setUserAvatar(response.getProfile().getProfilePicture());
                        if (response.getProfile().getFullName() != null)
                            drawerProfile.setUserName(String.valueOf(MethodChecker.fromHtml(
                                    response.getProfile().getFullName())));
                    }
                    viewListener.onGetProfile(drawerProfile);
                }


                //update values in session handler
                SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
                if (response.getProfile() != null && response.getProfile().getProfilePicture() != null) {
                    sessionHandler.setProfilePicture(response.getProfile().getProfilePicture());
                }
            }
        });
    }
}
