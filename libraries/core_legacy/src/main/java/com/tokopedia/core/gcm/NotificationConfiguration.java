package com.tokopedia.core.gcm;

import android.net.Uri;

/**
 * Created by alvarisi on 3/8/17.
 */

public class NotificationConfiguration {
    private static final int GENERAL_NOTIFICATION_ID = 100;
    private boolean bell;
    private boolean vibrate;
    private boolean networkIcon;
    private Uri soundUri;
    private long[] vibratePattern;
    private int notificationId;

    public NotificationConfiguration() {
        bell = false;
        vibrate = false;
        networkIcon = false;
        vibratePattern = new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500};
        notificationId = GENERAL_NOTIFICATION_ID;
    }

    public boolean isBell() {
        return bell;
    }

    public void setBell(boolean bell) {
        this.bell = bell;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isNetworkIcon() {
        return networkIcon;
    }

    public void setNetworkIcon(boolean networkIcon) {
        this.networkIcon = networkIcon;
    }

    public Uri getSoundUri() {
        return soundUri;
    }

    public void setSoundUri(Uri soundUri) {
        this.soundUri = soundUri;
    }

    public long[] getVibratePattern() {
        return vibratePattern;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
