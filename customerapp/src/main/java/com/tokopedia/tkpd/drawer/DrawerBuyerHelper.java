package com.tokopedia.tkpd.drawer;


import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.drawer2.DrawerAdapter;
import com.tokopedia.core.drawer2.DrawerHelper;
import com.tokopedia.core.drawer2.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.drawer2.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.model.DrawerGroup;
import com.tokopedia.core.drawer2.model.DrawerItem;
import com.tokopedia.core.drawer2.model.DrawerSeparator;
import com.tokopedia.core.drawer2.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;

import butterknife.BindView;

import static android.R.attr.data;


/**
 * Created by nisie on 1/11/17.
 */

public class DrawerBuyerHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerHeaderDataBinder.DrawerHeaderListener{

    @BindView(R.id.label)
    protected TextView shopName;

    @BindView(R.id.sublabel)
    protected TextView shopLabel;

    @BindView(R.id.icon)
    protected ImageView shopIcon;

    @BindView(R.id.drawer_shop)
    protected View shopLayout;

    @BindView(R.id.drawer_footer_shadow)
    protected View footerShadow;

    public DrawerBuyerHelper(Activity activity) {
        super(activity);
    }

    public static DrawerBuyerHelper createInstance(Activity activity) {
        return new DrawerBuyerHelper(activity);
    }

    @Override
    protected ArrayList<DrawerItem> createDrawerData() {
        ArrayList data = new ArrayList();

        data.add(new DrawerItem("Beranda", R.drawable.icon_home, TkpdState.DrawerPosition.INDEX_HOME, true));
        data.add(new DrawerItem("Wishlist", R.drawable.icon_wishlist, TkpdState.DrawerPosition.INDEX_HOME, true));
        data.add(getInboxMenu());
        data.add(getBuyerMenu());
        data.add(getSellerMenu());
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
    public void onItemClicked(DrawerItem item) {
        switch (item.getPosition()) {
            case TkpdState.DrawerPosition.INBOX_MESSAGE:
                Log.d("NISNIS", "FROM DRAWERBUYERHELPER");
                break;
            default:
                super.onItemClicked(item);
        }

    }

    @Override
    public boolean isOpened() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void setData(DrawerData drawerData) {
        adapter.getHeader().setData(drawerData);
        adapter.getHeader().notifyDataSetChanged();

        setNotification(drawerData.getDrawerNotification());
        adapter.notifyDataSetChanged();
    }

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


}
