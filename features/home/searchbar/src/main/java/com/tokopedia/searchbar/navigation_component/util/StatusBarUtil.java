package com.tokopedia.searchbar.navigation_component.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class StatusBarUtil {

    private final WeakReference<Activity> activityWeakReference;
    private boolean isLighStatusBar = true;

    public StatusBarUtil(WeakReference<Activity> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    public void requestStatusBarDark() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && activityWeakReference.get() != null) {
            activityWeakReference.get().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setWindowFlag(activityWeakReference.get(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activityWeakReference.get().getWindow().setStatusBarColor(Color.TRANSPARENT);
            isLighStatusBar = false;
        }
    }

    public void requestStatusBarLight() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && activityWeakReference.get() != null) {
            activityWeakReference.get().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setWindowFlag(activityWeakReference.get(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activityWeakReference.get().getWindow().setStatusBarColor(Color.TRANSPARENT);
            isLighStatusBar = true;
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
