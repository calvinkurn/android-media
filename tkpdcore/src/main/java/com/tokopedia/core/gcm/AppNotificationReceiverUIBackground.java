package com.tokopedia.core.gcm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.Cart;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.notification.dedicated.NewDiscussionNotification;
import com.tokopedia.core.gcm.notification.dedicated.NewMessageNotification;
import com.tokopedia.core.gcm.notification.dedicated.NewOrderNotification;
import com.tokopedia.core.gcm.notification.dedicated.NewReviewNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchaseAcceptedNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchaseDeliveredNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchaseDisputeNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchasePartialProcessedNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchaseRejectedNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchaseVerifiedNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToSellerEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToSellerNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterAdminBuyerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterAdminSellerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterBuyerAgreeNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterBuyerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterNewNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterSellerAgreeNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterSellerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReviewEditedNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReviewReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.TicketResponseNotification;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_IMAGE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_URL;

/**
 * @author by alvarisi on 1/9/17.
 */
@Deprecated
public class AppNotificationReceiverUIBackground {
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    private FCMCacheManager cacheManager;
    private Context mContext;
    private BuildAndShowNotification mBuildAndShowNotification;

    public AppNotificationReceiverUIBackground(Application application) {
        cacheManager = new FCMCacheManager(application.getBaseContext());
        mContext = application.getBaseContext();
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mBuildAndShowNotification = new BuildAndShowNotification(mContext);
    }

    void prepareAndExecuteDedicationNotification(Bundle data) {
        Map<Integer, Visitable> dedicatedNotification = new HashMap<>();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_MESSAGE, new NewMessageNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TALK, new NewDiscussionNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW, new NewReviewNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_EDIT, new ReviewEditedNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_REPLY, new ReviewReplyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TICKET, new TicketResponseNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RES_CENTER, new ResCenterNewNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_NEWORDER, new NewOrderNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY, new ReputationSmileyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY, new ReputationSmileyEditNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER, new ReputationSmileyToBuyerNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER, new ReputationSmileyToBuyerEditNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER, new ReputationSmileyToSellerNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER, new ReputationSmileyToSellerEditNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED, new PurchaseVerifiedNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED, new PurchaseAcceptedNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED, new PurchasePartialProcessedNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED, new PurchaseRejectedNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED, new PurchaseDeliveredNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE, new PurchaseDisputeNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY, new ResCenterBuyerReplyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY, new ResCenterSellerReplyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE, new ResCenterSellerAgreeNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE, new ResCenterBuyerAgreeNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY, new ResCenterAdminBuyerReplyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY, new ResCenterAdminSellerReplyNotification(mContext));

        Visitable visitable = dedicatedNotification.get(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
        );

        if (visitable != null) {
            visitable.proccessReceivedNotification(data);
        }
    }

    void prepareDeathOrLiveFunction(Bundle data){
        Map<Integer, Class> dedicatedNotification = new HashMap<>();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_MESSAGE,  NewMessageNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TALK,  NewDiscussionNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW,  NewReviewNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_EDIT,  ReviewEditedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_REPLY,  ReviewReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TICKET,  TicketResponseNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RES_CENTER,  ResCenterNewNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_NEWORDER,  NewOrderNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY,  ReputationSmileyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY,  ReputationSmileyEditNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER,  ReputationSmileyToBuyerNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER,  ReputationSmileyToBuyerEditNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER,  ReputationSmileyToSellerNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER,  ReputationSmileyToSellerEditNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED,  PurchaseVerifiedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED,  PurchaseAcceptedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED,  PurchasePartialProcessedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED,  PurchaseRejectedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED,  PurchaseDeliveredNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE,  PurchaseDisputeNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY,  ResCenterBuyerReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY,  ResCenterSellerReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE,  ResCenterSellerAgreeNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE,  ResCenterBuyerAgreeNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY,  ResCenterAdminBuyerReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY,  ResCenterAdminSellerReplyNotification.class);
        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            suchADangerousReflectionFunction(data, clazz);
        }
    }

    private void suchADangerousReflectionFunction(Bundle data, Class<?> clazz) {
        Constructor<?> ctor = null;
        try {
            ctor = clazz.getConstructor(Context.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        Object object = null;
        try {
            object = ctor.newInstance(mContext);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return;
        }

        if(object != null && object instanceof Visitable){
            ((Visitable) object).proccessReceivedNotification(data);
        }
    }

    void prepareAndExecutePromoNotification(Bundle data) {
        Map<Integer, Visitable> promoNotification = new HashMap<>();
        Visitable visitable = promoNotification.get(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
        );

        visitable.proccessReceivedNotification(data);

        Class<?> intentClass = null;
        switch (GCMUtils.getCode(data)) {
            case TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION:
                mBuildAndShowNotification.sendUpdateAppsNotification(data);
                return;
            case TkpdState.GCMServiceState.GCM_PROMO:
                intentClass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                intentClass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_SHOP:
                intentClass = ShopInfoActivity.class;
                break;
            case TkpdState.GCMServiceState.GCM_DEEPLINK:
                if (CustomerRouter.getDeeplinkClass() != null) {
                    intentClass = CustomerRouter.getDeeplinkClass();
                } else {
                    return;
                }
                break;
            case TkpdState.GCMServiceState.GCM_CART:
                if (SessionHandler.isV4Login(mContext)) {
                    intentClass = Cart.class;
                } else {
                    return;
                }
                break;
            case TkpdState.GCMServiceState.GCM_WISHLIST:
                if (SessionHandler.isV4Login(mContext)) {
                    intentClass = SimpleHomeRouter.getSimpleHomeActivityClass();
                } else {
                    return;
                }
                break;
            case TkpdState.GCMServiceState.GCM_VERIFICATION:
                if (SessionHandler.isV4Login(mContext)) {
                    intentClass = ManageGeneral.class;
                } else {
                    data.putInt("keylogin1", TkpdState.DrawerPosition.LOGIN);
                    data.putInt("keylogin2", SessionView.HOME);
                    intentClass = SessionRouter.getLoginActivityClass();
                }
                break;
            default:
                return;
        }

        Intent intent = createIntent(intentClass, data);

        if (data.getInt("keylogin1", -99) != -99) {
            intent.putExtra(
                    com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY,
                    data.getInt("keylogin1")
            );
            intent.putExtra(
                    SessionView.MOVE_TO_CART_KEY,
                    data.getInt("keylogin2")
            );
        }

        NotificationPass notificationPass = new NotificationPass();
        notificationPass.title = data.getString(ARG_NOTIFICATION_TITLE);
        notificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        notificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        notificationPass.componentNameParentStack = null;
        notificationPass.isAllowedBigStyle = true;
        notificationPass.classParentStack = intentClass;

        mBuildAndShowNotification.buildAndShowNotification(notificationPass, data, intent);
    }

    private Intent createIntent(Class<?> intentClass, Bundle data) {
        Intent intent = new Intent(mContext, intentClass);
        if (TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_IMAGE))) {
            data.putString("img_uri", data.getString(ARG_NOTIFICATION_IMAGE, ""));
            data.putString("img_uri_600", data.getString(ARG_NOTIFICATION_IMAGE, ""));
        }

        switch (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))) {
            case TkpdState.GCMServiceState.GCM_HOT_LIST: {
                URLParser urlp = new URLParser(data.getString(ARG_NOTIFICATION_URL));
                data.putString("alias", urlp.getHotAlias());
                break;
            }
            case TkpdState.GCMServiceState.GCM_DEEPLINK: {
                intent.setData(Uri.parse(data.getString(ARG_NOTIFICATION_URL)));
                break;
            }
            case TkpdState.GCMServiceState.GCM_CATEGORY: {
                Uri uri = Uri.parse(data.getString(ARG_NOTIFICATION_URL));
                List<String> linkSegment = uri.getPathSegments();
                String iden = linkSegment.get(1);
                for (int i = 2; i < linkSegment.size(); i++) {
                    iden = iden + "_" + linkSegment.get(i);
                }
                CategoryDB dep =
                        DbManagerImpl.getInstance().getCategoryDb(iden);
                String dep_id = dep.getDepartmentId() + "";
                data.putString("d_id", dep_id);
                data.putInt("state", 0);
                break;
            }
            case TkpdState.GCMServiceState.GCM_SHOP: {
                Uri uri = Uri.parse(data.getString(ARG_NOTIFICATION_URL));
                intent.putExtra("shop_domain", uri.getLastPathSegment());
                break;
            }
            case TkpdState.GCMServiceState.GCM_WISHLIST: {
                data.putInt(SimpleHomeRouter.FRAGMENT_TYPE, SimpleHomeRouter.WISHLIST_FRAGMENT);
                break;
            }
            default:
                break;
        }
        data = this.mNotificationAnalyticsReceiver.buildAnalyticNotificationData(data);
        intent.putExtras(data);
        return intent;
    }
}
