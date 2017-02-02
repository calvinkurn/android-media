package com.tokopedia.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;

/**
 * @author anggaprasetiyo on 1/6/17.
 */
public class CartBadgeNotificationReceiver extends BroadcastReceiver {
    public static final String ACTION
            = CartBadgeNotificationReceiver.class.getCanonicalName() + ".ACTION";
    private static final String EXTRA_HAS_CART = "EXTRA_HAS_CART";
    private ActionListener actionListener;

    public CartBadgeNotificationReceiver(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ACTION)) {
            boolean hasCart = intent.getBooleanExtra(EXTRA_HAS_CART, false);
            LocalCacheHandler cacheHandler = new LocalCacheHandler(
                    context, TkpdCache.NOTIFICATION_DATA
            );
            cacheHandler.putInt(TkpdCache.Key.IS_HAS_CART, hasCart ? 1 : 0);
            cacheHandler.applyEditor();
            if (actionListener != null) actionListener.onRefreshBadgeCart();
        }
    }

    public interface ActionListener {
        void onRefreshBadgeCart();
    }

    public static void resetBadgeCart(Context context) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_HAS_CART, false);
        context.sendBroadcast(intent);
    }
}
