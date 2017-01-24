package com.tokopedia.core.drawer2.viewmodel;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerData {
    private DrawerProfile drawerProfile;
    private DrawerNotification drawerNotification;

    public DrawerData() {
    }

    public DrawerData(DrawerProfile drawerProfile, DrawerNotification drawerNotification) {
        this.drawerNotification = drawerNotification;
        this.drawerProfile = drawerProfile;
    }

    public DrawerProfile getDrawerProfile() {
        return drawerProfile;
    }

    public void setDrawerProfile(DrawerProfile drawerProfile) {
        this.drawerProfile = drawerProfile;
    }

    public DrawerNotification getDrawerNotification() {
        return drawerNotification;
    }

    public void setDrawerNotification(DrawerNotification drawerNotification) {
        this.drawerNotification = drawerNotification;
    }
}
