package com.tokopedia.logger.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.tokopedia.logger.LogManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

/**
 * Tree that used for Timber in release Version
 * If there is log, it might be sent to logging server
 */
public class TimberReportingTree extends Timber.DebugTree {
//    public TimberReportingTreeContract contract;

    String TAG = "TimberReportingTree";

    public static final String PREFIX = "P";
    public static final String P1 = "P1";
    public static final String P2 = "P2";
    public static final String P3 = "P3";

    public static final int SEVERITY_HIGH = 1;
    public static final int SEVERITY_MEDIUM = 2;
    public static final int SEVERITY_LOW = 3;
    public static final int SEVERITY_P1_Testing = 4;
    public static final int SEVERITY_P2_Testing = 5;
    public static final int NO_SEVERITY = 0;

    private int serverSeverity;

    private @Nullable List<String> tags;

    private Long AppVersionMin;

    public TimberReportingTree(@Nullable List<String> tags, Long AppVersionMin) {
        this.tags = tags;
        this.AppVersionMin = AppVersionMin;
//        this.contract = contract;
    }

    @Override
    protected void log(int logPriority, String tag, @NonNull String message, Throwable t) {
        // Get time stamp the moment log is called
        long timeStamp = getTimeStamp();
        int priority = getPriority(message);

        if (logPriority == Log.VERBOSE || logPriority == Log.DEBUG || LogManager.instance == null) {
//            contract.logNotImportant();
            return;
        }
        // only log the message starts with P

        // Checking based on config
        if (message.startsWith(PREFIX) && tags != null) {
            for (String tagString : tags) {
                LinkedList<String> tagSplit = new LinkedList<>(Arrays.asList(tagString.split("#")));
                if (tagSplit.size() == 2) {
                    tagSplit.add(2, "online");
                }
                if (message.contains(tagSplit.get(0))) {
                    if (message.contains(tagSplit.get(1))) {
                        serverSeverity = setSeverity(message);
                        message = getMessage(message);
                        LogManager.log(logToString(logPriority) + "#" + message + "#" + tag, timeStamp, priority);
                    }
                    // Catches if user forgets to enter the "online" or "offline" in the message
                    else if (!message.contains("offline")) {
                        serverSeverity = setSeverity(message);
                        message = getMessage(message);
                        LogManager.log(
                                logToString(logPriority) + "#" + message + "#" + tag, timeStamp, priority);
                    }
                }
            }
        }
    }

    private String logToString(int logPriority) {
        switch (logPriority) {
            case Log.ERROR:
                return "E";
            case Log.WARN:
                return "W";
            default:
                return "I";
        }
    }

    @Nullable
    protected String createStackElementTag(@NotNull StackTraceElement element) {
        return String.format("[%s:%s:%s]",
                super.createStackElementTag(element),
                element.getMethodName(),
                element.getLineNumber());
    }

    private Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    private String getReadableTimeStamp(Long timeStamp) {
        return new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).format(new Date(timeStamp));
    }

    private String getAppVersionName(Long appVersionCode) {
        String appVersionCodeStr = appVersionCode.toString();
        return appVersionCodeStr.substring(3, 5) +
                "." + appVersionCodeStr.substring(5, 7) +
                "." + appVersionCodeStr.substring(7);
    }

    private Long getAppVersionCode() {
        return this.AppVersionMin;
    }

    private String getMessage(String message) {
        return getReadableTimeStamp(getTimeStamp()) + "#" + getAppVersionCode() + "#" + getAppVersionName(getAppVersionCode()) + "#UserID=1#" + message;
    }

    private int setSeverity(String message) {
        if (message.startsWith(P1)) {
            serverSeverity = SEVERITY_P1_Testing;
        } else if (message.startsWith(P2)) {
            serverSeverity = SEVERITY_P2_Testing;
        } else if (message.startsWith(P3)) {
            serverSeverity = SEVERITY_LOW;
        } else {
            serverSeverity = NO_SEVERITY;
//                    contract.fail();
        }
        return serverSeverity;
    }

    private int getPriority(String message) {
        String[] messageSplit = message.split("#");
        int priority = 0;
        if (messageSplit[2].equals("offline")) {
            priority = 1;
            return priority;
        } else {
            return priority;
        }
    }

}