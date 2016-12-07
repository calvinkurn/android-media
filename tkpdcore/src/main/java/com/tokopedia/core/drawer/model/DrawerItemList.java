package com.tokopedia.core.drawer.model;

import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 11/23/16.
 */

public class DrawerItemList extends DrawerItem {

    public List<DrawerItem> list = new ArrayList<>();
    public boolean isExpandable = false;

    public DrawerItemList(String label, int notif, int iconId, int id, boolean isExpandable) {
        super(label, notif, iconId, id, false);
        setType(TkpdState.DrawerItem.TYPE_LIST);
        this.isExpandable = isExpandable;
    }
}