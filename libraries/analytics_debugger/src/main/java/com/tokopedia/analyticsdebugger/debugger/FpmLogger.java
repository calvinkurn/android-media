package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analyticsdebugger.debugger.data.source.FpmLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.domain.model.FpmFileLogModel;
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.FpmDebuggerActivity;
import com.tokopedia.config.GlobalConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class FpmLogger implements PerformanceLogger {
    private static final String FPM_DEBUGGER = "FPM_DEBUGGER";
    private static final String IS_FPM_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled";
    private static final String IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED = "is_auto_log_file_enabled";

    private static PerformanceLogger instance;

    private Context context;
    private FpmLogDBSource dbSource;
    private LocalCacheHandler cache;

    private FpmLogger(Context context) {
        this.context = context;
        this.dbSource = new FpmLogDBSource(context);
        this.cache = new LocalCacheHandler(context, FPM_DEBUGGER);
    }

    public static void init(Context context) {
        if(GlobalConfig.isAllowDebuggingTools()) {
            instance = new FpmLogger(context);
        } else {
            instance = emptyInstance();
        }
    }

    public static PerformanceLogger getInstance() {
        return instance;
    }

    @Override
    public void save(String traceName,
                     long startTime,
                     long endTime,
                     Map<String, String> attributes,
                     Map<String, Long> metrics) {
        try {
            PerformanceLogModel performanceLogModel = new PerformanceLogModel(traceName);
            performanceLogModel.setStartTime(startTime);
            performanceLogModel.setEndTime(endTime);
            performanceLogModel.setAttributes(attributes);
            performanceLogModel.setMetrics(metrics);

            dbSource.insertAll(performanceLogModel).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());

            if(cache.getBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, false)) {
                NotificationHelper.show(context, performanceLogModel);
            }

            if(cache.getBoolean(IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED, false)) {
                writeToFile(createFormattedDataString(performanceLogModel) + ",\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createFormattedDataString(PerformanceLogModel model) {

        FpmFileLogModel fpmFileLogModel = new FpmFileLogModel();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

        fpmFileLogModel.setTraceName(model.getTraceName());
        fpmFileLogModel.setDurationMs(model.getEndTime() - model.getStartTime());
        fpmFileLogModel.setMetrics(model.getMetrics());
        fpmFileLogModel.setAttributes(model.getAttributes());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date timestamp = new Date();

        fpmFileLogModel.setTimestampMs(timestamp.getTime());
        fpmFileLogModel.setTimestampFormatted(dateFormat.format(timestamp));

        try {
            return URLDecoder.decode(gson.toJson(fpmFileLogModel), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return gson.toJson(fpmFileLogModel);
        }
    }

    private void writeToFile(String data) {
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, "fpm-auto-log.txt");
            FileOutputStream stream = new FileOutputStream(file, true);
            stream.write(data.getBytes());
            stream.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void wipe() {
        dbSource.deleteAll().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    @Override
    public void openActivity() {
        context.startActivity(FpmDebuggerActivity.newInstance(context));
    }

    @Override
    public void enableAutoLogFile(boolean isEnabled) {
        cache.putBoolean(IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED, isEnabled);
        cache.applyEditor();
    }

    @Override
    public boolean isAutoLogFileEnabled() {
        return cache.getBoolean(IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED, false);
    }

    @Override
    public void enableNotification(boolean isEnabled) {
        cache.putBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, isEnabled);
        cache.applyEditor();
    }

    @Override
    public boolean isNotificationEnabled() {
        return cache.getBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, false);
    }

    private Subscriber<? super Boolean> defaultSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                // no-op
            }
        };
    }

    private static PerformanceLogger emptyInstance() {
        return new PerformanceLogger() {
            @Override
            public void save(String traceName,
                             long startTime,
                             long endTime,
                             Map<String, String> attributes,
                             Map<String, Long> metrics) {

            }

            @Override
            public void wipe() {

            }

            @Override
            public void openActivity() {

            }

            @Override
            public void enableAutoLogFile(boolean status) {

            }

            @Override
            public boolean isAutoLogFileEnabled() {
                return false;
            }

            @Override
            public void enableNotification(boolean status) {

            }

            @Override
            public boolean isNotificationEnabled() {
                return false;
            }
        };
    }
}
