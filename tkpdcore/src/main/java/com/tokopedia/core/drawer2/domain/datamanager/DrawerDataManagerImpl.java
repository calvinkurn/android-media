package com.tokopedia.core.drawer2.domain.datamanager;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.Notifications;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.domain.interactor.GetSellerUserAttributesUseCase;
import com.tokopedia.core.drawer2.domain.interactor.GetUserAttributesUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.subscriber.TokoCashSubscriber;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.TokoCashUtil;
import com.tokopedia.core2.R;
import com.tokopedia.design.utils.CurrencyFormatUtil;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerDataManagerImpl implements DrawerDataManager {

    private static final String TAG = DrawerDataManagerImpl.class.getSimpleName();
    private final TokoCashUseCase tokoCashUseCase;
    private final GetUserAttributesUseCase userAttributesUseCase;
    private final GetSellerUserAttributesUseCase sellerUserAttributesUseCase;

    private final DrawerDataListener viewListener;

    public DrawerDataManagerImpl(DrawerDataListener viewListener,
                                 TokoCashUseCase tokoCashUseCase,
                                 GetUserAttributesUseCase uaUseCase,
                                 GetSellerUserAttributesUseCase sellerAttributeUseCase) {
        this.viewListener = viewListener;
        this.tokoCashUseCase = tokoCashUseCase;
        this.userAttributesUseCase = uaUseCase;
        this.sellerUserAttributesUseCase = sellerAttributeUseCase;
    }


    @Override
    public void getTokoCash() {
        tokoCashUseCase.execute(RequestParams.EMPTY, new TokoCashSubscriber(viewListener));
    }

    @Override
    public void unsubscribe() {
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
        if (viewListener == null || viewListener.getActivity() == null || sessionHandler == null) {
            return;
        }

        String query = GraphqlHelper.loadRawString(viewListener.getActivity().getResources(), R.raw.consumer_drawer_data_query);
        userAttributesUseCase.execute(userAttributesUseCase.getUserAttrParam(sessionHandler.getLoginID(), query), new Subscriber<UserData>() {
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
            public void onNext(UserData response) {
                if (viewListener.getActivity() == null) {
                    return;
                }

                renderSaldo(response);
                renderWallet(response);
                renderNotification(response.getNotifications());
                renderProfile(response);
                renderProfileCompletion(response);
            }
        });
    }

    /**
     * Fetch and render data in case of seller app
     *
     * @param sessionHandler
     */

    @Override
    public void getSellerUserAttributes(SessionHandler sessionHandler) {
        if (viewListener == null || viewListener.getActivity() == null || sessionHandler == null) {
            return;
        }

        String query = GraphqlHelper.loadRawString(viewListener.getActivity().getResources(), R.raw.seller_drawer_data_query);
        sellerUserAttributesUseCase.execute(sellerUserAttributesUseCase.getUserAttrParam(sessionHandler.getLoginID(), query), new Subscriber<UserData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(UserData response) {
                renderSaldo(response);
                renderProfile(response);
                renderNotification(response.getNotifications());

                //update values in session handler
                SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
                if (response.getProfile() != null && response.getProfile().getProfilePicture() != null) {
                    sessionHandler.setProfilePicture(response.getProfile().getProfilePicture());
                }
            }
        });
    }

    private void renderProfileCompletion(UserData response) {
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

        //render profile completion
        if (response.getProfile() != null) {
            viewListener.onSuccessGetProfileCompletion(response.getProfile().getCompletion());
        }
    }

    private void renderSaldo(UserData response) {
        if (response.getSaldo() != null) {
            long balance = response.getSaldo().getDeposit();
            String depositFormat = CurrencyFormatUtil.convertPriceValueToIdrFormat(balance, false);
            if (!TextUtils.isEmpty(depositFormat) && !depositFormat.equalsIgnoreCase("ERROR FAIL")) {
                DrawerDeposit drawerDeposit = new DrawerDeposit();
                drawerDeposit.setDeposit(depositFormat);
                viewListener.onGetDeposit(drawerDeposit);
            } else {
                viewListener.onErrorGetDeposit(depositFormat);
            }
        }
    }

    private void renderWallet(UserData response) {
        if (response.getWallet() != null) {
            viewListener.onGetTokoCash(TokoCashUtil.convertToViewModel(response.getWallet(), viewListener.getActivity()));
        } else {
            viewListener.onErrorGetTokoCash(ErrorHandler.getErrorMessage(new IOException()));
        }
    }


    private void renderProfile(UserData response) {
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
    }

    private void renderNotification(Notifications notificationData) {
        if (notificationData != null) {
            try {
                viewListener.onGetNotificationDrawer(
                        convertToViewModel(notificationData));
            } catch (Exception ex) {
                viewListener.onErrorGetNotificationDrawer(
                        viewListener.getString(R.string.default_request_error_unknown));
            }
        } else {
            viewListener.onErrorGetNotificationDrawer(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    private DrawerNotification convertToViewModel(Notifications notificationData) {
        int unreads = notificationData.getChat() != null ? notificationData.getChat().getUnreads() : 0;
        int inboxMessage = notificationData.getInbox() != null ? 0 : notificationData.getInbox().getInboxMessage();

        DrawerNotification drawerNotification = new DrawerNotification();
        drawerNotification.setInboxMessage(unreads);
        drawerNotification.setInboxResCenter(notificationData.getResolution());

        if (notificationData.getInbox() != null) {
            drawerNotification.setInboxReview(notificationData.getInbox().getInboxReputation());
            drawerNotification.setInboxTalk(notificationData.getInbox().getInboxTalk());
            drawerNotification.setInboxTicket(notificationData.getInbox().getInboxTicket());
        }

        drawerNotification.setPurchaseDeliveryConfirm(notificationData.getPurchase().getPurchaseDeliveryConfirm());
        drawerNotification.setPurchaseOrderStatus(notificationData.getPurchase().getPurchaseOrderStatus());
        drawerNotification.setPurchasePaymentConfirm(notificationData.getPurchase().getPurchasePaymentConfirm());
        drawerNotification.setPurchaseReorder(notificationData.getPurchase().getPurchaseReorder());

        drawerNotification.setSellingNewOrder(notificationData.getSales().getSalesNewOrder());
        drawerNotification.setSellingShippingConfirmation(notificationData.getSales().getSalesShippingConfirm());
        drawerNotification.setSellingShippingStatus(notificationData.getSales().getSalesShippingStatus());
        drawerNotification.setIncrNotif(notificationData.getIncrNotif());
        drawerNotification.setTotalCart(notificationData.getTotalCart());

        drawerNotification.setTotalNotif(notificationData.getTotalNotif() - inboxMessage + unreads);
        return drawerNotification;
    }
}
