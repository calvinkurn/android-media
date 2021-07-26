package com.tokopedia.sellerapp.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.utils.network.NetworkTrafficUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", activityName);
        messageMap.put("count", String.valueOf(sessionCount));
        messageMap.put("open_page_total", String.valueOf(openedPageCountTotal));
        messageMap.put("open_page", String.valueOf(openedPageCount));
        messageMap.put("diff_time", getDiffDuration(lastSessionMillis, currentMillis));
        ServerLogger.log(Priority.P1, "ACTIVE_SESSION", messageMap);
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
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", activityName);
        messageMap.put("count", String.valueOf(sessionCount));
        messageMap.put("open_page_total", String.valueOf(openedPageCountTotal));
        messageMap.put("open_page", String.valueOf(openedPageCount));
        messageMap.put("diff_time", getDiffDuration(lastSessionMillis, currentMillis));
        messageMap.put("net", getFormattedMBSize(network));
        messageMap.put("tx", getFormattedMBSize(diffTx));
        messageMap.put("rx", getFormattedMBSize(diffRx));
        messageMap.put("sum_net", getFormattedMBSize(sumNetwork));
        messageMap.put("sum_tx", getFormattedMBSize(sumDiffTx));
        messageMap.put("sum_rx", getFormattedMBSize(sumDiffRx));
        messageMap.put("boot_net", getFormattedMBSize(bootNetwork));
        messageMap.put("boot_tx", getFormattedMBSize(bootTx));
        messageMap.put("boot_rx", getFormattedMBSize(bootRx));
        ServerLogger.log(Priority.P1, "DATA_USAGE", messageMap);
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