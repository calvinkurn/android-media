package com.tokopedia.core.drawer.model;

import com.tokopedia.core.var.TkpdState;

/**
 * Created by nisie on 11/23/16.
 */

public class DrawerSeparator extends DrawerItem {
    public DrawerSeparator() {
        super("", 0, 0, 0, false);
        setType(TkpdState.DrawerItem.TYPE_SEPARATOR);
    }
}