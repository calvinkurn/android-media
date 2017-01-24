package com.tokopedia.core.app;

import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.drawer2.DrawerHelper;

/**
 * Created by sebastianuskh on 12/8/16.
 */
public interface TkpdCoreRouter {
    DrawerHelper getDrawer(AppCompatActivity activity);

}
