package com.tokopedia.tkpd.drawer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.EtalaseShopEditor;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.drawer2.DrawerAdapter;
import com.tokopedia.core.drawer2.DrawerHelper;
import com.tokopedia.core.drawer2.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.drawer2.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.model.DrawerGroup;
import com.tokopedia.core.drawer2.model.DrawerItem;
import com.tokopedia.core.drawer2.model.DrawerSeparator;
import com.tokopedia.core.drawer2.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.myproduct.ManageProduct;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;


/**
 * Created by nisie on 1/11/17.
 */

public class DrawerBuyerHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerHeaderDataBinder.DrawerHeaderListener {

    protected TextView shopName;
    protected TextView shopLabel;
    protected ImageView shopIcon;
    protected View shopLayout;
    protected View footerShadow;

    public DrawerBuyerHelper(Activity activity) {
        super(activity);
        shopName = (TextView) activity.findViewById(R.id.label);
        shopLabel = (TextView) activity.findViewById(R.id.sublabel);
        shopIcon = (ImageView) activity.findViewById(R.id.icon);
        shopLayout = activity.findViewById(R.id.drawer_shop);
        footerShadow = activity.findViewById(R.id.drawer_footer_shadow);
    }

    public static DrawerBuyerHelper createInstance(Activity activity) {
        return new DrawerBuyerHelper(activity);
    }

    @Override
    protected ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();

        data.add(new DrawerItem("Beranda", R.drawable.icon_home, TkpdState.DrawerPosition.INDEX_HOME, true));
        data.add(new DrawerItem("Wishlist", R.drawable.icon_wishlist, TkpdState.DrawerPosition.WISHLIST, true));
        data.add(getInboxMenu());
        data.add(getBuyerMenu());
        if (!SessionHandler.getShopID(context).equals("0") && !SessionHandler.getShopID(context).equals("")) {
            data.add(getSellerMenu());
        }
        data.add(new DrawerItem("Pengaturan", R.drawable.icon_setting, TkpdState.DrawerPosition.SETTINGS, true));
        if (!TrackingUtils.getBoolean(AppEventTracking.GTM.CONTACT_US)) {
            data.add(new DrawerItem("Hubungi Kami", R.drawable.ic_contact_us, TkpdState.DrawerPosition.CONTACT_US, true));
        }
        data.add(new DrawerItem("Keluar", R.drawable.ic_menu_logout, TkpdState.DrawerPosition.LOGOUT, true));
        if (BuildConfig.DEBUG & MainApplication.isDebug()) {
            data.add(new DrawerItem("Developer Options", android.R.drawable.stat_sys_warning, TkpdState.DrawerPosition.DEVELOPER_OPTIONS, true));
        }
        return data;
    }

    private DrawerGroup getSellerMenu() {
        DrawerGroup sellerMenu = new DrawerGroup("Penjualan", R.drawable.icon_penjualan, TkpdState.DrawerPosition.SHOP);
        sellerMenu.add(new DrawerItem("Order Baru", 0, TkpdState.DrawerPosition.SHOP_NEW_ORDER, false));
        sellerMenu.add(new DrawerItem("Konfirmasi Pengiriman", 0, TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING, false));
        sellerMenu.add(new DrawerItem("Status Pengiriman", 0, TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS, false));
        sellerMenu.add(new DrawerItem("Daftar Penjualan", 0, TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST, false));
        sellerMenu.add(new DrawerSeparator());
        sellerMenu.add(new DrawerItem("Daftar Produk", 0, TkpdState.DrawerPosition.MANAGE_PRODUCT, false));
        sellerMenu.add(new DrawerItem("Etalase Toko", 0, TkpdState.DrawerPosition.MANAGE_ETALASE, false));

        return sellerMenu;
    }

    private DrawerGroup getBuyerMenu() {
        DrawerGroup buyerMenu = new DrawerGroup("Pembelian", R.drawable.icon_pembelian, TkpdState.DrawerPosition.PEOPLE);
        buyerMenu.add(new DrawerItem("Status Pembayaran", 0, TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS, false));
        buyerMenu.add(new DrawerItem("Status Pemesanan", 0, TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS, false));
        buyerMenu.add(new DrawerItem("Konfirmasi Penerimaan", 0, TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING, false));
        buyerMenu.add(new DrawerItem("Transaksi Dibatalkan", 0, TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED, false));
        buyerMenu.add(new DrawerItem("Daftar Pembelian", 0, TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST, false));
        return buyerMenu;
    }

    private DrawerGroup getInboxMenu() {
        DrawerGroup inboxMenu = new DrawerGroup("Kotak Masuk", R.drawable.icon_inbox, TkpdState.DrawerPosition.INBOX);
        inboxMenu.add(new DrawerItem("Pesan", 0, TkpdState.DrawerPosition.INBOX_MESSAGE, false));
        inboxMenu.add(new DrawerItem("Diskusi Produk", 0, TkpdState.DrawerPosition.INBOX_TALK, false));
        inboxMenu.add(new DrawerItem("Ulasan", 0, TkpdState.DrawerPosition.INBOX_REVIEW, false));
        inboxMenu.add(new DrawerItem("Layanan Pengguna", 0, TkpdState.DrawerPosition.INBOX_TICKET, false));
        inboxMenu.add(new DrawerItem("Pusat Resolusi", 0, TkpdState.DrawerPosition.RESOLUTION_CENTER, false));
        return inboxMenu;
    }

    @Override
    public boolean isOpened() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private boolean hasShop(DrawerProfile drawerProfile) {
        return !drawerProfile.getShopName().equals("");
    }

    @Override
    public void setNotification(DrawerNotification drawerNotification) {
        for (DrawerItem item : adapter.getData()) {
            switch (item.getPosition()) {
                case TkpdState.DrawerPosition.INBOX_MESSAGE:
                    item.setNotif(drawerNotification.getInboxMessage());
                    break;
                case TkpdState.DrawerPosition.INBOX_TALK:
                    item.setNotif(drawerNotification.getInboxTalk());
                    break;
                case TkpdState.DrawerPosition.INBOX_REVIEW:
                    item.setNotif(drawerNotification.getInboxReview());
                    break;
                case TkpdState.DrawerPosition.INBOX_TICKET:
                    item.setNotif(drawerNotification.getInboxTicket());
                    break;
                case TkpdState.DrawerPosition.RESOLUTION_CENTER:
                    item.setNotif(drawerNotification.getInboxResCenter());
                    break;
                case TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS:
                    item.setNotif(drawerNotification.getPurchasePaymentConfirm());
                    break;
                case TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS:
                    item.setNotif(drawerNotification.getPurchaseOrderStatus());
                    break;
                case TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING:
                    item.setNotif(drawerNotification.getPurchaseDeliveryConfirm());
                    break;
                case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED:
                    item.setNotif(drawerNotification.getPurchaseReorder());
                    break;
                case TkpdState.DrawerPosition.SHOP_NEW_ORDER:
                    item.setNotif(drawerNotification.getSellingNewOrder());
                    break;
                case TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING:
                    item.setNotif(drawerNotification.getSellingShippingConfirmation());
                    break;
                case TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS:
                    item.setNotif(drawerNotification.getSellingShippingStatus());
                    break;
                default:
                    item.notif = 0;
            }
        }
    }

    @Override
    public void setFooterData(DrawerProfile profile) {
        if (hasShop(profile)) {
            shopName.setText(profile.getShopName());
            shopIcon.setVisibility(View.VISIBLE);
            shopLabel.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(shopIcon, profile.getShopAvatar());
            shopLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGoToShop();
                }
            });
        } else {
            shopName.setText(com.tokopedia.core.R.string.title_create_shop);
            shopIcon.setVisibility(View.GONE);
            shopLabel.setVisibility(View.GONE);
            shopLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGoToCreateShop();
                }
            });
        }
    }

    @Override
    public void initDrawer(Activity activity) {
        this.adapter = DrawerAdapter.createAdapter(activity, this);
        this.adapter.setData(createDrawerData());
        this.adapter.setHeader(new DrawerHeaderDataBinder(adapter, activity, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        setActionToolbar(activity);
        closeDrawer();
    }

//    @Override
//    public ToolbarBuyerHandler.OnDrawerToggleClickListener onDrawerToggleClick() {
//        return new ToolbarBuyerHandler.OnDrawerToggleClickListener() {
//            @Override
//            public void onDrawerToggleClick() {
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    closeDrawer();
//                } else {
//                    openDrawer();
//                }
//            }
//        };
//    }

    @Override
    public void setEnabled(boolean isEnabled) {
        if (isEnabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onItemClicked(DrawerItem item) {
        Boolean isFinish = true;
        switch (item.getPosition()) {
            case TkpdState.DrawerPosition.INDEX_HOME:
                Intent intent = HomeRouter.getHomeActivity(context);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.LOGIN:
            case TkpdState.DrawerPosition.REGISTER:
                intent = SessionRouter.getLoginActivityIntent(context);
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, item.getPosition());
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                context.startActivity(intent);
//                context.finish();
                break;
            case TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS:
                context.startActivity(TransactionPurchaseRouter.createIntentConfirmPayment(context));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PAYMENT_CONFIRMATION);
                break;
            case TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS:
                context.startActivity(TransactionPurchaseRouter.createIntentTxStatus(context));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.ORDER_STATUS);
                break;
            case TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING:
                context.startActivity(TransactionPurchaseRouter.createIntentConfirmShipping(context));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.RECEIVE_CONFIRMATION);
                break;
            case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED:
                context.startActivity(TransactionPurchaseRouter.createIntentTxCanceled(context));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.CANCELLED_ORDER);
                break;
            case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST:
                context.startActivity(TransactionPurchaseRouter.createIntentTxAll(context));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PURCHASE_LIST);
                break;
            case TkpdState.DrawerPosition.SHOP_NEW_ORDER:
                intent = SellerRouter.getActivitySellingTransaction(context);
                Bundle bundle = new Bundle();
                bundle.putInt("tab", 1);
                bundle.putString("user_id", SessionHandler.getLoginID(context));
                intent.putExtras(bundle);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.NEW_ORDER);
                break;
            case TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING:
                intent = SellerRouter.getActivitySellingTransaction(context);
                intent.putExtra("tab", 2);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_CONFIRMATION);
                break;
            case TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS:
                intent = SellerRouter.getActivitySellingTransaction(context);
                intent.putExtra("tab", 3);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_STATUS);
                break;
            case TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST:
                intent = SellerRouter.getActivitySellingTransaction(context);
                intent.putExtra("tab", 4);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SALES_LIST);
                break;
            case TkpdState.DrawerPosition.MANAGE_PRODUCT:
                startIntent(context, ManageProduct.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_LIST);
                break;
            case TkpdState.DrawerPosition.MANAGE_ETALASE:
                startIntent(context, EtalaseShopEditor.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISPLAY);
                break;
            case TkpdState.DrawerPosition.WISHLIST:
                Intent wishList = SimpleHomeRouter
                        .getSimpleHomeActivityIntent(context, SimpleHomeRouter.WISHLIST_FRAGMENT);

                context.startActivity(wishList);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.WISHLIST);
                break;
            case TkpdState.DrawerPosition.SETTINGS:
                context.startActivity(new Intent(context, ManageGeneral.class));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SETTING);
                break;
            case TkpdState.DrawerPosition.CONTACT_US:
                intent = InboxRouter.getContactUsActivityIntent(context);
                if (TrackingUtils.getBoolean(AppEventTracking.GTM.CREATE_TICKET)) {
                    intent.putExtra("link", "https://tokopedia.com/contact-us-android");
                }
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.LOGOUT:
                isFinish = false;
                SessionHandler session = new SessionHandler(context);
                session.Logout(context);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SIGN_OUT);
                break;
            default:
                super.onItemClicked(item);
        }

        if (isFinish && item.getPosition() != TkpdState.DrawerPosition.INDEX_HOME) {
            context.finish();
        }
        closeDrawer();

    }

    @Override
    public void onGoToDeposit() {
        Log.d("NISNIS", "GO TO DEPOSIT");

    }

    @Override
    public void onGoToProfile() {
        Log.d("NISNIS", "GO TO PROFILE");

    }

    @Override
    public void onGoToTopPoints(String topPointsUrl) {
        Log.d("NISNIS", "GO TO TOPPOINTS " + topPointsUrl);

    }

    @Override
    public void onGoToTopCash(String topCashUrl) {
        Log.d("NISNIS", "GO TO TOPCASH " + topCashUrl);

    }

    private void onGoToCreateShop() {
        Log.d("NISNIS", "GO TO CREATE SHOP ");

    }

    private void onGoToShop() {
        Log.d("NISNIS", "GO TO SHOP ");

    }
}
