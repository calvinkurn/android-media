package com.tokopedia.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;

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
                    context, DrawerHelper.DRAWER_CACHE
            );
            cacheHandler.putInt(DrawerNotification.IS_HAS_CART, hasCart ? 1 : 0);
            cacheHandler.applyEditor();
            if (actionListener != null) actionListener.onRefreshBadgeCart();
        }
    }

    public interface ActionListener {
        void onRefreshBadgeCart();
    }
}
