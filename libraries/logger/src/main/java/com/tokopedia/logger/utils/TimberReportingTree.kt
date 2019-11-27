package com.tokopedia.logger.utils

import android.os.Build
import android.util.Log
import com.tokopedia.logger.LogManager
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tree that used for Timber in release Version
 * If there is log, it might be sent to logging server
 */
class TimberReportingTree(private val tags: List<String>) : Timber.DebugTree() {

    var userId: String = ""
    var versionName: String = ""
    var versionCode: Int = 0
    var tagMaps: HashMap<String, Tag> = hashMapOf()

    init {
        populateTagMaps(tags)
    }

    override fun log(logPriority: Int, tag: String?, message: String, t: Throwable?) {
        val timeStamp = System.currentTimeMillis()
        if (logPriority == Log.VERBOSE || logPriority == Log.DEBUG || LogManager.instance == null) {
            return
        }
        if (!message.startsWith(PREFIX) || tags.isEmpty()) {
            return
        }
        val messageSplit = listOf(*message.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        if (messageSplit.size != SIZE_MESSAGE) {
            return
        }
        val messageKey = StringBuilder().append(messageSplit[0])
                .append("#")
                .append(messageSplit[1])
                .toString()
        tagMaps[messageKey]?.let {
            val priority = it.postPriority
            val classLine = tag ?: ""
            val processedMessage = getMessage(messageSplit[1], timeStamp, classLine, messageSplit[2])
            LogManager.log(processedMessage, timeStamp, priority, messageSplit.first())
        }
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format(
                "[%s:%s:%s]",
                super.createStackElementTag(element),
                element.methodName,
                element.lineNumber
        )
    }

    private fun getReadableTimeStamp(timeStamp: Long): String {
        return SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US).format(Date(timeStamp))
    }

    private fun getMessage(tag: String, timeStamp: Long, classLine: String, message: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("tag=")
                .append(tag)
                .append("#")
        stringBuilder.append("timestamp=")
                .append(timeStamp)
                .append("#")
        stringBuilder.append("time=")
                .append(getReadableTimeStamp(timeStamp))
                .append("#")
        stringBuilder.append("uid=")
                .append(userId)
                .append("#")
        stringBuilder.append("vernm=")
                .append(versionName)
                .append("#")
        stringBuilder.append("vercd=")
                .append(versionCode)
                .append("#")
        stringBuilder.append("os=")
                .append(Build.VERSION.RELEASE)
                .append("#")
        stringBuilder.append("device=")
                .append(Build.MODEL)
                .append("#")
        stringBuilder.append("cls=")
                .append(classLine)
                .append("#")
        stringBuilder.append("msg=")
                .append(message)
        return stringBuilder.toString()
    }

    private fun getPriority(tagPriority: String): Int {
        if (tagPriority == TAG_OFFLINE) {
            return PRIORITY_OFFLINE
        }
        return PRIORITY_ONLINE
    }

    private fun populateTagMaps(tags: List<String>) {
        for (tag in tags) {
            val tagSplit = listOf(*tag.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            if (tagSplit.size != SIZE_REMOTE_CONFIG_TAG) {
                continue
            }
            tagSplit[2].toDoubleOrNull()?.let {
                val randomNumber = Random().nextDouble() * MAX_RANDOM_NUMBER
                Timber.d("${tagSplit[0]}#${tagSplit[1]} randomNumber = $randomNumber")
                if (randomNumber < it) {
                    val tagKey = StringBuilder()
                            .append(tagSplit[0])
                            .append("#")
                            .append(tagSplit[1])
                            .toString()
                    tagMaps[tagKey] = Tag(getPriority(tagSplit[3]))
                }
            }
        }
    }

    companion object {
        const val MAX_RANDOM_NUMBER = 100

        const val SIZE_MESSAGE = 3
        const val SIZE_REMOTE_CONFIG_TAG = 4

        const val PREFIX = "P"
        const val P1 = "P1"
        const val P2 = "P2"

        const val TAG_OFFLINE = "offline"
        const val TAG_ONLINE = "online"

        const val PRIORITY_ONLINE = 2
        const val PRIORITY_OFFLINE = 1
    }

}