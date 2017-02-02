package com.tokopedia.core.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.drawer.DrawerVariable;

/**
 * Created by sebastianuskh on 12/8/16.
 */
public interface TkpdCoreRouter {
    DrawerVariable getDrawer(AppCompatActivity activity);

    void goToWallet(Context context, Bundle bundle);
}
