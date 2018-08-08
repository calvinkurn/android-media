package com.tokopedia.core.drawer.model;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by nisie on 11/23/16.
 */

public class DrawerItem extends RecyclerViewItem {

    public int id = 0;
    public String label = "";
    public int iconId;
    public int notif = 0;
    public boolean isExpanded = false;

    public DrawerItem(String label, int notif, int iconId, int id, boolean isExpanded) {
        setType(TkpdState.DrawerItem.TYPE_ITEM);
        this.id = id;
        this.label = label;
        this.notif = notif;
        this.iconId = iconId;
        this.isExpanded = isExpanded;
    }
}
