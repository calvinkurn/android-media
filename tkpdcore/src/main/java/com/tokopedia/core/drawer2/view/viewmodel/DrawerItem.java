
package com.tokopedia.core.drawer2.view.viewmodel;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerItem {

    public String label = "";
    public int iconId;
    public int notif = 0;
    public boolean isExpanded = false;
    public int id;
    private int position;
    private boolean isNew;

    public DrawerItem(String label, int iconId, int id, boolean isExpanded,boolean isNew) {
        this.id = id;
        this.label = label;
        this.iconId = iconId;
        this.isExpanded = isExpanded;
        this.notif = 0;
        this.isNew=isNew;
    }

    public DrawerItem(String label, int id, boolean isExpanded,boolean isNew) {
        this.id = id;
        this.label = label;
        this.isExpanded = isExpanded;
        this.notif = 0;
        this.isNew=isNew;
    }

    public DrawerItem(String label, int iconId, int id, boolean isExpanded) {
        this.id = id;
        this.label = label;
        this.iconId = iconId;
        this.isExpanded = isExpanded;
        this.notif = 0;
    }

    public DrawerItem(String label, int id, boolean isExpanded) {
        this.id = id;
        this.label = label;
        this.iconId = 0;
        this.isExpanded = isExpanded;
        this.notif = 0;
    }

    public DrawerItem(String label, int iconId, int id, boolean isExpanded, int notif) {
        this.id = id;
        this.label = label;
        this.iconId = iconId;
        this.isExpanded = isExpanded;
        this.notif = notif;
    }

    public DrawerItem(String label, int id, boolean isExpanded, int notif) {
        this.id = id;
        this.label = label;
        this.iconId = 0;
        this.isExpanded = isExpanded;
        this.notif = notif;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public boolean isNew() {return isNew;}

    public void setNew(boolean aNew) {isNew = aNew;}
}
