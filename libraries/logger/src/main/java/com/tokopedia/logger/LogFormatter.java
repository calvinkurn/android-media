package com.tokopedia.logger;

import android.annotation.SuppressLint;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    private static final String format = "%1$s %2$s: %3$s%4$s%n";
    private static final int MAX_BUFFER = 4096;
    private final Date dat = new Date();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault());

    @SuppressLint("DefaultLocale")
    public synchronized String format(LogRecord record) {
        dat.setTime(record.getMillis());
        String message = formatMessage(record);
        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        String str = String.format(format,
                sdf.format(dat),
                getLevelName(record),
                message,
                throwable);
        if (str.length() > MAX_BUFFER) {
            str = str.substring(0, MAX_BUFFER);
        }
        return str;
    }

    private String getLevelName(LogRecord record) {
        if (record.getLevel().equals(Level.WARNING)) {
            return "WARN";
        } else if (record.getLevel().equals(Level.SEVERE)) {
            return "SEVR";
        }
        return "INFO";
    }
}

