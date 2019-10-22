package com.tokopedia.remoteconfig;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.GuardedBy;
import androidx.core.util.Preconditions;

import com.tokopedia.remoteconfig.abtest.AbTestPlatform;

import java.util.concurrent.atomic.AtomicBoolean;

public class RemoteConfigInstance {

    private static final Object LOCK = new Object();
    private final Application context;
    private final AtomicBoolean deleted = new AtomicBoolean();

    @SuppressLint("StaticFieldLeak")
    @GuardedBy("LOCK")
    private static RemoteConfigInstance abPlatformInstance;

    public RemoteConfigInstance(Application context) {
        this.context = context;
    }

    public static RemoteConfigInstance getInstance() {
        synchronized (LOCK) {
            if (abPlatformInstance == null) {
                throw new IllegalStateException(
                        "Default RemoteConfigInstance is not initialized in this process."
                        + " Make sure to call RemoteConfigInstance.initAbTest(Context first."
                );
            }
            return abPlatformInstance;
        }
    }

    public static boolean deleteInstance() {
        synchronized (LOCK) {
            if (abPlatformInstance != null) {
                abPlatformInstance = null;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Call this method first
     * @param application
     * @return
     */
    public static RemoteConfigInstance initAbTestPlatform(Application application) {
        synchronized(LOCK) {
            abPlatformInstance = new RemoteConfigInstance(application);
            return abPlatformInstance;
        }
    }

    @SuppressLint("RestrictedApi")
    private void checkNotDeleted() {
        Preconditions.checkState(!deleted.get(), "AB Test Instance was deleted!");
    }

    public AbTestPlatform getABTestPlatform() {
        checkNotDeleted();
        getInstance();

        synchronized(LOCK) {
            return new AbTestPlatform(context);
        }
    }


}
