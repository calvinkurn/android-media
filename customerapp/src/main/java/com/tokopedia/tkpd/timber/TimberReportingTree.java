package com.tokopedia.tkpd.timber;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.logger.LogWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import timber.log.Timber;

/**
 * Tree that used for Timber in release Version
 * If there is log, it might be sent to logging server
 */
public class TimberReportingTree extends Timber.DebugTree {

    public static final String PREFIX = "P";
    public static final String P1 = "P1";
    public static final String P2 = "P2";
    public static final String P3 = "P3";

    public static final int SEVERITY_HIGH = 1;
    public static final int SEVERITY_MEDIUM = 2;
    public static final int SEVERITY_LOW = 3;
    public static final int NO_SEVERITY = 0;

    public @Nullable
    List<Boolean> priorityList;

    public TimberReportingTree(@Nullable List<Boolean> priorityList) {
        this.priorityList = priorityList;
    }

    @Override
    protected void log(int logPriority, String tag, @NonNull String message, Throwable t) {
        if (logPriority == Log.VERBOSE || logPriority == Log.DEBUG) {
            return;
        }
        int serverSeverity = NO_SEVERITY;
        // only log the message starts with P
        if (message.startsWith(PREFIX) && priorityList != null && priorityList.size() > 2) {
            if (priorityList.get(0) && message.startsWith(P1)) {
                message = message.replaceFirst(P1, "");
                serverSeverity = SEVERITY_HIGH;
            } else if (priorityList.get(1) && message.startsWith(P2)) {
                message = message.replaceFirst(P2, "");
                serverSeverity = SEVERITY_MEDIUM;
            } else if (priorityList.get(2) && message.startsWith(P3)) {
                serverSeverity = SEVERITY_LOW;
            } else {
                serverSeverity = NO_SEVERITY;
            }
        }
        if (serverSeverity != NO_SEVERITY) {
            // throwable is concatenated to message. This will be fixed in Timber next major release
            // https://github.com/JakeWharton/timber/issues/142
            LogWrapper.log(serverSeverity, logPriority, tag + " " + message);
        }
    }

    @Nullable
    protected String createStackElementTag(@NotNull StackTraceElement element) {
        return String.format("[%s:%s:%s]",
                super.createStackElementTag(element),
                element.getMethodName(),
                element.getLineNumber());
    }
}