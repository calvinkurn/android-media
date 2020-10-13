package com.tokopedia.logger.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogcatManager {

    private static volatile Listener sListener;
    /**
     * Log capture Mark
     */
    private static volatile boolean FLAG_WORK;
    /**
     * Alternate storage collection
     */
    private static final List<LogcatInfo> LOG_BACKUP = new ArrayList<>();

    // Start Capturing
    static void start(Listener listener) {
        FLAG_WORK = true;
        new Thread(new LogRunnable()).start();
        sListener = listener;
    }

    //Keep capturing
    static void resume() {
        FLAG_WORK = true;
        final Listener listener = sListener;
        if (listener != null && !LOG_BACKUP.isEmpty()) {
            for (LogcatInfo info : LOG_BACKUP) {
                if (info != null) {
                    listener.onReceiveLog(info);
                }
            }
        }
        LOG_BACKUP.clear();
    }

    //Pause capture
    static void pause() {
        FLAG_WORK = false;
    }

    //StopCapture
    static void destroy() {
        FLAG_WORK = false;
        sListener = null;
    }

    //clear logcat
    static void clear() {
        try {
            new ProcessBuilder("logcat", "-c").start();
            FLAG_WORK = true;
            new Thread(new LogRunnable()).start();
        } catch (IOException ignored) {
        }
    }

    private static class LogRunnable implements Runnable {

        @Override
        public void run() {
            BufferedReader reader = null;
            try {
                Process process = new ProcessBuilder("logcat", "-v", "threadtime").start();
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    synchronized (LogcatManager.class) {
                        if (LogcatInfo.Companion.getIGNORED_LOG().contains(line)) {
                            continue;
                        }
                        LogcatInfo info = LogcatInfo.Companion.create(line);
                        if (info == null) {
                            continue;
                        }
                        if (FLAG_WORK) {
                            final Listener listener = sListener;
                            if (listener != null) {
                                listener.onReceiveLog(info);
                            }
                        } else {
                            LOG_BACKUP.add(info);
                        }
                    }
                }
                pause();
            } catch (IOException ignored) {
                pause();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    public interface Listener {
        void onReceiveLog(LogcatInfo info);
    }
}