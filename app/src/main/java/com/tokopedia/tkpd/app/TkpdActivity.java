package com.tokopedia.tkpd.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.localytics.android.Localytics;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.tkpd.GCMListenerService.NotificationListener;
import com.tokopedia.tkpd.drawer.DrawerVariable;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.TkpdCache;

/**
 * Created by Nisie on 31/08/15.
 */
public class TkpdActivity extends TActivity implements NotificationListener {

    private Boolean isLogin;

    @Override
    public void onStart() {
        super.onStart();
        isLogin = SessionHandler.isV4Login(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.createToolbarWithDrawer();
        drawer.setEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetNotif() {

    }

    protected void RefreshDrawer() {
        drawer.updateData();
    }

    @Override
    protected void onPause() {
        MainApplication.setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TkpdActivity.class.getSimpleName(), "on resume");
        super.onResume();
    }

	@Override
	public void onRefreshCart(int status) {
		LocalCacheHandler Cache = new LocalCacheHandler(this, TkpdCache.NOTIFICATION_DATA);
		Cache.putInt(TkpdCache.Key.IS_HAS_CART, status);
		Cache.applyEditor();
		invalidateOptionsMenu();
		MainApplication.resetCartStatus(false);
	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Localytics.onNewIntent(this, intent);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isOpened()){
            drawer.closeDrawer();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawer.unsubscribe();
    }

    public void setDrawerEnabled(boolean isEnabled) {
        this.drawer.setEnabled(isEnabled);
    }
}
