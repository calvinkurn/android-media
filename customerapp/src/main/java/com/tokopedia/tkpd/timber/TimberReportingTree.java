package com.tokopedia.tkpd.timber;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.logger.LogSentryWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

/**
 * Tree that used for Timber in release Version
 * If there is log, it might be sent to logging server
 */
public class TimberReportingTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }
        // throwable is concatenated to message. This will be fixed in Timber next major release
        // https://github.com/JakeWharton/timber/issues/142
        LogSentryWrapper.log(priority, tag + " " + message);
    }

    @Nullable
    protected String createStackElementTag(@NotNull StackTraceElement element) {
        return String.format("[%s:%s:%s]",
                super.createStackElementTag(element),
                element.getMethodName(),
                element.getLineNumber());
    }
}