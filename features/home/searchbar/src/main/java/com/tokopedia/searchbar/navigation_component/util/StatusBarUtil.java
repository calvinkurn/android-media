package com.tokopedia.searchbar.navigation_component.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.tokopedia.searchbar.R;

import java.lang.ref.WeakReference;

public class StatusBarUtil {

    private final WeakReference<Activity> activityWeakReference;

    public StatusBarUtil(WeakReference<Activity> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    @SuppressLint("DeprecatedMethod")
    public void requestStatusBarDark() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            //to trigger white text when tokopedia darkmode not on top page
            requestStatusBarLight();
        } else {
            // default for sdk under 23
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int statusBarColor = ContextCompat.getColor(
                    activityWeakReference.get(),
                    R.color.searchbar_dms_statusbar_darkmode
            );
            // add light status bar if above equal 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                statusBarColor = Color.TRANSPARENT;
            }

            activityWeakReference.get().getWindow().getDecorView().setSystemUiVisibility(flag);
            setWindowFlag(activityWeakReference.get(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activityWeakReference.get().getWindow().setStatusBarColor(statusBarColor);
        }
    }

    @SuppressLint("DeprecatedMethod")
    public void requestStatusBarLight() {
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        activityWeakReference.get().getWindow().getDecorView().setSystemUiVisibility(flag);
        setWindowFlag(activityWeakReference.get(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        activityWeakReference.get().getWindow().setStatusBarColor(Color.TRANSPARENT);
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