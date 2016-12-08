package com.tokopedia.core.app;

import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.drawer.DrawerVariable;

/**
 * Created by sebastianuskh on 12/8/16.
 */
public interface TkpdCoreListener {
    DrawerVariable getDrawer(AppCompatActivity activity);
}
