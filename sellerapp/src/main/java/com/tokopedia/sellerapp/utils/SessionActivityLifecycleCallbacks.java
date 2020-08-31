package com.tokopedia.sellerapp.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tokopedia.utils.network.NetworkTrafficUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class SessionActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TIME_FORMAT = "%.2f";
    private static final long INTERVAL_SESSION = TimeUnit.MINUTES.toMillis(1);

    private static final String SIZE_FORMAT = "%.2f";
    private static final long MB_SIZE = 1000000;

    private long lastSessionMillis = -1;
    private long sessionCount = 0;
    private int openedPageCount = 0;
    private int openedPageCountTotal = 0;

    private long firstSumTx = 0;
    private long firstSumRx = 0;
    private long lastSumTx = 0;
    private long lastSumRx = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        openedPageCount++;
        openedPageCountTotal++;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // No-op
    }

    @Override
    public void onActivityResumed(Activity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkSession(activity.getClass().getSimpleName());
            }
        }).start();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // No-op
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // No-op
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // No-op
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // No-op
    }

    private void checkSession(String activityName) {
        long currentMillis = System.currentTimeMillis();
        long minSessionTimeMillis = currentMillis - INTERVAL_SESSION;
        if (lastSessionMillis < minSessionTimeMillis) {
            sessionCount++;
            logSession(activityName, currentMillis);
            logDataUsage(activityName, currentMillis);
            lastSessionMillis = System.currentTimeMillis();
            openedPageCount = 0;
        }
    }

    private void logSession(String activityName, long currentMillis) {
        Timber.w("P1#ACTIVE_SESSION#%s;count=%s;open_page_total=%s;open_page=%s;diff_time=%s",
                activityName, sessionCount, openedPageCountTotal, openedPageCount, getDiffDuration(lastSessionMillis, currentMillis));
    }

    private String getDiffDuration(long startDuration, long stopDuration) {
        float diffTimeInMillis = 0;
        if (startDuration > 0 && startDuration < stopDuration) {
            diffTimeInMillis = (stopDuration - startDuration);
            diffTimeInMillis /= INTERVAL_SESSION;
        }
        return String.format(Locale.ENGLISH, TIME_FORMAT, diffTimeInMillis);
    }

    private void logDataUsage(String activityName, long currentMillis) {
        int uid = android.os.Process.myUid();
        long bootTx = NetworkTrafficUtils.INSTANCE.getUidTxBytes(uid);
        long bootRx = NetworkTrafficUtils.INSTANCE.getUidRxBytes(uid);
        long bootNetwork = bootTx + bootRx;
        if (bootTx <= 0 || bootRx <= 0) {
            return;
        }
        if (firstSumTx <= 0 || firstSumRx <= 0) {
            firstSumTx = bootTx;
            firstSumRx = bootRx;
        }
        if (lastSumTx <= 0 || lastSumRx <= 0) {
            updateLastSumTraffic(bootTx, bootRx);
        }
        long diffTx = bootTx - lastSumTx;
        long diffRx = bootRx - lastSumRx;
        long network = diffTx + diffRx;

        long sumDiffTx = bootTx - firstSumTx;
        long sumDiffRx = bootRx - firstSumRx;
        long sumNetwork = sumDiffTx + sumDiffRx;

        updateLastSumTraffic(bootTx, bootRx);
        Timber.w("P1#DATA_USAGE#%s;count=%s;open_page_total=%s;open_page=%s;diff_time=%s;net=%s;tx=%s;rx=%s;sum_net=%s;sum_tx=%s;sum_rx=%s;boot_net=%s;boot_tx=%s;boot_rx=%s",
                activityName, sessionCount, openedPageCountTotal, openedPageCount, getDiffDuration(lastSessionMillis, currentMillis),
                getFormattedMBSize(network), getFormattedMBSize(diffTx), getFormattedMBSize(diffRx),
                getFormattedMBSize(sumNetwork), getFormattedMBSize(sumDiffTx), getFormattedMBSize(sumDiffRx),
                getFormattedMBSize(bootNetwork), getFormattedMBSize(bootTx), getFormattedMBSize(bootRx));
    }

    private void updateLastSumTraffic(long currentSumTx, long currentSumRx) {
        lastSumTx = currentSumTx;
        lastSumRx = currentSumRx;
    }

    private String getFormattedMBSize(long sizeInByte) {
        float sizeInMB = (float) sizeInByte / MB_SIZE;
        return String.format(Locale.ENGLISH, SIZE_FORMAT, sizeInMB);
    }
}