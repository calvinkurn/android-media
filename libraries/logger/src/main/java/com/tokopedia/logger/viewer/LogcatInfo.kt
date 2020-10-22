package com.tokopedia.logger.viewer

import java.util.regex.Pattern

class LogcatInfo private constructor() {
    var time: String? = null
        private set

    var level: String? = null
        private set

    var tag: String? = null
        private set

    var log: String? = null
        private set

    var pid: String? = null
        private set

    var threadId: String? = null
        private set

    fun addLog(text: String) {
        log = (if (log!!.startsWith(LINE_SPACE)) "" else LINE_SPACE) + log + LINE_SPACE + text
    }

    override fun toString(): String {
        return String.format("%s   %s   %s", time, tag, log)
    }

    companion object {
        private const val LINE_SPACE = "\n    "
        private val PATTERN: Pattern = Pattern.compile(
                "([0-9^-]+-[0-9^ ]+\\s[0-9^:]+:[0-9^:]+\\.[0-9]+)\\s+([0-9]+)\\s+([0-9]+)\\s([VDIWEF])\\s([^\\s]*)\\s*:\\s(.*)")
        val IGNORED_LOG = arrayListOf("--------- beginning of crash",
                "--------- beginning of main",
                "--------- beginning of system")

        fun create(line: String?): LogcatInfo? {
            val matcher = PATTERN.matcher(line)
            if (!matcher.find()) {
                return null
            }
            val info = LogcatInfo()
            info.time = matcher.group(1)
            info.pid = matcher.group(2)
            info.threadId = matcher.group(3)
            info.level = matcher.group(4)
            info.tag = matcher.group(5)
            info.log = matcher.group(6)
            return info
        }
    }
}