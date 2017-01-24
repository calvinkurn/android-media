package com.tokopedia.core.drawer2.model;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerItem {

    public String label = "";
    public int iconId;
    public int notif = 0;
    public boolean isExpanded = false;
    public int position;

    public DrawerItem(String label, int iconId, int position, boolean isExpanded) {
        this.position = position;
        this.label = label;
        this.iconId = iconId;
        this.isExpanded = isExpanded;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getNotif() {
        return notif;
    }

    public void setNotif(int notif) {
        this.notif = notif;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
