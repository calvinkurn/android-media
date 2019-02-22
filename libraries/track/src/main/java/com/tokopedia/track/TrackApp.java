package com.tokopedia.track;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.GuardedBy;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Preconditions;

//import com.google.android.gms.common.util.ProcessUtils;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class TrackApp {
    private static final Object LOCK = new Object();

    private static final Object LOCK2 = new Object();

    public static final String GTM = "GTM";
    public static final String APPSFLYER = "Appsflyer";
    public static final String MOENGAGE = "MoEngage";

    @GuardedBy("LOCK")
    static final Map<String, TypedValue<? extends ContextAnalytics>> INSTANCES = new ArrayMap<>();

    @SuppressLint("StaticFieldLeak")
    @GuardedBy("LOCK2")
    private static TrackApp trackApp;

    private static final Class<?>[] CONTEXT_ANALYTICS_CONSTRUCTOR_SIGNATURE =
            new Class[]{Context.class};

    private final Application context;

    private final AtomicBoolean deleted = new AtomicBoolean();

    /**
     * @param context Application Context
     */
    private TrackApp(Application context) {
        this.context = context;
    }

    /**
     * @return
     */
    @Nullable
    public static TrackApp getInstance() {
        synchronized (LOCK2) {
            if (trackApp == null) {
                throw new IllegalStateException(
                        "Default TrackApp is not initialized in this "
                                + "process "
                                + ". make sure to call "
                                + "TrackApp.initTrackApp(Context) first."
                );
            }
            return trackApp;
        }
    }

    public static boolean deleteInstance() {
        synchronized (LOCK2) {
            if (trackApp != null) {
                trackApp = null;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * this method needs to be called before other methods.
     * otherwise it will break other methods.
     *
     * @param application
     * @return
     */
    public static TrackApp initTrackApp(Application application) {
        synchronized (LOCK2) {
            trackApp = new TrackApp(application);
            return trackApp;
        }
    }

    @SuppressWarnings("RestrictedApi")
    private void checkNotDeleted() {
        Preconditions.checkState(!deleted.get(), "TrackApp was deleted");
    }

    public void registerImplementation(String TAG, Class<? extends ContextAnalytics> className) {
        // determine if trackapp is initialized or not ??
        checkNotDeleted();

        // create class using reflection
        getInstance();

        // register to map
        synchronized (LOCK) {
            INSTANCES.put(TAG, createContextAnalytics(className));
        }
    }

    public void initializeAllApis() {
        checkNotDeleted();

        getInstance();

        synchronized (LOCK) {
            // get TrackApp object
            for (Iterator<String> iterator = INSTANCES.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                ContextAnalytics analytics = trackApp.getValue(key);
                analytics.initialize();
            }
        }
    }

    private <T extends ContextAnalytics> TypedValue<? extends ContextAnalytics> createContextAnalytics(Class<? extends ContextAnalytics> className) {
        if (className != null) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                Class<? extends ContextAnalytics> contextAnalyticsClass
                        = classLoader.loadClass(className.getName()).asSubclass(ContextAnalytics.class);

                Constructor<? extends ContextAnalytics> constructor;
                Object[] constructorArgs = null;
                try {
                    constructor = contextAnalyticsClass.getConstructor(CONTEXT_ANALYTICS_CONSTRUCTOR_SIGNATURE);
                    constructorArgs = new Object[]{context};
                } catch (NoSuchMethodException e) {
                    try {
                        constructor = contextAnalyticsClass.getConstructor();
                    } catch (NoSuchMethodException e1) {
                        e1.initCause(e);
                        throw new IllegalStateException(className.getName()
                                + ": Error creating LayoutManager " + className, e1);
                    }
                }
                constructor.setAccessible(true);
                return new TypedValue(className, constructor.newInstance(constructorArgs));
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(className.getName()
                        + ": Unable to find LayoutManager " + className, e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(className.getName()
                        + ": Could not instantiate the LayoutManager: " + className, e);
            } catch (InstantiationException e) {
                throw new IllegalStateException(className.getName()
                        + ": Could not instantiate the LayoutManager: " + className, e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(className.getName()
                        + ": Cannot access non-public constructor " + className, e);
            } catch (ClassCastException e) {
                throw new IllegalStateException(className.getName()
                        + ": Class is not a LayoutManager " + className, e);
            }
        }
        return null;
    }

    public void delete() {
        boolean valueChanged = deleted.compareAndSet(false /* expected */, true);
        if (!valueChanged) {
            return;
        }

        synchronized (LOCK) {
            INSTANCES.clear();
        }
    }

    public ContextAnalytics getGTM() {
        return getValue(GTM);
    }

    public ContextAnalytics getAppsFlyer() {
        return getValue(APPSFLYER);
    }

    public ContextAnalytics getMoEngage() {
        return getValue(MOENGAGE);
    }

    /**
     * outside of the library, use getGTM(), or getMoEngage() or getAppsFyler instead
     * This will change to private
     * @param TAG
     * @return
     */
    @Deprecated
    public ContextAnalytics getValue(String TAG) {
        if (!INSTANCES.containsKey(TAG))
            throw new RuntimeException(String.format("no instance related to this TAG : \'%s\' ", TAG));

        TypedValue<? extends ContextAnalytics> typedValue = INSTANCES.get(TAG);
        ContextAnalytics value = typedValue.value;
        return typedValue.type.cast(value);
    }

    static class TypedValue<T> {
        public final Class<T> type;
        public final T value;

        TypedValue(Class<T> type, T value) {
            this.type = type;
            this.value = value;
        }
    }


}
