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
import com.tokopedia.core.EtalaseShopEditor;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.DrawerAdapter;
import com.tokopedia.core.drawer2.DrawerHelper;
import com.tokopedia.core.drawer2.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.drawer2.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.model.DrawerGroup;
import com.tokopedia.core.drawer2.model.DrawerItem;
import com.tokopedia.core.drawer2.model.DrawerSeparator;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerBuyerHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerHeaderDataBinder.DrawerHeaderListener {

    private TextView shopName;
    private TextView shopLabel;

    private ImageView shopIcon;
    private View shopLayout;
    private View footerShadow;

    private SessionHandler sessionHandler;

    public DrawerBuyerHelper(Activity activity, SessionHandler sessionHandler) {
        super(activity);
        this.sessionHandler = sessionHandler;
        shopName = (TextView) activity.findViewById(R.id.label);
        shopLabel = (TextView) activity.findViewById(R.id.sublabel);
        shopIcon = (ImageView) activity.findViewById(R.id.icon);
        shopLayout = activity.findViewById(R.id.drawer_shop);
        footerShadow = activity.findViewById(R.id.drawer_footer_shadow);
    }

    public static DrawerBuyerHelper createInstance(Activity activity,
                                                   SessionHandler sessionHandler) {
        return new DrawerBuyerHelper(activity, sessionHandler);
    }

    @Override
    protected ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();

        if (sessionHandler.isV4Login()) {
            createDataLogin(data);
            shopLayout.setVisibility(View.VISIBLE);
            footerShadow.setVisibility(View.VISIBLE);
        } else {
            createDataGuest(data);
            shopLayout.setVisibility(View.GONE);
            footerShadow.setVisibility(View.GONE);
        }
        return data;
    }

    private void createDataGuest(ArrayList<DrawerItem> data) {
        data.add(new DrawerItem("Beranda", 0, TkpdState.DrawerPosition.INDEX_HOME, true));
        data.add(new DrawerItem("Masuk", 0, TkpdState.DrawerPosition.LOGIN, true));
        data.add(new DrawerItem("Daftar", 0, TkpdState.DrawerPosition.REGISTER, true));
        if (GlobalConfig.isAllowDebuggingTools()) {
            data.add(new DrawerItem("Developer Options",
                    android.R.drawable.stat_sys_warning,
                    TkpdState.DrawerPosition.DEVELOPER_OPTIONS, true));
        }

    }

    private void createDataLogin(ArrayList<DrawerItem> data) {
        data.add(new DrawerItem("Beranda",
                R.drawable.icon_home,
                TkpdState.DrawerPosition.INDEX_HOME,
                true));
        data.add(new DrawerItem("Wishlist",
                R.drawable.icon_wishlist,
                TkpdState.DrawerPosition.WISHLIST,
                true));
        data.add(getInboxMenu());
        data.add(getBuyerMenu());
        if (!sessionHandler.getShopID(context).equals("0")
                && !sessionHandler.getShopID(context).equals("")) {
            data.add(getSellerMenu());
            data.add(new DrawerItem("Gold Merchant",
                    R.drawable.ic_goldmerchant_drawer,
                    TkpdState.DrawerPosition.GOLD_MERCHANT,
                    false));
        }
        data.add(new DrawerItem("Pengaturan",
                R.drawable.icon_setting,
                TkpdState.DrawerPosition.SETTINGS,
                true));
        if (!TrackingUtils.getBoolean(AppEventTracking.GTM.CONTACT_US)) {
            data.add(new DrawerItem("Hubungi Kami",
                    R.drawable.ic_contact_us,
                    TkpdState.DrawerPosition.CONTACT_US,
                    true));
        }
        data.add(new DrawerItem("Keluar",
                R.drawable.ic_menu_logout,
                TkpdState.DrawerPosition.LOGOUT,
                true));
        if (GlobalConfig.isAllowDebuggingTools()) {
            data.add(new DrawerItem("Developer Options",
                    0,
                    TkpdState.DrawerPosition.DEVELOPER_OPTIONS,
                    true));
        }
    }

    private DrawerGroup getSellerMenu() {
        DrawerGroup sellerMenu = new DrawerGroup("Penjualan",
                R.drawable.icon_penjualan,
                TkpdState.DrawerPosition.SHOP,
                drawerCache.getBoolean("Penjualan" + "isExpanded", false),
                getTotalSellerNotif());
        sellerMenu.add(new DrawerItem("Order Baru",
                0,
                TkpdState.DrawerPosition.SHOP_NEW_ORDER,
                drawerCache.getBoolean("Penjualan" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER)));
        sellerMenu.add(new DrawerItem("Konfirmasi Pengiriman",
                0,
                TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING,
                drawerCache.getBoolean("Penjualan" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION)));
        sellerMenu.add(new DrawerItem("Status Pengiriman",
                0,
                TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS,
                drawerCache.getBoolean("Penjualan" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS)));
        sellerMenu.add(new DrawerItem("Daftar Penjualan",
                0,
                TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST,
                drawerCache.getBoolean("Penjualan" + "isExpanded", false)));
        sellerMenu.add(new DrawerSeparator(drawerCache.getBoolean("Penjualan" + "isExpanded", false)));
        sellerMenu.add(new DrawerItem("Daftar Produk",
                0,
                TkpdState.DrawerPosition.MANAGE_PRODUCT,
                drawerCache.getBoolean("Penjualan" + "isExpanded", false)));
        sellerMenu.add(new DrawerItem("Etalase Toko",
                0,
                TkpdState.DrawerPosition.MANAGE_ETALASE,
                drawerCache.getBoolean("Penjualan" + "isExpanded", false)));

        return sellerMenu;
    }

    private DrawerGroup getBuyerMenu() {
        DrawerGroup buyerMenu = new DrawerGroup("Pembelian",
                R.drawable.icon_pembelian,
                TkpdState.DrawerPosition.PEOPLE,
                drawerCache.getBoolean("Pembelian" + "isExpanded", false),
                getTotalBuyerNotif());
        buyerMenu.add(new DrawerItem("Status Pembayaran",
                0,
                TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS,
                drawerCache.getBoolean("Pembelian" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM)));
        buyerMenu.add(new DrawerItem("Status Pemesanan",
                0,
                TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS,
                drawerCache.getBoolean("Pembelian" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_ORDER_STATUS)));
        buyerMenu.add(new DrawerItem("Konfirmasi Penerimaan",
                0,
                TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING,
                drawerCache.getBoolean("Pembelian" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM)));
        buyerMenu.add(new DrawerItem("Transaksi Dibatalkan",
                0,
                TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED,
                drawerCache.getBoolean("Pembelian" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_REORDER)));
        buyerMenu.add(new DrawerItem("Daftar Pembelian",
                0,
                TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST,
                drawerCache.getBoolean("Pembelian" + "isExpanded", false)));
        return buyerMenu;
    }

    private DrawerGroup getInboxMenu() {
        DrawerGroup inboxMenu = new DrawerGroup("Kotak Masuk",
                R.drawable.icon_inbox,
                TkpdState.DrawerPosition.INBOX,
                drawerCache.getBoolean("Kotak Masuk" + "isExpanded", false),
                getTotalInboxNotif());
        inboxMenu.add(new DrawerItem("Pesan",
                0,
                TkpdState.DrawerPosition.INBOX_MESSAGE,
                drawerCache.getBoolean("Kotak Masuk" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE)));
        inboxMenu.add(new DrawerItem("Diskusi Produk",
                0,
                TkpdState.DrawerPosition.INBOX_TALK,
                drawerCache.getBoolean("Kotak Masuk" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK)));
        inboxMenu.add(new DrawerItem("Ulasan",
                0,
                TkpdState.DrawerPosition.INBOX_REVIEW,
                drawerCache.getBoolean("Kotak Masuk" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW)));
        inboxMenu.add(new DrawerItem("Layanan Pengguna",
                0,
                TkpdState.DrawerPosition.INBOX_TICKET,
                drawerCache.getBoolean("Kotak Masuk" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET)));
        inboxMenu.add(new DrawerItem("Pusat Resolusi",
                0,
                TkpdState.DrawerPosition.RESOLUTION_CENTER,
                drawerCache.getBoolean("Kotak Masuk" + "isExpanded", false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER)));
        return inboxMenu;
    }

    private int getTotalInboxNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET, 0);
    }

    private int getTotalBuyerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_REORDER, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_ORDER_STATUS, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM, 0);
    }

    private int getTotalSellerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, 0);
    }

    @Override
    public boolean isOpened() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private boolean hasShop(DrawerProfile drawerProfile) {
        return !drawerProfile.getShopName().equals("");
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
    public void setSelectedPosition(int id) {
        adapter.setSelectedItem(id);
    }

    @Override
    public void initDrawer(Activity activity) {
        this.adapter = DrawerAdapter.createAdapter(activity, this, drawerCache);
        this.adapter.setData(createDrawerData());
        this.adapter.setHeader(new DrawerHeaderDataBinder(adapter, activity, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        setActionToolbar(activity);
        setExpand();
        closeDrawer();
    }

    private void setExpand() {
        if (drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false)) {
            DrawerGroup group = findGroup(TkpdState.DrawerPosition.INBOX);
            if (group != null)
                adapter.onGroupClicked(group, group.getPosition());
        }
        if (drawerCache.getBoolean(DrawerAdapter.IS_PEOPLE_OPENED, false)) {
            DrawerGroup group = findGroup(TkpdState.DrawerPosition.PEOPLE);
            if (group != null)
                adapter.onGroupClicked(group, group.getPosition());
        }
        if (drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false)) {
            DrawerGroup group = findGroup(TkpdState.DrawerPosition.SHOP);
            if (group != null)
                adapter.onGroupClicked(group, group.getPosition());
        }
    }

    private DrawerGroup findGroup(int id) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (adapter.getData().get(i) instanceof DrawerGroup
                    && adapter.getData().get(i).getId() == id) {
                adapter.getData().get(i).setPosition(i);
                return (DrawerGroup) adapter.getData().get(i);
            }
        }
        return null;
    }

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
        Intent intent;
        switch (item.getId()) {
            case TkpdState.DrawerPosition.WISHLIST:
                Intent wishList = SimpleHomeRouter
                        .getSimpleHomeActivityIntent(context, SimpleHomeRouter.WISHLIST_FRAGMENT);

                context.startActivity(wishList);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.WISHLIST);
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
                if (context.getApplication() instanceof TkpdCoreRouter) {
                    ((TkpdCoreRouter) context.getApplication()).goToManageProduct(context);
                }
                break;
            case TkpdState.DrawerPosition.MANAGE_ETALASE:
                startIntent(context, EtalaseShopEditor.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISPLAY);
                break;
            case TkpdState.DrawerPosition.GOLD_MERCHANT:
                if (context.getApplication() instanceof TkpdCoreRouter) {
                    ((TkpdCoreRouter) context.getApplication())
                            .goToMerchantRedirect(context);
                }
                break;
            default:
                super.onItemClicked(item);
        }

//        if (item.getId() != TkpdState.DrawerPosition.INDEX_HOME) {
//            context.finish();
//        }
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
