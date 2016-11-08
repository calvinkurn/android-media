package com.tokopedia.core.drawer.var;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;


/**
 * Created by Nisie on 5/05/15.
 */
public class NotificationItem {

    //PURCHASE
    public int payment_conf = 0; //Konfirmasi Pembayaran
    public int payment_confirmed = 0; //Ubah Pembayaran
    public int order_status = 0; //Status Pembayaran
    public int delivery_confirm = 0; //Konfirmasi Penerimaan
    public int reorder = 0; //Transaksi Dibatalkan

    //SALES
    public int new_order = 0; //Order Baru
    public int shipping_confirm = 0; //Konfirmasi Pengiriman
    public int shipping_status = 0; //Status Pengiriman

    //INBOX
    public int message = 0; //Pesan
    public int talk = 0; //Diskusi
    public int reputation = 0; //Ulasan
    public int ticket = 0; // Layanan Pengguna
    public int resolution = 0; //Pusat Resolusi

    public int inc_notif = 0;
    public int total_notif = 0;
    public int total_cart = 0;
    private LocalCacheHandler CacheNotif;

    private Context context;

    public NotificationItem(Context context) {
        this.context = context;
        CacheNotif = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
    }

    public int getNotifPurchase() {
        return CacheNotif.getInt("payment_conf") + CacheNotif.getInt("payment_confirmed") +
                CacheNotif.getInt("order_status") + CacheNotif.getInt("delivery_confirm") +
                CacheNotif.getInt("reorder");
    }

    public int getNotifShop() {
        return CacheNotif.getInt("new_order") + CacheNotif.getInt("shipping_confirm") +
                CacheNotif.getInt("shipping_status");
    }

    public int getNotifMessage() {
        return CacheNotif.getInt("message") + CacheNotif.getInt("talk")
                + CacheNotif.getInt("reputation")
                + CacheNotif.getInt("ticket") + CacheNotif.getInt("resolution");
    }

    public void setNotifToCache() {

        CacheNotif.putInt("payment_conf", payment_conf);
        CacheNotif.putInt("payment_confirmed", payment_confirmed);
        CacheNotif.putInt("order_status", order_status);
        CacheNotif.putInt("delivery_confirm", delivery_confirm);
        CacheNotif.putInt("reorder", reorder);

        CacheNotif.putInt("new_order", new_order);
        CacheNotif.putInt("shipping_confirm", shipping_confirm);
        CacheNotif.putInt("shipping_status", shipping_status);

        CacheNotif.putInt("message", message);
        CacheNotif.putInt("talk", talk);
        CacheNotif.putInt("reputation", reputation);
        CacheNotif.putInt("ticket", ticket);
        CacheNotif.putInt("resolution", resolution);

        CacheNotif.putInt("inc_notif", inc_notif);
        CacheNotif.putInt("total_notif", total_notif);
        CacheNotif.putInt(TkpdCache.Key.TOTAL_CART, total_cart);
        if(total_cart > 0){
            CacheNotif.putInt(TkpdCache.Key.IS_HAS_CART, 1);
        }
        CacheNotif.putLong("expiry", System.currentTimeMillis() / 1000);

        CacheNotif.applyEditor();
    }

    public int getTotalNotif() {
        return CacheNotif.getInt("total_notif");
    }

    public boolean isUnread() {
        return CacheNotif.getInt("inc_notif") > 0;
    }
}
