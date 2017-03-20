package com.tokopedia.core.gcm;

import android.net.Uri;

/**
 * Created by alvarisi on 3/8/17.
 */

public class NotificationConfiguration {
    private boolean bell;
    private boolean vibrate;
    private boolean networkIcon;
    private Uri soundUri;
    private long[] vibratePattern;

    public NotificationConfiguration() {
        bell = false;
        vibrate = false;
        networkIcon = false;
        vibratePattern = new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500};
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
}
