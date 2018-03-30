package com.tokopedia.core.drawer2.domain.datamanager;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.anals.ConsumerDrawerData;
import com.tokopedia.anals.SellerDrawerData;
import com.tokopedia.core.analytics.domain.usecase.GetSellerUserAttributesUseCase;
import com.tokopedia.core.analytics.domain.usecase.GetUserAttributesUseCase;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.domain.interactor.DepositUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
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
        userAttributesUseCase.execute(userAttributesUseCase.getUserAttrParam(sessionHandler), new Subscriber<ConsumerDrawerData.Data>() {
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
            public void onNext(ConsumerDrawerData.Data response) {
                //update values in session handler
                SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
                if (!sessionHandler.isV4Login()) {
                    if (response.shopInfoMoengage() != null) {
                        if (!TextUtils.isEmpty(response.shopInfoMoengage().info().shop_id()))
                            sessionHandler.setShopId(response.shopInfoMoengage().info().shop_id());
                        if (!TextUtils.isEmpty(response.shopInfoMoengage().info().shop_domain()))
                            sessionHandler.setShopDomain(viewListener.getActivity(), response.shopInfoMoengage().info().shop_domain());
                    }

                    if (response.profile() != null && response.profile().profile_picture() != null) {
                        sessionHandler.setProfilePicture(response.profile().profile_picture());
                    }

                    sessionHandler.setTempLoginSession(response.profile().user_id());
                    sessionHandler.setTempPhoneNumber(response.profile().phone());
                    sessionHandler.setTempLoginName(response.profile().full_name());
                    sessionHandler.setTempLoginEmail(response.profile().email().toString());
                }

                //render saldo
                if (response.saldo() != null) {
                    String depositFormat = response.saldo().deposit_fmt();
                    if (depositFormat != null && !depositFormat.equalsIgnoreCase("ERROR FAIL")) {
                        DrawerDeposit drawerDeposit = new DrawerDeposit();
                        drawerDeposit.setDeposit(depositFormat);
                        viewListener.onGetDeposit(drawerDeposit);
                    }
                }

                //render wallet data
                if (response.wallet() != null) {
                    viewListener.onGetTokoCash(TokoCashUtil.convertToViewModel(response.wallet()));
                } else {
                    viewListener.onErrorGetTokoCash(ErrorHandler.getErrorMessage(new IOException()));
                }

                //render profile data
                if (response.profile() != null) {
                    DrawerProfile drawerProfile = new DrawerProfile();
                    if (response.shopInfoMoengage() != null) {
                        if (response.shopInfoMoengage().info() != null && response.shopInfoMoengage().info().shop_avatar() != null)
                            drawerProfile.setShopAvatar(response.shopInfoMoengage().info().shop_avatar());
                        if (response.shopInfoMoengage().info() != null && response.shopInfoMoengage().info().shop_cover() != null)
                            drawerProfile.setShopCover(response.shopInfoMoengage().info().shop_cover());
                        if (response.shopInfoMoengage().info() != null && response.shopInfoMoengage().info().shop_name() != null)
                            drawerProfile.setShopName(String.valueOf(MethodChecker.fromHtml(
                                    response.shopInfoMoengage().info().shop_name())));
                    }
                    if (response.profile() != null) {
                        if (response.profile().profile_picture() != null)
                            drawerProfile.setUserAvatar(response.profile().profile_picture());
                        if (response.profile().full_name() != null)
                            drawerProfile.setUserName(String.valueOf(MethodChecker.fromHtml(
                                    response.profile().full_name())));
                    }
                    viewListener.onGetProfile(drawerProfile);
                }

                //render profile completion
                if (response.profile() != null) {
                    viewListener.onSuccessGetProfileCompletion(response.profile().completion());
                }
            }
        });
    }

    @Override
    public void getSellerUserAttributes(SessionHandler sessionHandler) {
        sellerUserAttributesUseCase.execute(sellerUserAttributesUseCase.getUserAttrParam(sessionHandler), new Subscriber<SellerDrawerData.Data>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(SellerDrawerData.Data response) {
                CommonUtils.dumper("rxapollo string " + response.toString());

                //render saldo
                if (response.saldo() != null) {
                    String depositFormat = response.saldo().deposit_fmt();
                    if (depositFormat != null && !depositFormat.equalsIgnoreCase("ERROR FAIL")) {
                        DrawerDeposit drawerDeposit = new DrawerDeposit();
                        drawerDeposit.setDeposit(depositFormat);
                        viewListener.onGetDeposit(drawerDeposit);
                    }
                }

                //render profile
                if (response.profile() != null) {
                    DrawerProfile drawerProfile = new DrawerProfile();
                    if (response.shopInfoMoengage() != null) {
                        if (response.shopInfoMoengage().info() != null && response.shopInfoMoengage().info().shop_avatar() != null)
                            drawerProfile.setShopAvatar(response.shopInfoMoengage().info().shop_avatar());
                        if (response.shopInfoMoengage().info() != null && response.shopInfoMoengage().info().shop_cover() != null)
                            drawerProfile.setShopCover(response.shopInfoMoengage().info().shop_cover());
                        if (response.shopInfoMoengage().info() != null && response.shopInfoMoengage().info().shop_name() != null)
                            drawerProfile.setShopName(String.valueOf(MethodChecker.fromHtml(
                                    response.shopInfoMoengage().info().shop_name())));
                    }
                    if (response.profile() != null) {
                        if (response.profile().profile_picture() != null)
                            drawerProfile.setUserAvatar(response.profile().profile_picture());
                        if (response.profile().full_name() != null)
                            drawerProfile.setUserName(String.valueOf(MethodChecker.fromHtml(
                                    response.profile().full_name())));
                    }

                    viewListener.onGetProfile(drawerProfile);
                }


                //update values in session handler
                SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
                if (response.profile() != null && response.profile().profile_picture() != null) {
                    sessionHandler.setProfilePicture(response.profile().profile_picture());
                }
            }
        });
    }
}
