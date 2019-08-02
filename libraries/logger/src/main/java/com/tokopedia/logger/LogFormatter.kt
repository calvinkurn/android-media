package com.tokopedia.logger

import android.annotation.SuppressLint

import java.io.PrintWriter
import java.io.StringWriter
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord

class LogFormatter : Formatter() {

    @SuppressLint("DefaultLocale")
    @Synchronized
    override fun format(record: LogRecord): String {
        val message = formatMessage(record)
        var throwable = ""
        if (record.thrown != null) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            pw.println()
            record.thrown.printStackTrace(pw)
            pw.close()
            throwable = sw.toString()
        }
        var str = String.format(format,
            getLevelName(record),
            message,
            throwable)
        if (str.length > MAX_BUFFER) {
            str = str.substring(0, MAX_BUFFER)
        }
        return str
    }

    private fun getLevelName(record: LogRecord): String {
        if (record.level == Level.WARNING) {
            return "WARN"
        } else if (record.level == Level.SEVERE) {
            return "SEVR"
        }
        return "INFO"
    }

    companion object {

        private val format = "%1\$s: %2\$s%3\$s%n"
        private val MAX_BUFFER = 3900
    }
}

