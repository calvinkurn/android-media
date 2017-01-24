package com.tokopedia.core.drawer2.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerGroup extends DrawerItem {

    public List<DrawerItem> list = new ArrayList<>();

    public DrawerGroup(String label, int iconId, int id) {
        super(label, iconId, id, false);
    }

    public List<DrawerItem> getList() {
        return list;
    }

    public void setList(List<DrawerItem> list) {
        this.list = list;
    }

    public void add(DrawerItem drawerItem) {
        this.list.add(drawerItem);
    }
}
