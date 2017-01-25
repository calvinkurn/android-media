package com.tokopedia.core.drawer2.viewmodel;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerData {
    private DrawerProfile drawerProfile;
    private DrawerNotification drawerNotification;
    private DrawerTokoCash drawerTokoCash;
    private DrawerTopPoints drawerTopPoints;
    private DrawerDeposit drawerDeposit;

    public DrawerData() {
        this.drawerDeposit = new DrawerDeposit();
        this.drawerProfile = new DrawerProfile();
        this.drawerTokoCash = new DrawerTokoCash();
        this.drawerNotification = new DrawerNotification();
        this.drawerTopPoints = new DrawerTopPoints();
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

    public DrawerTokoCash getDrawerTokoCash() {
        return drawerTokoCash;
    }

    public void setDrawerTokoCash(DrawerTokoCash drawerTokoCash) {
        this.drawerTokoCash = drawerTokoCash;
    }

    public DrawerTopPoints getDrawerTopPoints() {
        return drawerTopPoints;
    }

    public void setDrawerTopPoints(DrawerTopPoints drawerTopPoints) {
        this.drawerTopPoints = drawerTopPoints;
    }

    public DrawerDeposit getDrawerDeposit() {
        return drawerDeposit;
    }

    public void setDrawerDeposit(DrawerDeposit drawerDeposit) {
        this.drawerDeposit = drawerDeposit;
    }
}
